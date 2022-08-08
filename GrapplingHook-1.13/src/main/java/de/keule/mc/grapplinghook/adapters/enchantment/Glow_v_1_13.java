package de.keule.mc.grapplinghook.adapters.enchantment;

import java.lang.reflect.Field;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.keule.mc.grapplinghhok.adapters.enchantment.Glow;

public class Glow_v_1_13 implements Glow {
	private static boolean isRegistered = false;
	private static Enchantment glow;

	@Override
	public void register(Plugin pl) {
		if (isRegistered)
			return;

		isRegistered = true;
		glow = Enchantment.getByKey(new NamespacedKey(pl, "grappling_hook_glow"));
		if (glow != null)
			return;

		Field f;
		try {
			f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			return;
		}

		glow = new Enchantment(new NamespacedKey(pl, "grappling_hook_glow")) {
			public boolean isTreasure() {
				return false;
			}

			public boolean isCursed() {
				return false;
			}

			public int getStartLevel() {
				return 0;
			}

			public String getName() {
				return "glow";
			}

			public int getMaxLevel() {
				return 0;
			}

			public EnchantmentTarget getItemTarget() {
				return null;
			}

			public boolean conflictsWith(Enchantment arg0) {
				return false;
			}

			public boolean canEnchantItem(ItemStack arg0) {
				return false;
			}
		};

		Enchantment.registerEnchantment(glow);
		f.setAccessible(false);
	}

	@Override
	public Enchantment get() {
		return glow;
	}
}