package de.keule.mc.grapplinghook.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.main.GHPlugin;
import de.keule.mc.grapplinghook.main.GrapplingHook;
import de.keule.mc.grapplinghook.version.VersionUtil;

public class GHRecipe {
	private ShapedRecipe ghRecipe;
	private GrapplingHook gh;

	public GHRecipe(GrapplingHook gh) {
		this.gh = gh;

		if (gh != null)
			init(GHPlugin.getInstance());
	}

	public void init(GHPlugin pl) {
		ghRecipe = VersionUtil.getGhShapedRecipe().getRecipe(pl, gh.getGrapplingHook(), gh.getName());
		ghRecipe.shape("123", "456", "789");

		for (int i = 1; i < 10; i++) {
			final Material mat = ConfigManager.getGrapplingHookConfig()
					.getMaterial(gh.getName() + ".recipe.slot" + i, null);
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

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("{");
		ghRecipe.getIngredientMap().forEach((k, v) -> str.append(k + "=" + (v == null ? "AIR" : v.getType()) + ", "));
		str.append("}");
		return str.toString();
	}
}