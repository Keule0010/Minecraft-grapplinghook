package de.keule.mc.grapplinghhok.adapters.recipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public interface GHShapedRecipe {

	public ShapedRecipe getRecipe(Plugin pl, ItemStack result, String name);

}