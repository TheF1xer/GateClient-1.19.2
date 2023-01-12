package me.thef1xer.gateclient.mixin;

import me.thef1xer.gateclient.handlers.ModuleEventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(at = @At("HEAD"), method = "sendPacket(Lnet/minecraft/network/Packet;)V")
    public void sendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        if (packet instanceof PlayerMoveC2SPacket) {
            ModuleEventHandler.onPlayerMoveC2SPacket((PlayerMoveC2SPacket) packet);
        } else if (packet instanceof VehicleMoveC2SPacket) {
            ModuleEventHandler.onVehicleMoveC2SPacketAccessor((VehicleMoveC2SPacket) packet);
        }

    }
}
