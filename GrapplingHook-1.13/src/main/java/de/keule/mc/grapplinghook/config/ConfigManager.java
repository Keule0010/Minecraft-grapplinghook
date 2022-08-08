package de.keule.mc.grapplinghook.config;

import java.io.File;

import org.bukkit.plugin.Plugin;

public class ConfigManager {
	private static ConfigFile msgConfig;
	private static ConfigFile ghConfig;
	private static ConfigFile config;

	public static void init(Plugin pl) {
		msgConfig = new ConfigFile(pl, "messages.yml");
		ghConfig = new ConfigFile(pl, "ghConfig.yml");
		config = new ConfigFile(pl, "config.yml");

		backUpOldConfig(pl);
		reloadAll();
	}

//	public static boolean saveReloadAll() {
//		boolean msg = msgConfig.saveAndReload();
//		boolean gh = ghConfig.saveAndReload();
//		boolean cfg = config.saveAndReload();
//
//		Settings.reloadValues();
//		return msg && gh && cfg;
//	}

	public static void saveAll() {
		msgConfig.save();
		ghConfig.save();
		config.save();
	}

	public static boolean reloadAll() {
		boolean msg = msgConfig.reload();
		boolean gh = ghConfig.reload();
		boolean cfg = config.reload();

		Settings.reloadValues();
		return msg && gh && cfg;
	}

	public static boolean saveGHConfigReloadAll() {
		boolean gh = getGrapplingHookConfig().save();
		return reloadAll() && gh;
	}

	public static boolean saveConfigReloadAll() {
		boolean gh = getConfig().save();
		return reloadAll() && gh;
	}

	public static boolean saveGHConfig() {
		return getGrapplingHookConfig().save();
	}

	public static ConfigFile getGrapplingHookConfig() {
		return ghConfig;
	}

	public static ConfigFile getMsgConfig() {
		return msgConfig;
	}

	public static ConfigFile getConfig() {
		return config;
	}

	private static void backUpOldConfig(Plugin pl) {
		if (!getConfig().pathExists("config-version")) {
			new File(pl.getDataFolder(), "config.yml").renameTo(new File(pl.getDataFolder(), "config_old_BA.yml"));
			config = new ConfigFile(pl, "config.yml");
		}
	}
}