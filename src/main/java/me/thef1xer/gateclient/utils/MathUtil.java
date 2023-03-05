package me.thef1xer.gateclient.utils;

import net.minecraft.util.math.MathHelper;

public class MathUtil {

    public static double[] getDirection(float yaw, float pitch) {
        float yawSin = MathHelper.sin((float) Math.toRadians(yaw));
        float yawCos = MathHelper.cos((float) Math.toRadians(yaw));
        float pitchSin = MathHelper.sin((float) Math.toRadians(pitch));
        float pitchCos = MathHelper.cos((float) Math.toRadians(pitch));

        return new double[] {
                - yawSin * pitchCos,        // Gotta love how Minecraft's angles work
                - pitchSin,
                yawCos * pitchCos
        };
    }

    public static double[] getHorizontalDirection(float yaw) {
        return new double[] {
                - MathHelper.sin((float) Math.toRadians(yaw)), MathHelper.cos((float) Math.toRadians(yaw))
        };
    }
}
