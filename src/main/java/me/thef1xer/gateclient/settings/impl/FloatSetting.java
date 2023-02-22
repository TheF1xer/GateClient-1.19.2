package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.utils.NumberUtil;

public class FloatSetting extends Setting {
    private float value;
    private final float min;
    private final float max;

    public FloatSetting(String name, String id, float value, float min, float max) {
        super(name, id);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    @Override
    public String valueAsString() {
        return Float.toString(value);
    }

    @Override
    public boolean canLoadFromString(String s) {
        return NumberUtil.isFloat(s);
    }

    @Override
    public void loadFromString(String s) {
        setValue(Float.parseFloat(s));
    }
}
