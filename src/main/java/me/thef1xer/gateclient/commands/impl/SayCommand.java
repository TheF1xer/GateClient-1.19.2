package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.commands.nodes.AnyStringNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SayCommand extends Command {
    public SayCommand() {
        super("say");
    }

    @Override
    public void init(CommandNode commandNode) {
        commandNode.then(new AnyStringNode("<message>")).executes(s -> MinecraftClient.getInstance().player.sendChatMessage(s, Text.literal(s)));
    }
}
