package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.commands.CommandNode;

public class StringNode extends CommandNode<String> {
    public StringNode(String name) {
        super(name);
    }

    @Override
    public boolean tryParse(String s) {
        if (s.equalsIgnoreCase(getName())) {
            setParseResult(s);
            return true;
        }

        return false;
    }
}
