package de.keule.mc.grapplinghook.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.keule.mc.grapplinghook.config.Settings;
import de.keule.mc.grapplinghook.main.GHPlugin;

public class NoFallDamage {
	private static final Map<UUID, Integer> noFallDamage = new HashMap<>();

	public static void addPlayer(Player p) {
		int playerRandomSeed = new Random().nextInt();
		noFallDamage.put(p.getUniqueId(), playerRandomSeed);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GHPlugin.getInstance(), () -> {
			if (noFallDamage.containsKey(p.getUniqueId())) {
				if (noFallDamage.get(p.getUniqueId()) == playerRandomSeed) {
					Location loc = p.getLocation();
					Block b1 = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
					Block b2 = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ());

					if (b1.getType() == Material.AIR && p.getGameMode() != GameMode.CREATIVE) {
						if (b2.getType() != Material.AIR)
							noFallDamage.remove(p.getUniqueId());
					} else
						noFallDamage.remove(p.getUniqueId());
				}
			}
		}, Settings.getFallDamageRemove());
	}

	public static void removePlayer(Player p) {
		noFallDamage.remove(p.getUniqueId());
	}

	public static boolean containsPlayer(Player p) {
		return noFallDamage.containsKey(p.getUniqueId());
	}

	public static void cleareCache() {
		noFallDamage.clear();
	}
}
