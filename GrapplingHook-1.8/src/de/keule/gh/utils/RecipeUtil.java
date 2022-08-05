package de.keule.gh.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import de.keule.gh.main.GHPlugin;
import de.keule.gh.main.GrapplingHook;

public class RecipeUtil {
	private static ShapedRecipe ghRecipe;

	public static void register() {
		if (ghRecipe != null)
			return;
		Plugin pl = GHPlugin.getInstance();

		ghRecipe = new ShapedRecipe(GrapplingHook.getGrapplingHook());
		ghRecipe.shape("123", "456", "789");

		for (int i = 1; i < 10; i++) {
			final String material = pl.getConfig().getString("Recipe.slot" + i);
			if (!material.isEmpty())
				if (Material.getMaterial(material.toUpperCase()) == null)
					GHPlugin.sendConsoleMesssage("&c It looks like material solt: " + i + " is misspelled!");
				else
					ghRecipe.setIngredient(Character.forDigit(i, 10), Material.getMaterial(material.toUpperCase()));
		}

		pl.getServer().addRecipe(ghRecipe);
	}

	public static boolean isEqual(Recipe recipe) {
		if (ghRecipe == null || recipe == null)
			return false;

		if (ghRecipe == recipe)
			return true;

		if (ghRecipe.getResult().getItemMeta().getDisplayName()
				.equals(recipe.getResult().getItemMeta().getDisplayName())
				&& ghRecipe.getResult().getType().equals(recipe.getResult().getType()))
			return true;

		return false;
	}
}
