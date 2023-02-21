package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.nodes.AnyStringNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SayCommand extends Command {
    public SayCommand() {
        super("say");
    }

    @Override
    public void init(StringNode commandNode) {
        commandNode.then(new AnyStringNode("<message>")).executes(nodeList -> {
            AnyStringNode messageNode = (AnyStringNode) nodeList.get(1);
            String s = messageNode.getParseResult();

            MinecraftClient.getInstance().player.sendChatMessage(s, Text.literal(s));
        });
    }
}
