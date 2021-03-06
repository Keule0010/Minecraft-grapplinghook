package de.keule.grapplinghook.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import de.keule.grapplinghook.crafting.RecipeUtil;
import de.keule.grapplinghook.main.Main;

public class CraftListener implements Listener {

	@EventHandler
	public void onCrafting(PrepareItemCraftEvent e) {
		if (RecipeUtil.areEqual(e.getRecipe(), Main.re.getRecipe())) {
			if (e.getView().getPlayer() instanceof Player) {
				Player p = (Player) e.getView().getPlayer();
				
				if(Main.getPlugin().getConfig().getBoolean("Plugin.crafting")) {
					if (!p.isOp() && !p.hasPermission("grapplinghook.craft")) {
							e.getInventory().setResult(null);
							p.sendMessage(Main.noperm);
						}
				}else 
					e.getInventory().setResult(null);
			}
		}
	}
}
