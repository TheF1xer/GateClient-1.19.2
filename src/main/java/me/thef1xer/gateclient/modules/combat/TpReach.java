package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.utils.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class TpReach extends Module {
    public TpReach() {
        super("TP Reach", "tpreach", GLFW.GLFW_KEY_K, ModuleCategory.COMBAT);
    }

    public void spam() {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;
        double[] lookVec = MathUtil.getDirection(player.getYaw(), player.getPitch());

        Vec3d startVec = player.getEyePos();
        Vec3d endVec = startVec.add(lookVec[0] * 10, lookVec[1] * 10, lookVec[2] * 10);
        Entity target = selectTargetEntity(startVec, endVec);

        if (target == null) return;

        System.out.println(target.getName().getString());
    }

    private Entity selectTargetEntity(Vec3d start, Vec3d end) {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        Entity target = null;

        // Select closest entity that can be the player is looking at
        for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
            if (entity == player) continue;

            if (rayTraceHit(start, end, entity.getBoundingBox())) {
                if (target == null || player.squaredDistanceTo(target) < player.squaredDistanceTo(entity)) {
                    target = entity;
                }
            }
        }

        return target;
    }

    private boolean rayTraceHit(Vec3d rayStart, Vec3d rayEnd, Box aabb) {
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

        if (updateLambdaIntervalOneDimension(lambdaInterval, rayStart.x, rayEnd.x, aabb.minX, aabb.maxX)) return false;
        if (updateLambdaIntervalOneDimension(lambdaInterval, rayStart.y, rayEnd.y, aabb.minY, aabb.maxY)) return false;
        if (updateLambdaIntervalOneDimension(lambdaInterval, rayStart.z, rayEnd.z, aabb.minZ, aabb.maxZ)) return false;

        if (lambdaInterval[0] <= lambdaInterval[1]) {
            return true;
        }

        System.out.println(lambdaInterval[0] + " " + lambdaInterval[1] + " ");
        System.out.println(rayStart + " " + rayEnd + " " + aabb);
        return false;
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
