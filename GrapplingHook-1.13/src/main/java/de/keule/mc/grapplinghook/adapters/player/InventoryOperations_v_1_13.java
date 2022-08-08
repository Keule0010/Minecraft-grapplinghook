package de.keule.mc.grapplinghook.adapters.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.keule.mc.grapplinghhok.adapters.player.InventoryOperations;

public class InventoryOperations_v_1_13 implements InventoryOperations {

	@Override
	public ItemStack getGrapplingHookFromActiveHand(Player p) {
		ItemStack is = p.getInventory().getItemInMainHand();
		if (is == null || is.getType() != Material.FISHING_ROD)
			is = p.getInventory().getItemInOffHand();

		return is;
	}
}