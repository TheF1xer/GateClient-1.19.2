package me.thef1xer.gateclient.commands;

import me.thef1xer.gateclient.commands.nodes.StringNode;

public class Command {
    private final StringNode node;

    public Command(String name) {
        this.node = new StringNode(name);
        init(node);
    }

    public String getName() {
        return node.getName();
    }

    public void init(StringNode commandNode) {

    }

    public void executeCommand(String[] args) {
        node.passArguments(args, 0);
    }
}
