package de.keule.mc.grapplinghook.worldguard;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import de.keule.mc.grapplinghook.main.GHPlugin;

public class WorldGuardLogic {
	private boolean flagsRegistered;
	private WorldGuard wg;
	private GHPlugin pl;

	public WorldGuardLogic(GHPlugin plugin) {
		pl = plugin;
	}

	/**
	 * Checks whether or not a player is allowed to use the grappling hook in the
	 * region they are. Returns true when {@link WorldGuard} is disabled or the
	 * flags couldn't be registered.
	 */
	public boolean isPermitted(Player p) {
		if (!isEnabled() || !flagIsRegistered(p))
			return true;

		boolean allowed = false;
		for (ProtectedRegion region : getRegionSet(p))
			if (region.getFlag(CustomFlags.ghFlag) == State.ALLOW)
				allowed = true;

		return allowed;
	}

	/**
	 * Checks whether or not a player is not allowed to use the grappling hook in
	 * the region they are. Returns false when {@link WorldGuard} is disabled or the
	 * flags couldn't be registered.
	 */
	public boolean isForbidden(Player p) {
		if (!isEnabled() || !flagIsRegistered(p))
			return false;

		boolean denied = false;
		for (ProtectedRegion region : getRegionSet(p))
			if (region.getFlag(CustomFlags.ghFlag) == State.DENY)
				denied = true;

		return denied;
	}

	private ApplicableRegionSet getRegionSet(Player p) {
		Location loc = BukkitAdapter.adapt(p.getLocation());
		RegionQuery query = wg.getPlatform().getRegionContainer().createQuery();
		return query.getApplicableRegions(loc);
	}

	private boolean flagIsRegistered(Player p) {
		if (wg == null)
			return false;

		return flagsRegistered;
	}

	public boolean registerFlags() {
		if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			pl.getLogger()
					.severe("Couldn't regitser WorldGuard flags: Couldn't find WorldGuard plugin! Is it installed?");
			return (flagsRegistered = false);
		}

		try {
			FlagRegistry flagRegistery = WorldGuard.getInstance().getFlagRegistry();
			flagRegistery.register(CustomFlags.ghFlag);
			return (flagsRegistered = true);
		} catch (FlagConflictException e) {
			pl.getLogger().log(Level.SEVERE, "Couldn't register flag! Flag is allready registered!", e);
		}
		return (flagsRegistered = false);
	}

	public void loadWorldGuard() {
		if (!flagsRegistered)
			return;

		if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			pl.getLogger().severe("Couldn't find WorldGuard plugin! Is it installed?");
			return;
		}
		wg = WorldGuard.getInstance();
	}

	public boolean isEnabled() {
		return wg != null;
	}
}