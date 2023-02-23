package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.utils.NumberUtil;

public class IntSetting extends Setting {
    private int value;
    private final int min;
    private final int max;

    public IntSetting(String name, String id, int value, int min, int max) {
        super(name, id);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String valueAsString() {
        return Integer.toString(value);
    }

    @Override
    public boolean loadFromString(String s) {
        try {
            setValue(Integer.parseInt(s));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
