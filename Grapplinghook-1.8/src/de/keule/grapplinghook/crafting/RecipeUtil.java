package de.keule.grapplinghook.crafting;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeUtil {

	public static boolean areEqual(Recipe recipe1, Recipe recipe2) {
		if (recipe1 == recipe2) {
			return true;
		}

		if (recipe1 == null || recipe2 == null) {
			return false;
		}

		if (!recipe1.getResult().equals(recipe2.getResult())) {
			return false;
		}

		return match(recipe1, recipe2);
	}

	private static boolean match(Recipe recipe1, Recipe recipe2) {
		if (recipe1 instanceof ShapedRecipe) {
			if (recipe2 instanceof ShapedRecipe == false) {
				return false;
			}

			ShapedRecipe r1 = (ShapedRecipe) recipe1;
			ShapedRecipe r2 = (ShapedRecipe) recipe2;

			ItemStack[] picesRe1 = shapeToPices(r1.getShape(), r1.getIngredientMap());
			ItemStack[] picesRe2 = shapeToPices(r2.getShape(), r2.getIngredientMap());

			if (!Arrays.equals(picesRe1, picesRe2)) {
				mirrorMatrix(picesRe1);
				return Arrays.equals(picesRe1, picesRe2);
			}

			return true;
		} else if (recipe1 instanceof ShapelessRecipe) {
			if (recipe2 instanceof ShapelessRecipe == false) {
				return false;
			}

			ShapelessRecipe r1 = (ShapelessRecipe) recipe1;
			ShapelessRecipe r2 = (ShapelessRecipe) recipe2;

			List<ItemStack> find = r1.getIngredientList();
			List<ItemStack> compare = r2.getIngredientList();

			if (find.size() != compare.size()) {
				return false;
			}

			for (ItemStack item : compare) {
				if (!find.remove(item)) {
					return false;
				}
			}

			return find.isEmpty();
		} else {
			throw new IllegalArgumentException("Unsupported recipe type: '" + recipe1 + "', update this class!");
		}
	}

	private static ItemStack[] shapeToPices(String[] shape, Map<Character, ItemStack> map) {
		ItemStack[] pices = new ItemStack[9];
		int slot = 0;

		for (int r = 0; r < shape.length; r++) {
			for (char col : shape[r].toCharArray()) {
				pices[slot] = map.get(col);
				slot++;
			}
			slot = ((r + 1) * 3);
		}
		return pices;
	}

	private static void mirrorMatrix(ItemStack[] pices) {
		ItemStack tmp;

		for (int r = 0; r < 3; r++) {
			tmp = pices[(r * 3)];
			pices[(r * 3)] = pices[(r * 3) + 2];
			pices[(r * 3) + 2] = tmp;
		}
	}
}
