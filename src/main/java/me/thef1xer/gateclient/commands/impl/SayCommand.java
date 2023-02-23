package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.commands.nodes.AnyStringNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SayCommand extends Command {
    public CommandNode<String> messageNode;

    public SayCommand() {
        super("say");
    }

    @Override
    public void init(StringNode commandNode) {
        this.messageNode = commandNode.then(new AnyStringNode("<message>")).executes(() -> {
            String message = this.messageNode.getParseResult();
            MinecraftClient.getInstance().player.sendChatMessage(message, Text.literal(message));
        });
    }
}
