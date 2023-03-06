package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.utils.MathUtil;
import me.thef1xer.gateclient.utils.RayTraceHit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;

public class TpReach extends Module {
    public TpReach() {
        super("TP Reach", "tpreach", GLFW.GLFW_KEY_K, ModuleCategory.COMBAT);
    }

    public void spam() {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;
        final ClientWorld world = MinecraftClient.getInstance().world;
        double[] lookVec = MathUtil.getDirection(player.getYaw(), player.getPitch());

        Vec3d startVec = player.getEyePos();
        Vec3d endVec = startVec.add(lookVec[0] * 10, lookVec[1] * 10, lookVec[2] * 10);
        RayTraceHit.EntityHit entityHit = selectTargetEntity(startVec, endVec);

        if (entityHit.entity() == null) return;

        BlockHitResult blockHitResult = world.raycast(new RaycastContext(startVec, entityHit.hitPos(), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        if (blockHitResult != null) {
            if (blockHitResult.getType() != HitResult.Type.MISS) return;
        }

        System.out.println(entityHit.entity().getName().getString());
    }

    private RayTraceHit.EntityHit selectTargetEntity(Vec3d start, Vec3d end) {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        Entity target = null;
        Vec3d hitPos = null;

        // Select closest entity that can be the player is looking at
        for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
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

    private Vec3d rayTraceHit(Vec3d rayStart, Vec3d rayEnd, Box aabb) {
        /*
        Algorithm based in linear algebra to determine if AABB gets hit by ray

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

    private boolean updateLambdaIntervalOneDimension(double[] currentInterval, double rayStart, double rayEnd, double boxMin, double boxMax) {
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
