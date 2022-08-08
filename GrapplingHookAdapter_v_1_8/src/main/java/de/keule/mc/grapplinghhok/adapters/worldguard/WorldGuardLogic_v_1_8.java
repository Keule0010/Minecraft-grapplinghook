package de.keule.mc.grapplinghhok.adapters.worldguard;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class WorldGuardLogic_v_1_8 extends WorldGuardLogic {
	private WorldGuardPlugin wg;

	public WorldGuardLogic_v_1_8(Plugin plugin) {
		super(plugin);
	}

	@Override
	protected ApplicableRegionSet getRegionSet(Player p) {
		LocalPlayer localPlayer = wg.wrapPlayer(p);
		Vector playerVector = localPlayer.getPosition();
		RegionManager regionManager = wg.getRegionManager(p.getWorld());
		return regionManager.getApplicableRegions(playerVector);
	}

	@Override
	public boolean init() {
		final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			pl.getLogger().severe("Couldn't register WorldGuard flags: Couldn't find WorldGuard plugin.");
			return (flagsRegistered = false);
		}

		wg = (WorldGuardPlugin) plugin;

		try {
			FlagRegistry registery = wg.getFlagRegistry();
			registery.register(getFlag());
			return (flagsRegistered = true);
		} catch (FlagConflictException e) {
			pl.getLogger().log(Level.SEVERE, "Couldn't register flag! Flag is already registered!", e);
		}

		return (flagsRegistered = false);
	}
}