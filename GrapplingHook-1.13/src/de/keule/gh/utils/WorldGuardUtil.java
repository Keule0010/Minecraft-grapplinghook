package de.keule.gh.utils;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import de.keule.gh.config.ConfigManager;
import de.keule.gh.main.GHPlugin;

public class WorldGuardUtil {
	private static final WorldGuard wg;
	private static StateFlag ghFlag;

	static {
		wg = WorldGuard.getInstance();
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

	public static boolean isPermitted(Player p) {
		if (!flagIsRegistered(p))
			return false;

		boolean allowed = false;
		for (ProtectedRegion region : getRegionSet(p))
			if (region.getFlag(ghFlag) == State.ALLOW)
				allowed = true;

		return allowed;
	}

	public static boolean isForbidden(Player p) {
		if (!flagIsRegistered(p))
			return true;

		boolean denied = false;
		for (ProtectedRegion region : getRegionSet(p))
			if (region.getFlag(ghFlag) == State.DENY)
				denied = true;

		return denied;
	}

	private static ApplicableRegionSet getRegionSet(Player p) {
		Location loc = BukkitAdapter.adapt(p.getLocation());
		RegionQuery query = wg.getPlatform().getRegionContainer().createQuery();
		return query.getApplicableRegions(loc);
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