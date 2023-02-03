package me.thef1xer.gateclient.commands;

import me.thef1xer.gateclient.commands.nodes.StringNode;
import me.thef1xer.gateclient.utils.ChatUtil;

public class Command {
    private final CommandNode node;

    public Command(String name) {
        this.node = new StringNode(name);
        init(node);
    }

    public String getName() {
        return node.getName();
    }

    public void init(CommandNode commandNode) {

    }

    public void executeCommand(String[] args) {
        if (node.passArguments(args, 0)) {
            return;
        }

        syntaxError();
    }

    public void syntaxError() {
        ChatUtil.clientMessage("syntax");
    }
}
