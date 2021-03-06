package de.keule.grapplinghook.listener;

import java.io.IOException;
import java.util.List;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.keule.grapplinghook.cooldown.Cooldown;
import de.keule.grapplinghook.cooldown.NoFallDamage;
import de.keule.grapplinghook.main.Main;

public class Grapplinghook1_8_to_1_12 implements Listener {
	private NoFallDamage noFallDamage = NoFallDamage.getInstance();
	private Cooldown cooldown = new Cooldown();
	private static Main pl = Main.getPlugin();
	public static double multiplier;
	public static double g;

	static {
		updateVars();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onThrow(PlayerFishEvent event) {
		final Player player = event.getPlayer();
		final String pWorld = player.getLocation().getWorld().getName();

		if (pl.getConfig().getStringList("WorldList").contains(pWorld)
				|| pl.getConfig().getBoolean("Plugin.useInAllWorlds")) {
			if ((player.isOp() || player.hasPermission("grapplinghook.worlds.*")
					|| player.hasPermission("grapplinghook.world." + pWorld))
					&& (!player.hasPermission("grapplinghook.removeWorld." + pWorld) || player.isOp())) {

				boolean onFishingDisable = Main.getPlugin().getConfig().getBoolean("Plugin.onFishingDisable");
				if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && onFishingDisable) {
					onFishingDisable = false;
				} else {
					onFishingDisable = true;
				}
				if (event.getState() != PlayerFishEvent.State.FISHING && onFishingDisable) {
					if (!pl.getConfig().getBoolean("Plugin.allRods")) {
						String ghName = ChatColor
								.translateAlternateColorCodes('&', pl.getConfig().getString("Plugin.grapplingHookName"))
								.replace("%prefix%", Main.prefix);
						if (ghName.equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())) {
							if (cooldown.cooldown(player)) {
								fishEvent(player, event);
							}
						}
					} else {
						if (cooldown.cooldown(player)) {
							fishEvent(player, event);
						}
					}
				}
			}
		}
	}

	private void fishEvent(Player player, PlayerFishEvent event) {
		if (pl.getConfig().getInt("Plugin.maxUses") != 0) {
			ItemStack gh = player.getItemInHand();
			ItemMeta meta = gh.getItemMeta();
			if (meta.getLore() == null)
				return;
			final String firstLore = meta.getLore().get(0);
			final int usesLeft = Integer.parseInt(firstLore.substring(firstLore.lastIndexOf(":") + 1).replaceAll("[^\\d.]", ""));

			if (usesLeft == 0) {
				if(pl.getConfig().getBoolean("Plugin.destroyWhenNoMoreUses"))
					player.getInventory().remove(gh);
				return;
			}else {
				if (usesLeft - 1 == 0 && pl.getConfig().getBoolean("Plugin.destroyWhenNoMoreUses")) {
					player.getInventory().remove(gh);
				} else {
					ItemMeta newMeta = gh.getItemMeta();
					List<String> lore = newMeta.getLore();
					lore.set(0,
							ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.usesLeft"))
									+ ": " + (usesLeft - 1));
					newMeta.setLore(lore);
					gh.setItemMeta(newMeta);
				}
			}
		}

		FishHook h = event.getHook();
		boolean useAir = pl.getConfig().getBoolean("Plugin.useAir");
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
				if (Bukkit.getBukkitVersion().split("-")[0].contains("1.8")) {
					Main.getPlugin().getConfig().set("Sound.grapplinghookSound", "ENDERMAN_TELEPORT");
					player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0F, 100.0F);
					try {
						Main.getPlugin().getConfig().save(Main.configFile);
					} catch (IOException e1) {
						player.sendMessage(Main.prefix + "§4An error has occurred! Couldn't save config.yml file!");
					}
				} else {
					Main.getPlugin().getConfig().set("Sound.grapplinghookSound", "ENTITY_ENDERMEN_TELEPORT");
					try {
						Main.getPlugin().getConfig().save(Main.configFile);
					} catch (IOException e1) {
						player.sendMessage(Main.prefix + "§4An error has occurred! Couldn't save config.yml file!");
					}
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

	public static void updateVars() {
		g = Main.getPlugin().getConfig().getDouble("Plugin.gravity");
		multiplier = Main.getPlugin().getConfig().getDouble("Plugin.throw_speed_multiplier") * 2;
	}
}