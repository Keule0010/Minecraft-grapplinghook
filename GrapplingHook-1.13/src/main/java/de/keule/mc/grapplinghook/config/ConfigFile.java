package de.keule.mc.grapplinghook.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class ConfigFile {
	private YamlConfiguration config;
	private File configFile;
	private Plugin pl;

	public ConfigFile(Plugin pl, String fileName) {
		this.pl = pl;
		if (!pl.getDataFolder().exists())
			pl.getDataFolder().mkdirs();

		this.configFile = new File(pl.getDataFolder(), fileName);

		if (!configFile.exists()) {
			try (InputStream defValues = ConfigFile.class.getResourceAsStream("/" + fileName);
					FileOutputStream fo = new FileOutputStream(configFile)) {

				byte readBytes[] = new byte[1024 * 3];
				int read;
				while ((read = defValues.read(readBytes)) != -1) {
					fo.write(readBytes, 0, read);
				}
			} catch (IOException e) {
				pl.getLogger().log(Level.SEVERE, "Couldn't save default config: " + fileName + "!", e);
			}
		}

		config = YamlConfiguration.loadConfiguration(configFile);
		// TODO If new config version -> add new values to existing config
	}

	/* Save/Reload */
	public boolean saveAndReload() {
		boolean save = save();
		boolean reload = reload();

		return save && reload;
	}

	public boolean save() {
		if (config == null || configFile == null)
			return false;

		try {
			config.save(configFile);
			return true;
		} catch (IOException e) {
			pl.getLogger().log(Level.WARNING, "Couldn't save config: " + configFile.getName() + "!", e);
		}
		return false;
	}

	public boolean reload() {
		if (config == null || configFile == null)
			return false;

		try {
			config.load(configFile);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			pl.getLogger().log(Level.WARNING, "Couldn't reload config: " + configFile.getName() + "!", e);
		}
		return false;
	}

	/* Remove */
	public void remove(String route) {
		config.set(route, null);
	}

	/* Setters */
	public boolean set(ConfigKey key, Object value) {
		try {
			config.set(key.PATH, value);
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean set(String key, Object value) {
		try {
			config.set(key, value);
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* Getters */
	public YamlConfiguration getYamlConfiguration() {
		return config;
	}

	public File getConfigFile() {
		return configFile;
	}

	public boolean pathExists(String path) {
		return config.contains(path);
	}

	public boolean pathExistsIgnoreDefaults(String path) {
		return config.contains(path, true);
	}

	public String getString(ConfigKey key) {
		return config.getString(key.PATH);
	}

	public String getString(String key) {
		return config.getString(key);
	}

	public String getString(String key, String def) {
		return config.getString(key, def);
	}

	public Material getMaterial(ConfigKey key) {
		return getMaterial(key.PATH, null);
	}

	public Material getMaterial(ConfigKey key, Material def) {
		return getMaterial(key.PATH, def);
	}

	public Material getMaterial(String route, Material def) {
		String mat = config.getString(route);
		if (mat == null || mat.isEmpty())
			return def;

		mat = mat.toUpperCase();
		Material matt = Material.getMaterial(mat);
		if (matt == null)
			return def;
		return matt;
	}

	public Sound getSound(ConfigKey key) {
		return getSound(key.PATH, null);
	}

	public Sound getSound(ConfigKey key, Sound def) {
		return getSound(key.PATH, def);
	}

	public Sound getSound(String route, Sound def) {
		String sound = config.getString(route);
		if (sound == null || sound.isEmpty())
			return def;
		sound = sound.toUpperCase();
		try {
			return Sound.valueOf(sound);
		} catch (IllegalArgumentException e) {
		}
		return def;
	}

	public Effect getEffect(ConfigKey key) {
		return getEffect(key.PATH, null);
	}

	public Effect getEffect(ConfigKey key, Effect def) {
		return getEffect(key.PATH, def);
	}

	public Effect getEffect(String route, Effect def) {
		String effect = config.getString(route);
		if (effect == null || effect.isEmpty())
			return def;

		effect = effect.toUpperCase();
		try {
			return Effect.valueOf(effect);
		} catch (IllegalArgumentException e) {
		}
		return def;
	}

	public GameMode getGameMode(ConfigKey key) {
		return getGameMode(key.PATH, null);
	}

	public GameMode getGameMode(ConfigKey key, GameMode def) {
		return getGameMode(key.PATH, def);
	}

	public GameMode getGameMode(String route, GameMode def) {
		String gm = config.getString(route);
		if (gm == null || gm.isEmpty())
			return def;
		gm = gm.toUpperCase();
		try {
			return GameMode.valueOf(gm);
		} catch (IllegalArgumentException e) {
		}
		return def;
	}

	/**
	 * The world has to be saved as a {@link UUID} ({@code World#getUID()}).
	 * 
	 * @return A {@link World} object.
	 */
	public World getWorld(ConfigKey key) {
		return Bukkit.getWorld(UUID.fromString(getString(key)));
	}

	/**
	 * @return A string and already replaces the prefix and {@link ChatColor}s
	 */
	public String getMessage(ConfigKey key) {
		return getTranslatedString(key.PATH);
	}

	/**
	 * @return A string and already replaces the prefix and {@link ChatColor}s
	 */
	public String getTranslatedString(String route) {
		return getTranslatedString(route, "");
	}

	public String getTranslatedString(String route, String def) {
		final String str = config.getString(route, def);
		if (str == null)
			return def;
		return ChatColor.translateAlternateColorCodes('&', str.replace(ConfVars.PREFIX, Settings.getPrefix()));
	}

	public boolean getBlooean(ConfigKey key) {
		return getBlooean(key.PATH);
	}

	public boolean getBlooean(String key) {
		return config.getBoolean(key);
	}

	public boolean getBlooean(String key, boolean def) {
		return config.getBoolean(key, def);
	}

	public int getInt(ConfigKey key) {
		return config.getInt(key.PATH);
	}

	public int getInt(String key) {
		return config.getInt(key);
	}

	public int getInt(ConfigKey key, int def) {
		return config.getInt(key.PATH, def);
	}

	public int getInt(String key, int def) {
		return config.getInt(key, def);
	}

	public long getLong(ConfigKey key) {
		return config.getLong(key.PATH);
	}

	public Double getDouble(String key, double def) {
		return config.getDouble(key, def);
	}

	public Double getDouble(String key) {
		return getDouble(key, 0);
	}

	public Double getDouble(ConfigKey key) {
		return getDouble(key.PATH, 0);
	}

	public List<?> getList(ConfigKey key) {
		return config.getList(key.PATH);
	}

	public List<String> getStringList(ConfigKey key) {
		return getStringList(key.PATH);
	}

	public List<String> getStringList(String key) {
		return config.getStringList(key);
	}

	public ConfigurationSection getSection(ConfigKey route) {
		return getSection(route.PATH);
	}

	public ConfigurationSection getSection(String route) {
		return config.getConfigurationSection(route);
	}

	public Object get(String route, Object def) {
		return config.get(route, def);
	}

	public Set<String> getKeys() {
		return config.getKeys(false);
	}
}