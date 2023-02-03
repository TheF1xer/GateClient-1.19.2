package me.thef1xer.gateclient.manager;

import me.thef1xer.gateclient.utils.ChatUtil;

public class CommandManager {
    private String prefix = ":";

    public void processCommand(String[] args) {
        ChatUtil.clientMessage("Command: " + args.length);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
