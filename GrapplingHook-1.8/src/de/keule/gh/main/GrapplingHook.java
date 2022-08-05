package de.keule.gh.main;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.keule.gh.config.ConfigKeys;
import de.keule.gh.config.ConfigManager;
import de.keule.gh.utils.VersionUtil;

public class GrapplingHook {

	public static boolean isEqual(ItemStack is) {
		if (is == null)
			return false;

		if (!is.getType().equals(Material.FISHING_ROD))
			return false;

		if(is.getItemMeta().getDisplayName() == null)
			return false;
		
		if (is.getItemMeta().getDisplayName().equals(ConfigManager.getMessage(ConfigKeys.NAME)))
			return true;

		return false;
	}

	public static ItemStack getGrapplingHook() {
		ItemStack is = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta = is.getItemMeta();

		meta.setDisplayName(ConfigManager.getMessage(ConfigKeys.NAME));
		meta.setLore(ConfigManager.getLore());

		if (ConfigManager.getBoolean(ConfigKeys.ENCHANTED))
			if (Glow.get() == null || VersionUtil.isAbove11())
				meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
			else
				meta.addEnchant(Glow.get(), 0, true);

		if (ConfigManager.getBoolean(ConfigKeys.UNBREAKABLE)) {
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			meta.spigot().setUnbreakable(true);
		}

		is.setItemMeta(meta);
		return is;
	}

	public static boolean updateLore(Player p) {
		if (ConfigManager.getBoolean(ConfigKeys.ALL_RODS))
			return true;

		ItemStack is = p.getInventory().getItemInHand();
		if (!isEqual(is))
			return false;

		ItemMeta meta = is.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null || lore.isEmpty())
			return false;

		final String l = lore.get(lore.size() - 1).replaceAll("§", "").replaceAll("\\s+", "");
		if (l.equals("a"))
			return true;
		int uses = 0;
		try {
			uses = Integer.parseInt(l);
		} catch (NumberFormatException e) {
			GHPlugin.sendConsoleMesssage("&cThe lore of a grappling hook from player: " + p.getName()
					+ " is corupted! When you have maxUses enabled it won't work anymore!&7 You can try fix it with /gh setPlayerUses [Name] [Uses]");
			return false;
		}

		if (uses == 0) {
			destroyHook(p, is);
			return false;
		}

		if (uses == 1) 
			destroyHook(p, is);

		meta.setLore(ConfigManager.getLore(uses - 1));
		is.setItemMeta(meta);
		return true;
	}

	private static void destroyHook(Player p, ItemStack is) {
		if (ConfigManager.getBoolean(ConfigKeys.DESTROY_NO_MORE_UESE)) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GHPlugin.getInstance(), () -> {
				p.getInventory().remove(is);
				try {
					p.playSound(p.getLocation(), Sound.valueOf(ConfigManager.getString(ConfigKeys.DESTROY_SOUND)), 10,
							100);
				} catch (Exception e) {
					if (p.isOp())
						GHPlugin.sendConsoleMesssage("The break sound is misspeled!");
				}
			}, ConfigManager.getLong(ConfigKeys.DESTROY_DELAY));
		}
	}
}
