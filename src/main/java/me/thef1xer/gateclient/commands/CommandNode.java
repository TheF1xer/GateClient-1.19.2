package me.thef1xer.gateclient.commands;

import me.thef1xer.gateclient.utils.ChatUtil;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class CommandNode<T> {
    private final String name;
    private T parseResult;
    private Consumer<List<CommandNode<?>>> consumer;
    private final List<CommandNode<?>> children = new ArrayList<>();

    public CommandNode(String name) {
        this.name = name;
        this.consumer = null;
        this.parseResult = null;
    }

    public String getName() {
        return name;
    }

    public void setParseResult(T parseResult) {
        this.parseResult = parseResult;
    }

    public T getParseResult() {
        return parseResult;
    }

    public CommandNode<T> executes(Consumer<List<CommandNode<?>>> consumer) {
        this.consumer = consumer;
        return this;
    }

    public <S> CommandNode<S> then(CommandNode<S> newNode) {
        children.add(newNode);
        return newNode;
    }

    public abstract boolean tryParse(String s);

    public boolean passArguments(String[] args, int argIndex, List<CommandNode<?>> nodeList) {
        /* Returns true if the passed arguments coincide with a node */

        if (!tryParse(args[argIndex])) return false;

        nodeList.add(this);                     // Add current node if successfully parsed

        // Last stop
        if (argIndex == args.length - 1) {
            if (consumer == null) {
                syntaxError(args, argIndex);
            } else {
                consumer.accept(nodeList);
            }

            return true;
        }

        // More children to check
        for (CommandNode<?> childrenNode : children) {
            if (childrenNode.passArguments(args, argIndex + 1, nodeList)) {
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
