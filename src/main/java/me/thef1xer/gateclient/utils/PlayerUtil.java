package me.thef1xer.gateclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class PlayerUtil {

    /*
     Returns a double[] in the form (x, z)
     */
    public static double[] getPlayerHorizontalMoveVec() {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;
        final float forward = player.input.movementForward;
        final float sideways = player.input.movementSideways;

        if (forward == 0 && sideways == 0) return new double[] {0, 0};  // If there's no movement, return null vec

        float angle = player.getYaw();

        // Change yaw depending on the direction the playing is travelling
        if (forward > 0) {

            // Moving forward
            if (sideways > 0) {
                angle = angle - 45;
            } else if (sideways < 0) {
                angle = angle + 45;
            }

        } else if (forward < 0) {

            // Moving backwards
            angle = angle - 180;
            if (sideways > 0) {
                angle = angle + 45;
            } else if (sideways < 0) {
                angle = angle - 45;
            }

        } else {

            // Not moving forward nor backwards
            if (sideways > 0) {
                angle = angle - 90;
            } else if (sideways < 0) {
                angle = angle + 90;
            }
        }

        return new double[] {-MathHelper.sin((float) Math.toRadians(angle)), MathHelper.cos((float) Math.toRadians(angle))};
    }
}
