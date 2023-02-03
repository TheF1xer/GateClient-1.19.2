package me.thef1xer.gateclient.handlers;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ClientEventHandler {

    /* ChatScreen */

    public static void onSendMessage(String chatText, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (chatText.startsWith(GateClient.getGateClient().commandManager.getPrefix())) {
            final int prefixLen = GateClient.getGateClient().commandManager.getPrefix().length();
            String[] args = chatText.substring(prefixLen).split("\\s+");    // Split on multiple spaces too
            GateClient.getGateClient().commandManager.processCommand(args);

            callbackInfoReturnable.setReturnValue(true);                        // Closes Chat GUI
            callbackInfoReturnable.cancel();
        }
    }

    /* KeyBinding */

    public static void onKeyPressed(InputUtil.Key key) {
        // Toggle modules
        for (Module module : GateClient.getGateClient().moduleManager.moduleList) {
            if (module.getKeyBind() == key.getCode()) {
                module.toggle();
            }
        }
    }
}
