package me.thef1xer.gateclient.settings.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.thef1xer.gateclient.settings.Setting;

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
    public JsonElement getAsJsonElement() {
        return new JsonPrimitive(value);
    }

    @Override
    public void setFromJsonElement(JsonElement element) {
        setValue(element.getAsFloat());
    }

    @Override
    public boolean loadFromString(String s) {
        try {
            setValue(Float.parseFloat(s));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
