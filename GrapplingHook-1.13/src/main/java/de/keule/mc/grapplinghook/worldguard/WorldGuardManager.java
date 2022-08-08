package de.keule.mc.grapplinghook.worldguard;

import org.bukkit.Bukkit;

public class WorldGuardManager {
	private static boolean firstCall = true;
	private static boolean enabled = false;

	public static boolean isWorldGuardEnabled() {
		if (firstCall) {
			enabled = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null;
			firstCall = false;
		}
		
		return enabled;
	}
}