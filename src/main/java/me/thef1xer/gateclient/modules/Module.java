package me.thef1xer.gateclient.modules;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.utils.ChatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private final String name;
    private final String id;
    private boolean enabled = false;
    private int keyBind;
    private final ModuleCategory moduleCategory;

    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String id, ModuleCategory category) {
        this(name, id, -1, category);
    }

    public Module(String name, String id, int keyBind, ModuleCategory category) {
        this.name = name;
        this.id = id;
        this.keyBind = keyBind;
        this.moduleCategory = category;
    }

    public void onEnable() {
        GateClient.getGateClient().profileManager.saveProfile();

        if (MinecraftClient.getInstance().player != null) {
            ChatUtil.clientMessage(name + Formatting.GRAY + " toggled " + Formatting.GREEN + "on");
        }
    }

    public void onDisable() {
        if (GateClient.getGateClient().profileManager.getAutoSave()) {
            GateClient.getGateClient().profileManager.saveProfile();
        }

        if (MinecraftClient.getInstance().player != null) {
            ChatUtil.clientMessage(name + Formatting.GRAY + " toggled " + Formatting.RED + "off");
        }
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enabled = true;
            this.onEnable();
        } else {
            this.enabled = false;
            this.onDisable();
        }
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    public int getKeyBind() {
        return this.keyBind;
    }

    public void setKeyBind(int key) {
        this.keyBind = key;
    }

    public ModuleCategory getModuleCategory() {
        return this.moduleCategory;
    }

    public <T extends Setting> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public enum ModuleCategory {
        COMBAT("Combat", 0xE54343),
        HUD("HUD", 0xDC33E4),
        MOVEMENT("Movement", 0x6399FF),
        PLAYER("Player", 0xFF8031),
        RENDER("Render", 0xFFDF29);

        private final String name;
        private final int color;

        ModuleCategory(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public int getColor() {
            return color;
        }
    }
}
