package de.keule.mc.grapplinghhok.adapters.enchantment;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Glow_v_1_8 implements Glow {
	private static final String NAME = "grappling_hook_glow";
	private static final int ID = 49875478;

	private boolean isRegistered = false;
	private Enchantment glow;

	@Override
	public void register(Plugin pl) {
		if (isRegistered)
			return;

		isRegistered = true;

		glow = Enchantment.getByName(NAME);
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

		glow = new Enchantment(ID) {
			public int getStartLevel() {
				return 1;
			}

			public String getName() {
				return NAME;
			}

			public int getMaxLevel() {
				return 1;
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