package me.thef1xer.gateclient.commands;

import me.thef1xer.gateclient.utils.ChatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class CommandNode {
    private final String name;
    private Consumer<String> consumer;
    private final List<CommandNode> children = new ArrayList<>();

    public CommandNode(String name) {
        this.name = name;
        this.consumer = s -> ChatUtil.clientMessage("Syntax error");
    }

    public String getName() {
        return name;
    }

    public void executes(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public CommandNode then(CommandNode newNode) {
        children.add(newNode);
        return newNode;
    }

    public abstract boolean isNode(String s);

    public boolean passArguments(String[] args, int argIndex) {
        /* Returns true if the passed arguments coincide with a node */

        if (!isNode(args[argIndex])) return false;

        if (argIndex == args.length - 1) {
            consumer.accept(args[argIndex]);
            return true;
        }

        for (CommandNode childrenNode : children) {
            if (childrenNode.passArguments(args, argIndex + 1)) {
                return true;
            }
        }

        return false;
    }
}
