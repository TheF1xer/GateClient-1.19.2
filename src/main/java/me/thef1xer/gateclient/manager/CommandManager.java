package me.thef1xer.gateclient.manager;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.impl.SayCommand;
import me.thef1xer.gateclient.utils.ChatUtil;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private String prefix = ":";
    public final List<Command> commandList = new ArrayList<>();

    public void init() {
        commandList.add(new SayCommand());
    }

    public void processCommand(String[] args) {
        for (Command command : commandList) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                command.executeCommand(args);
                return;
            }
        }

        ChatUtil.clientMessage(Formatting.RED + "Command not found");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
