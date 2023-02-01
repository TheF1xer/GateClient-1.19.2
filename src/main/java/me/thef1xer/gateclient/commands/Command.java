package me.thef1xer.gateclient.commands;

public class Command {
    private final String name;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
