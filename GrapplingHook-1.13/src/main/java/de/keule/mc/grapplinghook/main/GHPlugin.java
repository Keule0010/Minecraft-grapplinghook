package de.keule.mc.grapplinghook.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.keule.mc.grapplinghhok.adapters.worldguard.WorldGuardLogic;
import de.keule.mc.grapplinghook.commands.GHCommand;
import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.config.Settings;
import de.keule.mc.grapplinghook.events.GrapplingHookEvents;
import de.keule.mc.grapplinghook.events.PlayerEvents;
import de.keule.mc.grapplinghook.utils.TimeUtil;
import de.keule.mc.grapplinghook.utils.UpdateUtil;
import de.keule.mc.grapplinghook.version.VersionUtil;
import de.keule.mc.grapplinghook.worldguard.WorldGuardManager;

public class GHPlugin extends JavaPlugin {
	private static GHPlugin plugin;

	private boolean canRegisterRecipe = true;

	private WorldGuardLogic worldGuardLogic;

	@Override
	public void onLoad() {
		plugin = this;

		/* Check Version */
		VersionUtil.versionCheck(plugin);

		/* Register Enchantment */
		VersionUtil.getGlow().register(plugin);

		/* Register Flags */
		if (WorldGuardManager.isWorldGuardEnabled())
			VersionUtil.getWorldGuardLogic(this).init();
	}

	@Override
	public void onEnable() {
		final long start = System.currentTimeMillis();
		println("&8+-----------------------------------------------+");
		/* Loading Configs */
		ConfigManager.init(plugin);

		/* Register Events */
		registerEvents();

		/* Register Commands */
		registerCommands();

		/* Check for updates */
		UpdateUtil.checkForUpdate(plugin);

		/* Disable Register */
		canRegisterRecipe = false;

		println("  &8Server Version: &a" + VersionUtil.getServerVersion() + "&8 -> API-Version: &a"
				+ (VersionUtil.isNewApi() ? "1.13 and above" : "1.8 - 1.12")
				+ (VersionUtil.isUnsupported() ? " &8(&cThe plugin was not tested on this server version!&8)" : ""));
		println("  &8WorldGuard " + (WorldGuardManager.isWorldGuardEnabled() ? "&2loaded" : "&cnot loaded"));
		println("  &8Grappling hooks loaded: &c" + GrapplingHook.getGrapplingHooks().size());
		println("&8+---------------[ &5By Keule2 &8(&a" + TimeUtil.formatMillis(System.currentTimeMillis() - start)
				+ "&8) ]-------------+");
	}

	private void registerCommands() {
		getCommand("grapplinghook").setExecutor(new GHCommand(plugin));
	}

	private void registerEvents() {
		PluginManager plManager = getServer().getPluginManager();

		plManager.registerEvents(new GrapplingHookEvents(), plugin);
		plManager.registerEvents(new PlayerEvents(), plugin);
	}

	@Override
	public void onDisable() {
	}

	public static void println(String msg) {
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