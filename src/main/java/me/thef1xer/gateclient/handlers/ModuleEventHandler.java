package me.thef1xer.gateclient.handlers;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.modules.Modules;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public class ModuleEventHandler {

    public static void onKeyPressed(InputUtil.Key key) {

        // Toggle modules
        for (Module module : GateClient.getGateClient().moduleManager.moduleList) {
            if (module.getKeyBind() == key.getCode()) {
                module.toggle();
            }
        }
    }

    public static void onPlayerMoveC2SPacket(PlayerMoveC2SPacket packet) {
        if (Modules.LO_BOT_SPOOF.isEnabled()) {
            Modules.LO_BOT_SPOOF.onPlayerMoveC2SPacket(packet);
        }
    }

    public static void onVehicleMoveC2SPacketAccessor(VehicleMoveC2SPacket packet) {
        if (Modules.LO_BOT_SPOOF.isEnabled()) {
            Modules.LO_BOT_SPOOF.onVehicleMoveC2SPacketAccessor(packet);
        }
    }
}
