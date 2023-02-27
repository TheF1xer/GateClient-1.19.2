package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.mixin.PlayerMoveC2SPacketAccessor;
import me.thef1xer.gateclient.mixin.VehicleMoveC2SPacketAccessor;
import me.thef1xer.gateclient.modules.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

public class LOBotSpoof extends Module {
    public LOBotSpoof() {
        super("LO's Bot Spoof", "lobotspoof", GLFW.GLFW_KEY_M, ModuleCategory.MOVEMENT);
    }

    public void onSendPlayerMoveC2SPacket(PlayerMoveC2SPacket packet) {
        if (packet.changesPosition()) {
            double[] spoofedPos = spoofPosition(packet.getX(0), packet.getZ(0));
            ((PlayerMoveC2SPacketAccessor) packet).setX(spoofedPos[0]);
            ((PlayerMoveC2SPacketAccessor) packet).setZ(spoofedPos[1]);
        }
    }

    public void onSendVehicleMoveC2SPacket(VehicleMoveC2SPacket packet) {
        double[] spoofedPos = spoofPosition(packet.getX(), packet.getZ());
        ((VehicleMoveC2SPacketAccessor) packet).setX(spoofedPos[0]);
        ((VehicleMoveC2SPacketAccessor) packet).setZ(spoofedPos[1]);
    }

    private double[] spoofPosition(double posX, double posZ) {
        long extraX = ((long) (posX * 1000)) % 10;
        long extraZ = ((long) (posZ * 1000)) % 10;

        return new double[] {
                posX - ((double) extraX / 1000.0),
                posZ - ((double) extraZ / 1000.0)
        };
    }
}
