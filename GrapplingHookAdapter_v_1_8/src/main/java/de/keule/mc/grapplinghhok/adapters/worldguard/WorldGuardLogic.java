package de.keule.mc.grapplinghhok.adapters.worldguard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public abstract class WorldGuardLogic {
	private static final StateFlag GH_FLAG = new StateFlag("gh-pl", false);
	protected boolean flagsRegistered = false;
	protected Plugin pl;

	public WorldGuardLogic(Plugin plugin) {
		pl = plugin;
	}

	/**
	 * Checks whether or not a player is allowed to use the grappling hook in the
	 * region they are. Returns true when {@link WorldGuard} is disabled or the
	 * flags couldn't be registered.
	 */
	public boolean isPermitted(Player p) {
		if (!flagsRegistered())
			return true;

		boolean allowed = false;
		for (ProtectedRegion region : getRegionSet(p))
			if (region.getFlag(getFlag()) == State.ALLOW)
				allowed = true;

		return allowed;
	}

	/**
	 * Checks whether or not a player is not allowed to use the grappling hook in
	 * the region they are. Returns false when {@link WorldGuard} is disabled or the
	 * flags couldn't be registered.
	 */
	public boolean isForbidden(Player p) {
		if (!flagsRegistered())
			return false;

		boolean denied = false;
		for (ProtectedRegion region : getRegionSet(p))
			if (region.getFlag(getFlag()) == State.DENY)
				denied = true;

		return denied;
	}

	public StateFlag getFlag() {
		return (StateFlag) GH_FLAG;
	}

	protected boolean flagsRegistered() {
		return flagsRegistered;
	}

	protected abstract ApplicableRegionSet getRegionSet(Player p);

	/**
	 * @implNote This method has to be called in the {@code JavaPlugin#onLoad()}
	 *           method. The method should load the WorldGuardPlugin and register
	 *           flags.
	 */
	public abstract boolean init();

}