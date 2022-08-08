package de.keule.mc.grapplinghhok.adapters.itemstack;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.keule.mc.grapplinghhok.adapters.enchantment.Glow;

public class CreateGrapplingHook_v_1_8 implements CreateGrapplingHook {

	@Override
	public ItemStack create(Glow glowEnchantment, String displayName, List<String> lore, boolean glow,
			boolean unbreakable) {
		ItemStack gh = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta = gh.getItemMeta();

		meta.setDisplayName(displayName);
		meta.setLore(lore);

		if (glow)
			if (glowEnchantment == null || glowEnchantment.get() == null)
				meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
			else
				meta.addEnchant(glowEnchantment.get(), 1, true);

		if (unbreakable) {
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			meta.spigot().setUnbreakable(true);
		}

		gh.setItemMeta(meta);
		return gh;
	}

}