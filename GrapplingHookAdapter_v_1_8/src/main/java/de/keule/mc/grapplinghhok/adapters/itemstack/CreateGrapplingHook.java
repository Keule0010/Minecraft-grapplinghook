package de.keule.mc.grapplinghhok.adapters.itemstack;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import de.keule.mc.grapplinghhok.adapters.enchantment.Glow;

public interface CreateGrapplingHook {

	public ItemStack create(Glow glowEnchantment, String displayName, List<String> lore, boolean glow, boolean unbreakable);

}