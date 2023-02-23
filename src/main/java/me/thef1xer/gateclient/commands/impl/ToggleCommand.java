package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.commands.nodes.ModuleNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;
import me.thef1xer.gateclient.modules.Module;

public class ToggleCommand extends Command {
    public CommandNode<Module> moduleNode;

    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public void init(StringNode commandNode) {
        this.moduleNode = commandNode.then(new ModuleNode("<module>")).executes(() -> this.moduleNode.getParseResult().toggle());
    }
}
