package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.commands.nodes.AnyStringNode;
import me.thef1xer.gateclient.commands.nodes.ModuleNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.utils.ChatUtil;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class BindCommand extends Command {
    public CommandNode<String> keyNode;
    public CommandNode<Module> moduleNode;

    public BindCommand() {
        super("bind");
    }

    @Override
    public void init(StringNode commandNode) {
        this.moduleNode = commandNode.then(new ModuleNode("<module>")).executes(() -> {
            Module module = this.moduleNode.getParseResult();
            String keybind = GLFW.glfwGetKeyName(module.getKeyBind(), GLFW.glfwGetKeyScancode(module.getKeyBind()));

            ChatUtil.clientMessage(this.moduleNode.getParseResult().getName() + Formatting.GRAY + " is bound to " + Formatting.WHITE + Formatting.BOLD + keybind);
        });

        this.keyNode = this.moduleNode.then(new AnyStringNode("<key>")).executes(() -> {
            if (this.keyNode.getParseResult().length() != 1) {
                ChatUtil.clientError("Only one key must be provided");
                return;
            }

            char keycode = this.keyNode.getParseResult().toUpperCase().charAt(0);
            this.moduleNode.getParseResult().setKeyBind(keycode);
            ChatUtil.clientMessage(this.moduleNode.getParseResult().getName() + Formatting.GRAY + " bound to " + Formatting.WHITE + Formatting.BOLD + keycode);
        });
    }
}
