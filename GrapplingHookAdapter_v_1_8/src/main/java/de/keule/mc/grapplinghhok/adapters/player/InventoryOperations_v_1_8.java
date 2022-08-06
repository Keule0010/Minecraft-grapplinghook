package de.keule.mc.grapplinghhok.adapters.player;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryOperations_v_1_8 implements InventoryOperations {

	@Override
	public ItemStack getGrapplingHookFromActiveHand(Player p) {
		return p.getInventory().getItemInHand();
	}
}