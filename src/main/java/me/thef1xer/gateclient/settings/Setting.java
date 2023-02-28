package me.thef1xer.gateclient.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class Setting {
    private final String name;
    private final String id;

    public Setting(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCommandSyntax() {
        return "";
    }

    public JsonElement getAsJsonElement() {
        return new JsonPrimitive("");
    }

    public void setFromJsonElement(JsonElement element) {

    }

    public boolean loadFromString(String s) {
        return true;
    }
}
