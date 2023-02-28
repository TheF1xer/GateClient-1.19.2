package me.thef1xer.gateclient.manager;

import com.google.gson.*;
import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.utils.DirectoryUtil;

import java.io.*;
import java.util.Map;

public class ProfileManager {
    private File activeProfile = new File(DirectoryUtil.PROFILE_FOLDER, "default.json");
    private boolean autoSave = true;

    public void init() {
        DirectoryUtil.PROFILE_FOLDER.mkdirs();
        this.loadProfile();
    }

    public File getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(File activeProfile) {
        this.activeProfile = activeProfile;
    }

    public boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public void saveProfile() {
        JsonObject profileJson = new JsonObject();

        profileJson.addProperty("auto-save", autoSave);
        JsonObject moduleListObj = new JsonObject();
        profileJson.add("modules", moduleListObj);

        // Fill Modules Array
        for (Module module : GateClient.getGateClient().moduleManager.moduleList) {
            JsonObject moduleObject = new JsonObject();
            moduleListObj.add(module.getId(), moduleObject);

            // Fill Module Object
            moduleObject.addProperty("enabled", module.isEnabled());
            moduleObject.addProperty("keybind", module.getKeyBind());

            JsonObject settingListObj = new JsonObject();
            moduleObject.add("settings", settingListObj);

            // Fill Setting Array
            for (Setting setting : module.getSettings()) {
                settingListObj.add(setting.getId(), setting.getAsJsonElement());
            }
        }

        // Write to file
        try {
            FileWriter writer = new FileWriter(activeProfile);
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

            writer.write(gson.toJson(profileJson));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProfile() {
        if (!this.activeProfile.exists()) {
            this.activeProfile = new File(DirectoryUtil.PROFILE_FOLDER, "default.json");

            if (!this.activeProfile.exists()) {
                this.autoSave = true;       // Auto-Save is on by default
                saveProfile();              // Save the current settings to create new default profile
                return;
            }
        }

        // Parse File
        JsonObject profileObj;

        try {
            profileObj = (new Gson()).fromJson(new FileReader(activeProfile), JsonObject.class);
        } catch (FileNotFoundException e) {
            saveProfile();
            return;
        }

        // Read JSON
        for (Map.Entry<String, JsonElement> entry : profileObj.entrySet()) {
            if (entry.getKey().equals("auto-save")) {
                setAutoSave(entry.getValue().getAsBoolean());
                continue;
            }

            if (entry.getKey().equals("modules")) {
                readModuleListObj(entry.getValue().getAsJsonObject());
            }
        }

        saveProfile();                      // Save after loading to make sure profile file is up-to-date
    }

    private void readModuleListObj(JsonObject moduleListObj) {
        for (Map.Entry<String, JsonElement> entry : moduleListObj.entrySet()) {
            for (Module module : GateClient.getGateClient().moduleManager.moduleList) {

                // Module found
                if (module.getId().equals(entry.getKey())) {
                    for (Map.Entry<String, JsonElement> moduleEntry : entry.getValue().getAsJsonObject().entrySet()) {

                        if (moduleEntry.getKey().equals("enabled")) {
                            module.setEnabled(moduleEntry.getValue().getAsBoolean());
                            continue;
                        }

                        if (moduleEntry.getKey().equals("keybind")) {
                            module.setKeyBind(moduleEntry.getValue().getAsInt());
                        }

                        if (moduleEntry.getKey().equals("settings")) {
                            readSettingListObj(module, moduleEntry.getValue().getAsJsonObject());
                        }
                    }
                }
            }
        }
    }

    public void readSettingListObj(Module module, JsonObject settingListObj) {
        for (Map.Entry<String, JsonElement> entry : settingListObj.entrySet()) {
            for (Setting setting : module.getSettings()) {

                // Setting found
                if (setting.getId().equals(entry.getKey())) {
                    setting.setFromJsonElement(entry.getValue());
                }

            }
        }
    }
}
