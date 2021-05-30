package de.keule.gh.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import de.keule.gh.main.GHPlugin;
import de.keule.gh.utils.CooldownUtil;
import de.keule.gh.utils.NoFallDamageUtil;

public class ConfigManager {
	private final static GHPlugin pl = GHPlugin.getInstance();

	public static void init() {
		// Save old configuration file
		YamlConfiguration con = YamlConfiguration.loadConfiguration(new File(pl.getDataFolder(), "config.yml"));
		if (con.getString("WorldList") != null)
			new File(pl.getDataFolder(), "config.yml").renameTo(new File(pl.getDataFolder(), "config_old.yml"));

		// Load defaults
		for (ConfigKeys key : ConfigKeys.values())
			pl.getConfig().addDefault(key.KEY, key.DEFAULT_VALUE);

		pl.getConfig().options().copyDefaults(true);
		save();
		reload();
	}

	public static void restoreFactorySettings() {
		for (ConfigKeys key : ConfigKeys.values())
			pl.getConfig().set(key.KEY, key.DEFAULT_VALUE);
		save();
		reload();
	}

	public static void set(ConfigKeys key, Object value) {
		pl.getConfig().set(key.KEY, value);
		save();
	}

	public static List<String> getLore(int uses) {
		List<String> lore = new ArrayList<>();
		for (String string : pl.getConfig().getStringList(ConfigKeys.LORE.KEY))
			lore.add(ChatColor.translateAlternateColorCodes('&', string).replace("%prefix%", getPrefix())
					.replace("%uses%", uses + ""));
		String l = " ";
		for (char c : (uses + "").toCharArray())
			l += "§" + c + " ";
		lore.add(l);
		return lore;
	}

	public static List<String> getLore() {
		List<String> lore = new ArrayList<>();
		for (String string : pl.getConfig().getStringList(ConfigKeys.LORE.KEY))
			lore.add(ChatColor.translateAlternateColorCodes('&', string).replace("%prefix%", getPrefix())
					.replace("%uses%", getMaxUsesAsString()));
		if (getBoolean(ConfigKeys.UNLIMTED_USES)) {
			lore.add("§a");
			return lore;
		}
		String l = " ";
		for (char c : (ConfigManager.getInt(ConfigKeys.MAX_USES) + "").toCharArray())
			l += "§" + c + " ";
		lore.add(l);
		return lore;
	}

	public static String getMaxUsesAsString() {
		return getBoolean(ConfigKeys.UNLIMTED_USES) ? getMessage(ConfigKeys.UNLIMITED_NAME)
				: getInt(ConfigKeys.MAX_USES) + "";
	}

	public static String getMessage(ConfigKeys key) {
		return ChatColor.translateAlternateColorCodes('&', getString(key)).replace("%prefix%", getPrefix());
	}

	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString(ConfigKeys.PREFIX.KEY));
	}

	public static String getNoPerm() {
		return getMessage(ConfigKeys.NO_PERMS_MSG);
	}

	public static String getString(ConfigKeys key) {
		final String s = pl.getConfig().getString(key.KEY);
		return s == null? "&cError!&7Config deleted?!": s;
	}

	public static List<String> getList(ConfigKeys key) {
		return pl.getConfig().getStringList(key.KEY);
	}

	public static boolean getBoolean(ConfigKeys key) {
		return pl.getConfig().getBoolean(key.KEY);
	}

	public static double getDouble(ConfigKeys key) {
		return pl.getConfig().getDouble(key.KEY);
	}

	public static int getInt(ConfigKeys key) {
		return pl.getConfig().getInt(key.KEY);
	}

	public static long getLong(ConfigKeys key) {
		return pl.getConfig().getLong(key.KEY);
	}

	public static void reload() {
		pl.reloadConfig();
		CooldownUtil.refresh();
		NoFallDamageUtil.refresh();
	}

	public static void save() {
		pl.saveConfig();
	}
}

//pl.getConfig().addDefault(ConfigKeys.PREFIX.KEY, ConfigKeys.PREFIX.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.NAME.KEY, ConfigKeys.NAME.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.USE_AIR.KEY, ConfigKeys.USE_AIR.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.ALL_RODS.KEY, ConfigKeys.ALL_RODS.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.CRAFTING.KEY, ConfigKeys.CRAFTING.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.NO_FALL_DAMAGE.KEY, ConfigKeys.NO_FALL_DAMAGE.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.USE_ALL_WORLDS.KEY, ConfigKeys.USE_ALL_WORLDS.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.DESTROY_NO_MORE_UESE.KEY, ConfigKeys.DESTROY_NO_MORE_UESE.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.MAX_USES.KEY, ConfigKeys.MAX_USES.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.COOLDOWN.KEY, ConfigKeys.COOLDOWN.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.GRAVITY.KEY, ConfigKeys.GRAVITY.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SPEED_MULTIPLIER.KEY, ConfigKeys.SPEED_MULTIPLIER.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.FALL_DAMAGE_RM.KEY, ConfigKeys.FALL_DAMAGE_RM.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SOUND.KEY, ConfigKeys.SOUND.DEFAULT_VALUE);
//
//pl.getConfig().addDefault(ConfigKeys.RELOAD_MSG.KEY, ConfigKeys.RELOAD_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.NO_PERMS_MSG.KEY, ConfigKeys.NO_PERMS_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.UNKOWNCMD_MSG.KEY, ConfigKeys.UNKOWNCMD_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.ONLY_NUMBERS_MSG.KEY, ConfigKeys.ONLY_NUMBERS_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.USE_ALL_WORLDS_MSG.KEY, ConfigKeys.USE_ALL_WORLDS_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SET_SOUND_MSG.KEY, ConfigKeys.SET_SOUND_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SET_WORLD_MSG.KEY, ConfigKeys.SET_WORLD_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RM_WORLD_MSG.KEY, ConfigKeys.RM_WORLD_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.ALREADY_IN_LIST_MSG.KEY, ConfigKeys.ALREADY_IN_LIST_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.NOT_IN_LIST_MSG.KEY, ConfigKeys.NOT_IN_LIST_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.COOLDOWN_MSG.KEY, ConfigKeys.COOLDOWN_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SET_COOLDOWN_MSG.KEY, ConfigKeys.SET_COOLDOWN_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SET_USE_AIR_MSG.KEY, ConfigKeys.SET_USE_AIR_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SET_CRAFTING_MSG.KEY, ConfigKeys.SET_CRAFTING_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.SET_USE_AIR_MSG.KEY, ConfigKeys.SET_USES_MSG.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.USES_LEFT_MSG.KEY, ConfigKeys.USES_LEFT_MSG.DEFAULT_VALUE);
//
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT1.KEY, ConfigKeys.RECIEP_SLOT1.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT2.KEY, ConfigKeys.RECIEP_SLOT2.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT3.KEY, ConfigKeys.RECIEP_SLOT3.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT4.KEY, ConfigKeys.RECIEP_SLOT4.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT5.KEY, ConfigKeys.RECIEP_SLOT5.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT6.KEY, ConfigKeys.RECIEP_SLOT6.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT7.KEY, ConfigKeys.RECIEP_SLOT7.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT8.KEY, ConfigKeys.RECIEP_SLOT8.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.RECIEP_SLOT9.KEY, ConfigKeys.RECIEP_SLOT9.DEFAULT_VALUE);
//
//pl.getConfig().addDefault(ConfigKeys.LORE.KEY, ConfigKeys.LORE.DEFAULT_VALUE);
//pl.getConfig().addDefault(ConfigKeys.WORLDS.KEY, ConfigKeys.WORLDS.DEFAULT_VALUE);