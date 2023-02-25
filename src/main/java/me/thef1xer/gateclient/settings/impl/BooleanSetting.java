package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;

public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, String id, boolean value) {
        super(name, id);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String valueAsString() {
        return String.valueOf(value);
    }

    @Override
    public boolean loadFromString(String s) {
        if (s.equals("true")) {
            setValue(true);
            return true;
        }

        if (s.equals("false")) {
            setValue(false);
            return true;
        }

        return false;
    }
}
