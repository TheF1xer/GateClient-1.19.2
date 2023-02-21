package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.commands.CommandNode;

public class AnyStringNode extends CommandNode<String> {
    public AnyStringNode(String name) {
        super(name);
    }

    @Override
    public boolean tryParse(String s) {
        setParseResult(s);
        return true;
    }
}
