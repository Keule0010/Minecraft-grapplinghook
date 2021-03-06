package de.keule.grapplinghook.cooldown;

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

import de.keule.grapplinghook.main.Main;

public class NoFallDamage {
	private Map<UUID, Integer> noFallDamage = new HashMap<UUID, Integer>();
	public boolean noFallDamageBo = Main.getPlugin().getConfig().getBoolean("Plugin.noFallDamage");
	public long latestRemove = (long) (Main.getPlugin().getConfig().getDouble("Plugin.fallDamageRemove") * 20);
	private static NoFallDamage noFallObj;

	static {
		noFallObj = new NoFallDamage();
	}
	
	private NoFallDamage() {
		latestRemove = latestRemove <= 10? 25: latestRemove;
	}
	
	@SuppressWarnings("deprecation")
	public void addPlayer(Player p) {
		if (noFallDamageBo) {
			int playerRandomSeed = new Random().nextInt();
			noFallDamage.put(p.getUniqueId(), playerRandomSeed);

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
				if (noFallDamage.containsKey(p.getUniqueId())) {
					if (noFallDamage.get(p.getUniqueId()) == playerRandomSeed) {
						Location loc = p.getLocation();
						Block b1 = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
						Block b2 = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ());

						if(b1.getType() == Material.AIR && p.getGameMode() != GameMode.CREATIVE) {
							if(b2.getType() != Material.AIR && p.isOnGround()) {
								noFallDamage.remove(p.getUniqueId());
							}
						}else 
							noFallDamage.remove(p.getUniqueId());
					}
				}
			}, latestRemove);
		}
	}

	public void removePlayer(UUID pUUID) {
		noFallDamage.remove(pUUID);
	}

	public boolean containsPlayer(UUID pUUID) {
		return noFallDamage.containsKey(pUUID);
	}

	public void refresh() {
		noFallDamage.clear();
		noFallDamageBo = Main.getPlugin().getConfig().getBoolean("Plugin.noFallDamage");
		latestRemove = Main.getPlugin().getConfig().getLong("Plugin.fallDamageRemove") * 20;
		latestRemove = latestRemove <= 10? 50: latestRemove;
	}

	public static NoFallDamage getInstance() {
		return noFallObj;
	}
}