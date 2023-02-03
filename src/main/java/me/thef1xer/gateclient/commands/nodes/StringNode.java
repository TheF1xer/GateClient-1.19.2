package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.commands.CommandNode;

public class StringNode extends CommandNode {
    public StringNode(String name) {
        super(name);
    }

    @Override
    public boolean isNode(String s) {
        return s.equalsIgnoreCase(getName());
    }
}
