package me.thef1xer.gateclient.mixin;

import me.thef1xer.gateclient.handlers.ModuleEventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
    public void move(MovementType movementType, Vec3d movement, CallbackInfo callbackInfo) {
        ModuleEventHandler.onMove(movementType, movement);
    }

    @Inject(at = @At("HEAD"), method = "sendMovementPackets()V", cancellable = true)
    public void sendMovementPackets(CallbackInfo callbackInfo) {
        ModuleEventHandler.onSendMovementPackets(callbackInfo);
    }
}
