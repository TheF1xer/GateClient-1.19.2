package me.thef1xer.gateclient;

import me.thef1xer.gateclient.manager.CommandManager;
import me.thef1xer.gateclient.manager.ConfigManager;
import me.thef1xer.gateclient.manager.ModuleManager;
import me.thef1xer.gateclient.manager.ProfileManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GateClient implements ModInitializer {
	private static GateClient gateClient;
	public static String NAME = "Gate Client";
	public static final Logger LOGGER = LoggerFactory.getLogger("gateclient");

	public final ModuleManager moduleManager = new ModuleManager();
	public final ConfigManager configManager = new ConfigManager();
	public final ProfileManager profileManager = new ProfileManager();
	public final CommandManager commandManager = new CommandManager();

	public static GateClient getGateClient() {
		return GateClient.gateClient;
	}

	@Override
	public void onInitialize() {
		GateClient.LOGGER.info("Initializing " + GateClient.NAME);
		GateClient.gateClient = this;

		this.moduleManager.init();
		this.configManager.init();
		this.profileManager.init();
		this.commandManager.init();

		GateClient.LOGGER.info(GateClient.NAME + " initialized");
	}
}
