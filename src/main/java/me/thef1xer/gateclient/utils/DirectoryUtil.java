package me.thef1xer.gateclient.utils;

import me.thef1xer.gateclient.GateClient;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class DirectoryUtil {
    public static final File GATE_FOLDER = new File(MinecraftClient.getInstance().runDirectory, GateClient.NAME + "/1.19.2");
    public static final File PROFILE_FOLDER = new File(GATE_FOLDER, "Profiles");
}
