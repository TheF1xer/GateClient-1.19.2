package me.thef1xer.gateclient.commands;

import me.thef1xer.gateclient.commands.nodes.StringNode;

import java.util.ArrayList;
import java.util.List;

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
        List<CommandNode<?>> nodeList = new ArrayList<>();
        node.passArguments(args, 0, nodeList);
    }
}
