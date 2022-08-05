package de.keule.mc.grapplinghook.utils;

import org.bukkit.Bukkit;

import de.keule.mc.grapplinghook.main.GHPlugin;

public class VersionUtil {
	private static boolean newApi = false;

	public static void versionCheck() {
		final String serverVersion = Bukkit.getBukkitVersion().split("-")[0];
		if (!serverVersion.contains("1.8") && !serverVersion.contains("1.9") && !serverVersion.contains("1.10")
				&& !serverVersion.contains("1.11") && !serverVersion.contains("1.12")) {
			GHPlugin.sendConsoleMesssage(
					"&2Server version " + serverVersion + " identified. Plugin successfully loaded!");
		} else {
			GHPlugin.sendConsoleMesssage("&4Unsupported version (not tested)! &7Try the other .jar file!");
		}
	}

	public static boolean isNewApi() {
		return newApi;
	}
}