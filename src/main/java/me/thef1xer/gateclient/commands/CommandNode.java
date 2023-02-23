package me.thef1xer.gateclient.commands;

import me.thef1xer.gateclient.utils.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandNode<T> {
    private final String name;
    private T parseResult;
    private CommandAction action;
    private final List<CommandNode<?>> children = new ArrayList<>();

    public CommandNode(String name) {
        this.name = name;
        this.action = null;
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

    public CommandNode<T> executes(CommandAction consumer) {
        this.action = consumer;
        return this;
    }

    public <S> CommandNode<S> then(CommandNode<S> newNode) {
        children.add(newNode);
        return newNode;
    }

    public abstract boolean tryParse(String s);

    public boolean passArguments(String[] args, int argIndex) {
        /* Returns true if the passed arguments coincide with a node */

        if (!tryParse(args[argIndex])) return false;

        // Last stop
        if (argIndex == args.length - 1) {
            if (action == null) {
                ChatUtil.syntaxErrorMessage(args, argIndex);
            } else {
                action.executeCommand();
            }

            return true;
        }

        // More children to check
        for (CommandNode<?> childrenNode : children) {
            if (childrenNode.passArguments(args, argIndex + 1)) {
                return true;
            }
        }

        // More args than children
        ChatUtil.syntaxErrorMessage(args, argIndex + 1);
        return true;
    }

}
