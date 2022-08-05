package de.keule.gh.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.keule.gh.config.ConfigManager;
import de.keule.gh.listener.GHListener;
import de.keule.gh.listener.PlayerListener;
import de.keule.gh.utils.RecipeUtil;
import de.keule.gh.utils.UpdateUtil;
import de.keule.gh.utils.VersionUtil;
import de.keule.gh.utils.WorldGuardUtil;

public class GHPlugin extends JavaPlugin {
	private static boolean worldguard;
	private static GHPlugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		ConfigManager.init();
		UpdateUtil.checkForUpdate();
		VersionUtil.versionCheck();

		Glow.init();
		RecipeUtil.register();
		getCommand("grapplinghook").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new GHListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	@Override
	public void onLoad() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		worldguard = plugin != null && (plugin instanceof WorldGuardPlugin);
		if (worldGuardExists())
			WorldGuardUtil.registerFlag();
	}

	@Override
	public void onDisable() {
		sendConsoleMesssage("Disabeled");
	}

	public static void sendConsoleMesssage(String msg) {
		Bukkit.getConsoleSender()
				.sendMessage(ConfigManager.getPrefix() + " " + ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static GHPlugin getInstance() {
		return instance;
	}

	public static boolean worldGuardExists() {
		return worldguard;
	}
}