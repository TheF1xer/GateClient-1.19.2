package me.thef1xer.gateclient.handlers;

import me.thef1xer.gateclient.modules.Modules;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ModuleEventHandler {

    /* ClientPlayerEntity */

    public static void onMove(MovementType movementType, Vec3d movement) {
        if (Modules.NO_MOVE_EVENT.isEnabled()) {
            Modules.NO_MOVE_EVENT.onMove(movementType, movement);
        }
    }

    public static void onSendMovementPackets(CallbackInfo callbackInfo) {
        if (Modules.NO_MOVE_EVENT.isEnabled()) {
            Modules.NO_MOVE_EVENT.onSendMovementPackets(callbackInfo);
        }
    }

    /* ClientPlayNetworkHandler */

    public static void onPlayerMoveC2SPacket(PlayerMoveC2SPacket packet) {
        if (Modules.LO_BOT_SPOOF.isEnabled()) {
            Modules.LO_BOT_SPOOF.onPlayerMoveC2SPacket(packet);
        }
    }

    public static void onVehicleMoveC2SPacket(VehicleMoveC2SPacket packet) {
        if (Modules.LO_BOT_SPOOF.isEnabled()) {
            Modules.LO_BOT_SPOOF.onVehicleMoveC2SPacket(packet);
        }
    }
}
