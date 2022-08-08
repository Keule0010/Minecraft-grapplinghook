package de.keule.mc.grapplinghook.adapters.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import de.keule.mc.grapplinghhok.adapters.recipe.GHShapedRecipe;

public class GHShapedRecipe_v_1_13 implements GHShapedRecipe {

	@Override
	public ShapedRecipe getRecipe(Plugin pl, ItemStack result, String name) {
		return new ShapedRecipe(new NamespacedKey(pl, name), result);
	}
}