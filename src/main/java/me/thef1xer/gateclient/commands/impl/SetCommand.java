package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.commands.nodes.AnyStringNode;
import me.thef1xer.gateclient.commands.nodes.ModuleNode;
import me.thef1xer.gateclient.commands.nodes.ModuleSettingNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.utils.ChatUtil;
import net.minecraft.util.Formatting;

public class SetCommand extends Command {
    public CommandNode<Module> moduleNode;
    public CommandNode<Setting> settingNode;
    public CommandNode<String> valueNode;

    public SetCommand() {
        super("set");
    }

    @Override
    public void init(StringNode commandNode) {
        this.moduleNode = commandNode.then(new ModuleNode("<module>")).executes(() -> {

            ChatUtil.clientMessage(this.moduleNode.getParseResult().getName() + " settings:");
            for (Setting setting : this.moduleNode.getParseResult().getSettings()) {
                ChatUtil.clientInfo(setting.getName() + Formatting.ITALIC + " (" + setting.getId() + ")");
            }

        });

        this.settingNode = this.moduleNode.then(new ModuleSettingNode("<setting>", this.moduleNode));

        this.valueNode = this.settingNode.then(new AnyStringNode("<value>")).executes(() -> {

            if (this.settingNode.getParseResult().loadFromString(this.valueNode.getParseResult())) {
                GateClient.getGateClient().profileManager.saveProfile();

                ChatUtil.clientMessage(
                        this.settingNode.getParseResult().getName() + Formatting.GRAY + " set to " + Formatting.RESET + this.valueNode.getParseResult()
                );
                return;
            }

            ChatUtil.clientError("Value provided is not valid");
        });
    }
}
