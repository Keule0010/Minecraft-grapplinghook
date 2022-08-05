package de.keule.grapplinghook.worldGuard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import de.keule.grapplinghook.main.Main;

public class WorldGuard {
	public static WorldGuardPlugin worldGuard;
	public static StateFlag ghFlag;

	public WorldGuard() {
		worldGuard = getWorldGuard();
		FlagRegistry registery = worldGuard.getFlagRegistry();

		try {
			StateFlag flag = new StateFlag("gh-pl", true);
			registery.register(flag);
			ghFlag = flag;
			System.out.println("Flag added");
		} catch (FlagConflictException e) {
			e.printStackTrace();
			Main.error = true;
		}
	}

	private WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
			return null;
		return (WorldGuardPlugin) plugin;
	}
}
