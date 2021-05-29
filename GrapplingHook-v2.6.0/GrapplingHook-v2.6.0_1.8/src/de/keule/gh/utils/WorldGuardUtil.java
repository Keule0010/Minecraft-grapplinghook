package de.keule.gh.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.keule.gh.config.ConfigManager;
import de.keule.gh.main.GHPlugin;

public class WorldGuardUtil {
	private static final WorldGuardPlugin wg;
	private static StateFlag ghFlag;

	static {
		wg = getWorldGuard();
	}

	public static void registerFlag() {
		try {
			FlagRegistry registery = wg.getFlagRegistry();
			StateFlag flag = new StateFlag("gh-pl", true);
			registery.register(flag);
			ghFlag = flag;
			System.out.println("[GrapplingHook] Flag added");
		} catch (FlagConflictException e) {
			System.out.println("[GrapplingHook] §cThe WorldGuard flag couldn't registered properly!");
			e.printStackTrace();
		}
	}

	private static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
			return null;
		return (WorldGuardPlugin) plugin;
	}
	
	public static boolean isPermitted(Player p) {
		if (!flagIsRegistered(p))
			return false;

		boolean allowed = false;
		for (ProtectedRegion region : getRegionSte(p))
			if (region.getFlag(ghFlag) == State.ALLOW)
				allowed = true;

		return allowed;
	}

	public static boolean isForbidden(Player p) {
		if (!flagIsRegistered(p))
			return true;

		boolean denied = false;
		for (ProtectedRegion region : getRegionSte(p))
			if (region.getFlag(ghFlag) == State.DENY)
				denied = true;

		return denied;
	}

	private static ApplicableRegionSet getRegionSte(Player p) {
		LocalPlayer localPlayer = wg.wrapPlayer(p);
		Vector playerVector = localPlayer.getPosition();
		RegionManager regionManager = wg.getRegionManager(p.getWorld());
		return regionManager.getApplicableRegions(playerVector);
	}
	
	private static boolean flagIsRegistered(Player p) {
		if (!GHPlugin.worldGuardExists())
			return false;

		if (ghFlag == null) {
			if (p.isOp())
				p.sendMessage(ConfigManager.getPrefix() + "§4The WorldGuard flag couldn't registered properly!");
			else
				GHPlugin.sendConsoleMesssage("&4The WorldGuard flag couldn't registered properly!");
			return false;
		} else
			return true;
	}
}