package de.keule.grapplinghook.listener;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import de.keule.grapplinghook.cooldown.Cooldown;
import de.keule.grapplinghook.cooldown.NoFallDamage;
import de.keule.grapplinghook.main.Main;

public class Grapplinghook1_13_above implements Listener {
	private Cooldown cooldown = new Cooldown();
	private NoFallDamage noFallDamage = NoFallDamage.getInstance();
	public static double multiplier = Main.getPlugin().getConfig().getDouble("Plugin.throw_speed_multiplier") * 2;
	public static double g = Main.getPlugin().getConfig().getDouble("Plugin.gravity");
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onThrow(PlayerFishEvent event) {
		Player player = event.getPlayer();
		String pWorld = player.getLocation().getWorld().getName();
		
		if (Main.getPlugin().getConfig().getStringList("WorldList").contains(pWorld)
				|| Main.getPlugin().getConfig().getBoolean("Plugin.useInAllWorlds")) {
			if ((player.isOp() || player.hasPermission("grapplinghook.worlds.*")
					|| player.hasPermission("grapplinghook.world." + pWorld))
					&& (!player.hasPermission("grapplinghook.removeWorld." + pWorld) || player.isOp())) {
				
				if (event.getState() != PlayerFishEvent.State.FISHING) {
					if (!Main.getPlugin().getConfig().getBoolean("Plugin.allRods")) {
						String ghName = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Plugin.grapplingHookName")).replace("%prefix%", Main.prefix);
						if(ghName.equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())) {
							if (cooldown.cooldown(player)) {
								fishEvent(player, event);
							}
						}
					}else {
						if (cooldown.cooldown(player)) {
							fishEvent(player, event);
						}
					}
				}
			}
		}
	}

	public void fishEvent(Player player, PlayerFishEvent event) {
		FishHook h = event.getHook();
		boolean useAir = Main.getPlugin().getConfig().getBoolean("Plugin.useAir");
		if (Bukkit.getWorld(event.getPlayer().getWorld().getName())
				.getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1, h.getLocation().getBlockZ())
				.getType() != Material.AIR || useAir) {
			Location lc = player.getLocation();
			Location to = event.getHook().getLocation();
			
			try {
				player.playSound(player.getLocation(),
						Sound.valueOf(Main.getPlugin().getConfig().getString("Sound.grapplinghookSound").toUpperCase()),
						10.0F, 100.0F);
			} catch (Exception e) {
					Main.getPlugin().getConfig().set("Sound.grapplinghookSound", "ENTITY_ENDERMAN_TELEPORT");
					player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10.0F, 100.0F);
					try {
						Main.getPlugin().getConfig().save(Main.configFile);
					} catch (IOException e1) {
						Main.error = true;
						player.sendMessage(Main.prefix + "§4An error has occurred! Couldn't save config.yml file!");
					}
			}
			
			double d = to.distance(lc);
			double v_x = (1.0D + multiplier * d) * (to.getX() - lc.getX()) / d;
			double v_y = (1.0D + 0.3D * d) * (to.getY() - lc.getY()) / d - 0.5D * g * d;
			double v_z = (1.0D + multiplier * d) * (to.getZ() - lc.getZ()) / d;
			Vector v = player.getVelocity();
			v.setX(v_x);
			v.setY(v_y);
			v.setZ(v_z);
			player.setVelocity(v);
			noFallDamage.addPlayer(player);
		}
	}
}