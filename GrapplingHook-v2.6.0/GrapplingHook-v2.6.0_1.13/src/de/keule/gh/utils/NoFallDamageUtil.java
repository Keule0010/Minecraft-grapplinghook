package de.keule.gh.utils;

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

import de.keule.gh.config.ConfigKeys;
import de.keule.gh.config.ConfigManager;
import de.keule.gh.main.GHPlugin;

public class NoFallDamageUtil {
	private static Map<UUID, Integer> noFallDamage;
	private static boolean noFallDamageBo;
	private static long latestRemove;

	static {
		noFallDamage = new HashMap<UUID, Integer>();
		refresh();
	}
	
	public static void addPlayer(Player p) {
		if (noFallDamageBo) {
			int playerRandomSeed = new Random().nextInt();
			noFallDamage.put(p.getUniqueId(), playerRandomSeed);

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GHPlugin.getInstance(), () -> {
				if (noFallDamage.containsKey(p.getUniqueId())) {
					if (noFallDamage.get(p.getUniqueId()) == playerRandomSeed) {
						Location loc = p.getLocation();
						Block b1 = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
						Block b2 = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ());

						if(b1.getType() == Material.AIR && p.getGameMode() != GameMode.CREATIVE) {
							if(b2.getType() != Material.AIR && p.isOnGround()) 
								noFallDamage.remove(p.getUniqueId());
						}else 
							noFallDamage.remove(p.getUniqueId());
					}
				}
			}, latestRemove);
		}
	}

	public static void removePlayer(Player p) {
		noFallDamage.remove(p.getUniqueId());
	}

	public static boolean containsPlayer(Player p) {
		return noFallDamage.containsKey(p.getUniqueId());
	}

	public static void refresh() {
		noFallDamage.clear();
		noFallDamageBo = ConfigManager.getBoolean(ConfigKeys.NO_FALL_DAMAGE); 
		latestRemove = (long) ConfigManager.getDouble(ConfigKeys.FALL_DAMAGE_RM) * 20;
		latestRemove = latestRemove <= 10? 50: latestRemove;
	}
}
