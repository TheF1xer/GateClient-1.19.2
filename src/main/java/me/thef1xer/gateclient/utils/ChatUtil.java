package me.thef1xer.gateclient.utils;

import me.thef1xer.gateclient.GateClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtil {

    public static void clientMessage(String message) {
        message = Formatting.GRAY + "[" + Formatting.BLUE + GateClient.NAME + Formatting.GRAY + "] " + Formatting.RESET + message;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(message));
    }
}
