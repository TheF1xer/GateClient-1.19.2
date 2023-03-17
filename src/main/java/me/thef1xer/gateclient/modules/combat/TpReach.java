package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.utils.MathUtil;
import me.thef1xer.gateclient.utils.RayTraceHit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Stack;

public class TpReach extends Module {
    private RayTraceHit.EntityHit targetEntity = null;

    public final BooleanSetting blocksStop = addSetting(new BooleanSetting("Blocks Stop", "blocksstop", true));
    public final FloatSetting reach = addSetting(new FloatSetting("Reach", "reach", 10, 0, 100));
    public final FloatSetting straightDistance = addSetting(new FloatSetting("Straight Distance", "strdistance", 3, 0, 10));

    public TpReach() {
        super("TP Reach", "tpreach", GLFW.GLFW_KEY_K, ModuleCategory.COMBAT);
    }

    public void onTick() {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;
        final ClientWorld world = MinecraftClient.getInstance().world;
        double[] lookVec = MathUtil.getDirection(player.getYaw(), player.getPitch());

        this.targetEntity = null;           // Null until entity is found

        // Ray Trace target
        Vec3d startVec = player.getEyePos();
        Vec3d endVec = startVec.add(
                lookVec[0] * reach.getValue(),
                lookVec[1] * reach.getValue(),
                lookVec[2] * reach.getValue()
        );

        RayTraceHit.EntityHit entityHit = selectTargetEntity(startVec, endVec);

        if (entityHit.entity() == null) return;

        // Check if blocks should stop
        if (blocksStop.getValue()) {
            if (isBlockBetween(startVec, entityHit.hitPos(), world, player)) return;
        }

        this.targetEntity = entityHit;
    }

    public void doAttack(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (targetEntity == null) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Stack<Packet<?>> positionPacketStack = new Stack<>();

        sendPacketsStraightLine(player.getPos(), targetEntity.hitPos(), straightDistance.getValue(), positionPacketStack);
        player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(targetEntity.entity(), false));

        for (Packet<?> packet : positionPacketStack) {
            MinecraftClient.getInstance().player.networkHandler.sendPacket(packet);
        }

        callbackInfoReturnable.setReturnValue(false);
        callbackInfoReturnable.cancel();
    }

    private static void sendPacketsStraightLine(Vec3d currentPos, Vec3d hitPos, float packetDistance, Stack<Packet<?>> packetStack) {
        final float MIN_DIST_SQUARED = 9.0F;
        final double distToTravel = currentPos.squaredDistanceTo(hitPos) - MIN_DIST_SQUARED;
        final Vec3d unitVec = hitPos.subtract(currentPos).normalize();
        float currentMultiplier = 0;

        while (currentMultiplier * currentMultiplier < distToTravel) {

            Vec3d packetPos = currentPos.add(unitVec.multiply(currentMultiplier));

            // Send packet
            PlayerMoveC2SPacket.PositionAndOnGround packet = new PlayerMoveC2SPacket.PositionAndOnGround(
                    packetPos.x,
                    packetPos.y,
                    packetPos.z,
                    true
            );

            packetStack.push(packet);
            MinecraftClient.getInstance().player.networkHandler.sendPacket(packet);

            currentMultiplier += packetDistance;
        }
    }

    private static boolean isBlockBetween(Vec3d start, Vec3d end, ClientWorld world, Entity entity) {
        HitResult hitResult = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));

        if (hitResult == null) return false;

        return hitResult.getType() == HitResult.Type.BLOCK;
    }

    private static RayTraceHit.EntityHit selectTargetEntity(Vec3d start, Vec3d end) {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        Entity target = null;
        Vec3d hitPos = null;

        // Select closest entity that can be the player is looking at
        for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {

            // TODO: discard entities that are not in the same "quadrant"
            if (entity == player) continue;

            Vec3d newHitVec = rayTraceHit(start, end, entity.getBoundingBox());
            if (newHitVec == null) continue;

            if (target == null || player.squaredDistanceTo(target) < player.squaredDistanceTo(entity)) {
                target = entity;
                hitPos = newHitVec;
            }
        }

        return new RayTraceHit.EntityHit(target, hitPos);
    }

    private static Vec3d rayTraceHit(Vec3d rayStart, Vec3d rayEnd, Box aabb) {
        /*
        Algorithm based on linear algebra to determine if AABB gets hit by ray

        C1 <= (lambda)(V2-V1) + V1 <= C2, 0 <= lambda <= 1
        (Not real math notation, I am "comparing" vectors)

        C1: AABB min vector
        C2: AABB max vector
        V1: ray start
        V2: ray end
        lambda: real number between 0 and 1

        This algorithm checks if there exists a point between rayStart and rayEnd that is inside the
        AABB box
         */

        double[] lambdaInterval = {0, 1};

        if (updateLambdaIntervalOneDimension(lambdaInterval, rayStart.x, rayEnd.x, aabb.minX, aabb.maxX)) return null;
        if (updateLambdaIntervalOneDimension(lambdaInterval, rayStart.y, rayEnd.y, aabb.minY, aabb.maxY)) return null;
        if (updateLambdaIntervalOneDimension(lambdaInterval, rayStart.z, rayEnd.z, aabb.minZ, aabb.maxZ)) return null;

        if (lambdaInterval[0] <= lambdaInterval[1]) {
            return rayStart.add((rayEnd.subtract(rayStart)).multiply(lambdaInterval[0]));
        }

        return null;
    }

    private static boolean updateLambdaIntervalOneDimension(double[] currentInterval, double rayStart, double rayEnd, double boxMin, double boxMax) {
        // This function returns true if the check fails

        if (rayStart == rayEnd) {
            return boxMin > rayStart || rayStart > boxMax;                 // Check if the ray hits when rayStart and rayEnd are equal
        }

        // Compute new bounds
        double bound1 = (boxMin - rayStart) / (rayEnd - rayStart);
        double bound2 = (boxMax - rayStart) / (rayEnd - rayStart);

        // Check new bounds
        if (bound1 < bound2) {
            currentInterval[0] = Math.max(bound1, currentInterval[0]);
            currentInterval[1] = Math.min(bound2, currentInterval[1]);
            return false;
        }

        currentInterval[0] = Math.max(bound2, currentInterval[0]);
        currentInterval[1] = Math.min(bound1, currentInterval[1]);
        return false;
    }
}
