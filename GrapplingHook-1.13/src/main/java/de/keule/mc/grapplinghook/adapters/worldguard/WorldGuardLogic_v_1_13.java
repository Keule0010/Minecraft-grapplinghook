package de.keule.mc.grapplinghook.adapters.worldguard;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import de.keule.mc.grapplinghhok.adapters.worldguard.WorldGuardLogic;

public class WorldGuardLogic_v_1_13 extends WorldGuardLogic {
	private WorldGuard wg;

	public WorldGuardLogic_v_1_13(Plugin plugin) {
		super(plugin);
	}

	@Override
	protected ApplicableRegionSet getRegionSet(Player p) {
		Location loc = BukkitAdapter.adapt(p.getLocation());
		RegionQuery query = wg.getPlatform().getRegionContainer().createQuery();
		return query.getApplicableRegions(loc);
	}

	@Override
	public boolean init() {
		final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			pl.getLogger().severe("Couldn't register WorldGuard flags: Couldn't find WorldGuard plugin.");
			return (flagsRegistered = false);
		}

		wg = WorldGuard.getInstance();

		try {
			FlagRegistry flagRegistery = WorldGuard.getInstance().getFlagRegistry();
			flagRegistery.register(getFlag());
			return (flagsRegistered = true);
		} catch (FlagConflictException e) {
			pl.getLogger().log(Level.SEVERE, "Couldn't register flag! Flag is already registered!", e);
		}
		return (flagsRegistered = false);
	}
}