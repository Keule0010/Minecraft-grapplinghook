package de.keule.grapplinghook.crafting;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import de.keule.grapplinghook.main.Main;

public class GrapplingHookRecipe {
	private ShapedRecipe ghRecipe;

	public void loadRecipe() {
		ItemStack is = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor
				.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Plugin.grapplingHookName"))
				.replace("%prefix%", Main.prefix));
		meta.setLore(Main.lore);
		is.setItemMeta(meta);

		ghRecipe = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "custom_grappling_hook"), is);
		ghRecipe.shape("123", "456", "789");

		boolean reIsEmpty = Main.cfg.getString("Recipe.slot1") == null && Main.cfg.getString("Recipe.slot2") == null
				&& Main.cfg.getString("Recipe.slot3") == null && Main.cfg.getString("Recipe.slot4") == null
				&& Main.cfg.getString("Recipe.slot5") == null && Main.cfg.getString("Recipe.slot6") == null
				&& Main.cfg.getString("Recipe.slot7") == null && Main.cfg.getString("Recipe.slot8") == null
				&& Main.cfg.getString("Recipe.slot9") == null;

		if (reIsEmpty) {
			ghRecipe.setIngredient('3', Material.STICK);
			ghRecipe.setIngredient('5', Material.STICK);
			ghRecipe.setIngredient('6', Material.STRING);
			ghRecipe.setIngredient('7', Material.STICK);
			ghRecipe.setIngredient('9', Material.IRON_INGOT);
			Main.getPlugin().getConfig().set("Recipe.slot1", "");
			Main.getPlugin().getConfig().set("Recipe.slot2", "");
			Main.getPlugin().getConfig().set("Recipe.slot3", "STICK");
			Main.getPlugin().getConfig().set("Recipe.slot4", "");
			Main.getPlugin().getConfig().set("Recipe.slot5", "STICK");
			Main.getPlugin().getConfig().set("Recipe.slot6", "STRING");
			Main.getPlugin().getConfig().set("Recipe.slot7", "STICK");
			Main.getPlugin().getConfig().set("Recipe.slot8", "");
			Main.getPlugin().getConfig().set("Recipe.slot9", "IRON_INGOT");
			Main.sendConsoleMesssage(Main.prefix + "§4Craftingrecipe was empty. §2Default settings were loaded!");
			try {
				Main.getPlugin().getConfig().save(Main.configFile);
			} catch (IOException e1) {
				Main.sendConsoleMesssage(Main.prefix
						+ "§4Craftingrecipe was empty. §4An error occured! Default settings couldn't saved!");
			}
		} else {
			try {
				if (Main.cfg.getString("Recipe.slot1") != null && !Main.cfg.getString("Recipe.slot1").equals("")) {
					ghRecipe.setIngredient('1',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot1").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot2") != null && !Main.cfg.getString("Recipe.slot2").equals("")) {
					ghRecipe.setIngredient('2',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot2").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot3") != null && !Main.cfg.getString("Recipe.slot3").equals("")) {
					ghRecipe.setIngredient('3',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot3").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot4") != null && !Main.cfg.getString("Recipe.slot4").equals("")) {
					ghRecipe.setIngredient('4',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot4").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot5") != null && !Main.cfg.getString("Recipe.slot5").equals("")) {
					ghRecipe.setIngredient('5',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot5").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot6") != null && !Main.cfg.getString("Recipe.slot6").equals("")) {
					ghRecipe.setIngredient('6',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot6").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot7") != null && !Main.cfg.getString("Recipe.slot7").equals("")) {
					ghRecipe.setIngredient('7',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot7").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot8") != null && !Main.cfg.getString("Recipe.slot8").equals("")) {
					ghRecipe.setIngredient('8',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot8").toUpperCase()));
				}

				if (Main.cfg.getString("Recipe.slot9") != null && !Main.cfg.getString("Recipe.slot9").equals("")) {
					ghRecipe.setIngredient('9',
							Material.getMaterial(Main.getPlugin().getConfig().getString("Recipe.slot9").toUpperCase()));
				}

			} catch (Exception e) {
				Main.sendConsoleMesssage(
						Main.prefix + " §4En error occured! Look that you have written all the materials correctly. Then reload the server.");
			}
		}
		Main.getPlugin().getServer().addRecipe(ghRecipe);
	}

	public ShapedRecipe getRecipe() {
		return this.ghRecipe;
	}
}