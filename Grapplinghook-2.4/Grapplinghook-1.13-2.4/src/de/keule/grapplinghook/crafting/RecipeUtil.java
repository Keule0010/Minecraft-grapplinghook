package de.keule.grapplinghook.crafting;

public class RecipeUtil {

	@SuppressWarnings("deprecation")
	public static boolean areEqual(org.bukkit.inventory.Recipe recipe1, org.bukkit.inventory.Recipe recipe2) {
		if (recipe1 == recipe2) {
			return true;
		}

		if (recipe1 == null || recipe2 == null) {
			return false;
		}

		if (recipe1.getResult().getItemMeta().getDisplayName().equals(recipe2.getResult().getItemMeta().getDisplayName()) && recipe1.getResult().getData().getItemType().equals(recipe2.getResult().getData().getItemType())) {
			return true;
		}
		
		return false;
	}
}
