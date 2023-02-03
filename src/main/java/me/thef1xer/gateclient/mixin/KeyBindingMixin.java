package me.thef1xer.gateclient.mixin;

import me.thef1xer.gateclient.handlers.ClientEventHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

    @Inject(at = @At("HEAD"), method = "onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V")
    private static void onKeyPressed(InputUtil.Key key, CallbackInfo callbackInfo) {
        ClientEventHandler.onKeyPressed(key);
    }
}
