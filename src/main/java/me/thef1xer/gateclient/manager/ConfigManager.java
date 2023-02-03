package me.thef1xer.gateclient.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.utils.DirectoryUtil;

import java.io.*;
import java.util.Map;

public class ConfigManager {
    public final File CONFIG_FILE = new File(DirectoryUtil.GATE_FOLDER, "config.json");

    public void init() {
        DirectoryUtil.GATE_FOLDER.mkdirs();
        this.load();
    }

    public void save() {
        JsonObject configJson = new JsonObject();
        configJson.addProperty("active_profile", (new File(DirectoryUtil.PROFILE_FOLDER, "default.json")).toString());
        configJson.addProperty("prefix", GateClient.getGateClient().commandManager.getPrefix());

        try {
            FileWriter writer = new FileWriter(CONFIG_FILE);
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

            writer.write(gson.toJson(configJson));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        // Parse File
        JsonObject configObj;

        try {
            configObj = (new Gson()).fromJson(new FileReader(CONFIG_FILE), JsonObject.class);
        } catch (FileNotFoundException e) {
            save();
            return;
        }

        // Read JSON
        for (Map.Entry<String, JsonElement> entry : configObj.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getAsString());

            if (entry.getKey().equals("prefix")) {
                GateClient.getGateClient().commandManager.setPrefix(entry.getValue().getAsString());
            }
        }

        save();         // Save after loading to make sure config file is up-to-date
    }
}
