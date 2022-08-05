package de.keule.mc.grapplinghook.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.main.GHPlugin;
import de.keule.mc.grapplinghook.main.GrapplingHook;

public class GHRecipe {
	private ShapedRecipe ghRecipe;
	private GrapplingHook gh;

	public GHRecipe(GrapplingHook gh) {
		this.gh = gh;

		init(GHPlugin.getInstance());
	}

	private void init(GHPlugin pl) {
		ghRecipe = new ShapedRecipe(new NamespacedKey(pl, gh.getConfigPath()), gh.getGrapplingHook());
		ghRecipe.shape("123", "456", "789");

		for (int i = 1; i < 10; i++) {
			final Material mat = ConfigManager.getGrapplingHookConfig()
					.getMaterial(gh.getConfigPath() + ".recipe.slot" + i, Material.AIR);
			if (mat != null)
				ghRecipe.setIngredient(Character.forDigit(i, 10), mat);
		}

		if (pl.canRegisterRecipes()) {
			pl.getServer().addRecipe(ghRecipe);
		}
	}

	public boolean isEqual(Recipe recipe) {
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