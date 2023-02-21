package me.thef1xer.gateclient.commands.nodes;

import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.settings.Setting;

public class ModuleSettingValueNode extends CommandNode<String> {
    private final CommandNode<Setting> moduleSettingNode;

    public ModuleSettingValueNode(String name, CommandNode<Setting> moduleSettingNode) {
        super(name);
        this.moduleSettingNode = moduleSettingNode;
    }

    @Override
    public boolean tryParse(String s) {
        if (moduleSettingNode.getParseResult().canLoadFromString(s)) {
            setParseResult(s);
            return true;
        }

        return false;
    }
}
