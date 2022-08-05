package de.keule.mc.grapplinghook.config;

import org.bukkit.ChatColor;

import de.keule.mc.grapplinghook.main.GrapplingHook;
import de.keule.mc.grapplinghook.utils.NoFallDamage;

public class Settings {
	private static long fallDamageRemove;
	private static boolean allRods;
	private static String prefix;

	public static void reloadValues() {
		prefix = ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString(ConfigKey.PREFIX));
		allRods = ConfigManager.getConfig().getBlooean(ConfigKey.ALL_RODS);

		fallDamageRemove = (long) (ConfigManager.getConfig().getDouble(ConfigKey.FALL_DMG_RM) * 20);
		fallDamageRemove = fallDamageRemove <= 10 ? 50 : fallDamageRemove;
		NoFallDamage.cleareCache();

		GrapplingHook.reloadGrapplingHooks();
	}

	public static long getFallDamageRemove() {
		return fallDamageRemove;
	}

	public static boolean isAllRods() {
		return allRods;
	}

	public static String getPrefix() {
		return prefix;
	}
}