package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.Setting;

public class ModuleSettingNode extends CommandNode<Setting> {
    private final CommandNode<Module> moduleNode;

    public ModuleSettingNode(String name, CommandNode<Module> moduleNode) {
        super(name);
        this.moduleNode = moduleNode;
    }

    @Override
    public boolean tryParse(String s) {
        for (Setting setting : moduleNode.getParseResult().getSettings()) {
            if (setting.getId().equalsIgnoreCase(s)) {
                setParseResult(setting);
                return true;
            }
        }

        return false;
    }

    public record ModuleSettingPair(Module module, Setting setting) {}
}
