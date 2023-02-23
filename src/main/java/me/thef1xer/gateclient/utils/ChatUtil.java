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

    public static void clientError(String message) {
        message = Formatting.GRAY + "[" + Formatting.BLUE + GateClient.NAME + Formatting.GRAY + "] " + Formatting.RED + message;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(message));
    }

    public static void clientInfo(String message) {
        message = Formatting.GRAY + "[" + Formatting.BLUE + GateClient.NAME + Formatting.GRAY + "] " + Formatting.YELLOW + Formatting.ITALIC + message;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(message));
    }

    public static void syntaxErrorMessage(String[] args, int argIndex) {
        StringBuilder syntaxMessage = new StringBuilder("Syntax error at: ");

        for (int i = 0; i < argIndex; i++) {
            syntaxMessage.append(args[i]).append(" ");
        }
        syntaxMessage.append(Formatting.BOLD).append(args[argIndex]);     // Important one should be bold

        ChatUtil.clientError(syntaxMessage.toString());
    }
}
