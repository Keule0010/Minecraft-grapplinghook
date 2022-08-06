package de.keule.mc.grapplinghhok.adapters.recipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class GHShapedRecipe_v_1_8 implements GHShapedRecipe {

	@Override
	public ShapedRecipe getRecipe(Plugin pl, ItemStack result, String name) {
		return new ShapedRecipe(result);
	}
}