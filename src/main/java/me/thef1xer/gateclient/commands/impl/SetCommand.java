package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.commands.nodes.ModuleNode;
import me.thef1xer.gateclient.commands.nodes.ModuleSettingNode;
import me.thef1xer.gateclient.commands.nodes.ModuleSettingValueNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.utils.ChatUtil;
import net.minecraft.util.Formatting;

public class SetCommand extends Command {
    public SetCommand() {
        super("set");
    }

    @Override
    public void init(StringNode commandNode) {
        CommandNode<Module> moduleNode = commandNode.then(new ModuleNode("<module>")).executes(nodeList -> {
            ModuleNode mNode = (ModuleNode) nodeList.get(1);

            ChatUtil.clientMessage(mNode.getParseResult().getName() + " settings:");
            for (Setting setting : mNode.getParseResult().getSettings()) {
                ChatUtil.clientInfo(setting.getName() + Formatting.ITALIC + " (" + setting.getId() + ")");
            }
        });

        CommandNode<Setting> settingNode = moduleNode.then(new ModuleSettingNode("<setting>", moduleNode));

        settingNode.then(new ModuleSettingValueNode("<value>", settingNode)).executes(nodeList -> {
            ModuleSettingNode sNode = (ModuleSettingNode) nodeList.get(2);
            ModuleSettingValueNode msvNode = (ModuleSettingValueNode) nodeList.get(3);

            sNode.getParseResult().loadFromString(msvNode.getParseResult());
            GateClient.getGateClient().profileManager.saveProfile();
            ChatUtil.clientMessage(sNode.getParseResult().getName() + " set to " + msvNode.getParseResult());
        });
    }
}
