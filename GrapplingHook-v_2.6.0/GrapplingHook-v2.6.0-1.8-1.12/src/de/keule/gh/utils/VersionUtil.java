package de.keule.gh.utils;

import org.bukkit.Bukkit;

import de.keule.gh.main.GHPlugin;

public class VersionUtil {
	private static boolean isAbove11 = false;

	public static void versionCheck() {
		final String serverVersion = Bukkit.getBukkitVersion().split("-")[0];
		if (serverVersion.contains("1.8") || serverVersion.contains("1.9") || serverVersion.contains("1.10")
				|| serverVersion.contains("1.11") || serverVersion.contains("1.12")) {
			isAbove11 = serverVersion.contains("1.11") || serverVersion.contains("1.12");
			GHPlugin.sendConsoleMesssage(
					"&2Server version " + serverVersion + " identified. Plugin successfully loaded!");
		} else {
			GHPlugin.sendConsoleMesssage("&4Unsupported version! &7Try the other .jar file!");
			Bukkit.getPluginManager().disablePlugin(GHPlugin.getInstance());
		}
	}

	public static boolean isAbove11() {
		return isAbove11;
	}
}
