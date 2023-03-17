package me.thef1xer.gateclient.mixin;

import me.thef1xer.gateclient.handlers.ModuleEventHandler;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo callbackInfo) {
        ModuleEventHandler.onTick();
    }

    @Inject(at = @At("HEAD"), method = "doAttack()Z", cancellable = true)
    public void doAttack(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ModuleEventHandler.doAttack(callbackInfoReturnable);
    }
}
