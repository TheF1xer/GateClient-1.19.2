package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.nodes.ModuleNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public void init(StringNode commandNode) {
        commandNode.then(new ModuleNode("<module>")).executes(nodeList -> {
            ModuleNode moduleNode = (ModuleNode) nodeList.get(1);
            moduleNode.getParseResult().toggle();
        });
    }
}
