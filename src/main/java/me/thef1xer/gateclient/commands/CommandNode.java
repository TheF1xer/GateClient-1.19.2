package me.thef1xer.gateclient.commands;

import me.thef1xer.gateclient.utils.ChatUtil;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class CommandNode {
    private final String name;
    private Consumer<String> consumer;
    private final List<CommandNode> children = new ArrayList<>();

    public CommandNode(String name) {
        this.name = name;
        this.consumer = null;
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

        // Last stop
        if (argIndex == args.length - 1) {
            if (consumer == null) {
                syntaxError(args, argIndex);
            } else {
                consumer.accept(args[argIndex]);
            }

            return true;
        }

        // More children to check
        for (CommandNode childrenNode : children) {
            if (childrenNode.passArguments(args, argIndex + 1)) {
                return true;
            }
        }

        // More args than children
        syntaxError(args, argIndex + 1);
        return true;
    }

    private void syntaxError(String[] args, int argIndex) {
        StringBuilder syntaxMessage = new StringBuilder("Syntax error at: ");

        for (int i = 0; i < argIndex; i++) {
            syntaxMessage.append(args[i]).append(" ");
        }
        syntaxMessage.append(Formatting.BOLD).append(args[argIndex]);     // Important one should be bold

        ChatUtil.clientError(syntaxMessage.toString());
    }
}
