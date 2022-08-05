package de.keule.mc.grapplinghook.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.keule.mc.grapplinghook.commands.GHCommand;
import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.config.Settings;
import de.keule.mc.grapplinghook.crafting.Glow;
import de.keule.mc.grapplinghook.events.GrapplingHookEvents;
import de.keule.mc.grapplinghook.events.PlayerEvents;
import de.keule.mc.grapplinghook.update.UpdateUtil;
import de.keule.mc.grapplinghook.utils.VersionUtil;
import de.keule.mc.grapplinghook.worldguard.WorldGuardLogic;

public class GHPlugin extends JavaPlugin {
	private static GHPlugin plugin;

	private boolean canRegisterRecipe = true;
	private WorldGuardLogic worldGuardLogic;

	@Override
	public void onLoad() {
		plugin = this;

		/* Register Glow-Enchantment */
		Glow.register(plugin);

		/* Register Flags */
		worldGuardLogic = new WorldGuardLogic(plugin);
		worldGuardLogic.registerFlags();
	}

	@Override
	public void onEnable() {
		/* Loading Configs */
		ConfigManager.init(plugin);

		/* Load Dependencies */
		worldGuardLogic.loadWorldGuard();

		/* Check Version */
		VersionUtil.versionCheck();

		/* Register Events */
		registerEvents();

		/* Register Commands */
		getCommand("grapplinghook").setExecutor(new GHCommand(plugin));

		/* Check for updates */
		UpdateUtil.checkForUpdate(plugin);

		/* Disable Register */
		canRegisterRecipe = false;
	}

	private void registerEvents() {
		PluginManager plManager = getServer().getPluginManager();

		plManager.registerEvents(new GrapplingHookEvents(), plugin);
		plManager.registerEvents(new PlayerEvents(), plugin);
	}

	@Override
	public void onDisable() {
	}

	public static void sendConsoleMesssage(String msg) {
		Bukkit.getServer().getConsoleSender()
				.sendMessage(Settings.getPrefix() + " " + ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static GHPlugin getInstance() {
		return plugin;
	}

	public boolean canRegisterRecipes() {
		return canRegisterRecipe;
	}

	public WorldGuardLogic getWorldGuardLogic() {
		return worldGuardLogic;
	}
}