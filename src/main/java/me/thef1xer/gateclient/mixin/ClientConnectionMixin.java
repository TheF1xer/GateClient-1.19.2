package me.thef1xer.gateclient.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.thef1xer.gateclient.handlers.ModuleEventHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V")
    public void send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo callbackInfo) {
        if (packet instanceof PlayerMoveC2SPacket) {
            ModuleEventHandler.onSendPlayerMoveC2SPacket((PlayerMoveC2SPacket) packet);
        } else if (packet instanceof VehicleMoveC2SPacket) {
            ModuleEventHandler.onSendVehicleMoveC2SPacket((VehicleMoveC2SPacket) packet);
        }
    }

    @Inject(at = @At("HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callbackInfo) {
        if (packet instanceof PlayerPositionLookS2CPacket) {
            ModuleEventHandler.onRcvPlayerPositionLookS2CPacket((PlayerPositionLookS2CPacket) packet);
        }
    }
}
