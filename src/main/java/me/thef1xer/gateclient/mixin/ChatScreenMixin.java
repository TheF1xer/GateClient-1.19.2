package me.thef1xer.gateclient.mixin;

import me.thef1xer.gateclient.handlers.ClientEventHandler;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(at = @At("HEAD"), method = "sendMessage(Ljava/lang/String;Z)Z", cancellable = true)
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ClientEventHandler.onSendMessage(chatText, callbackInfoReturnable);
    }
}
