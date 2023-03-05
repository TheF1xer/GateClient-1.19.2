package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.commands.CommandNode;

public class FloatNode extends CommandNode<Float> {
    public FloatNode(String name) {
        super(name);
    }

    @Override
    public boolean tryParse(String s) {
        try {
            setParseResult(Float.parseFloat(s));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
