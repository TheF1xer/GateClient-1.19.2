package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.commands.CommandNode;

public class AnyStringNode extends CommandNode {
    public AnyStringNode(String name) {
        super(name);
    }

    @Override
    public boolean isNode(String s) {
        return true;
    }
}
