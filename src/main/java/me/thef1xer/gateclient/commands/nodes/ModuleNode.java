package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.modules.Module;

public class ModuleNode extends CommandNode<Module> {
    public ModuleNode(String name) {
        super(name);
    }

    @Override
    public boolean tryParse(String s) {
        for (Module module : GateClient.getGateClient().moduleManager.moduleList) {
            if (s.equalsIgnoreCase(module.getId())) {
                setParseResult(module);
                return true;
            }
        }

        return false;
    }
}
