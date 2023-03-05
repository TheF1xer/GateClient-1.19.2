package me.thef1xer.gateclient.manager;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.impl.*;
import me.thef1xer.gateclient.utils.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private String prefix = ":";
    public final List<Command> commandList = new ArrayList<>();

    public void init() {
        commandList.add(new BindCommand());
        commandList.add(new ClipCommand());
        commandList.add(new SayCommand());
        commandList.add(new SetCommand());
        commandList.add(new ToggleCommand());
    }

    public void processCommand(String[] args) {
        for (Command command : commandList) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                command.executeCommand(args);
                return;
            }
        }

        ChatUtil.clientError("Command not found");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
