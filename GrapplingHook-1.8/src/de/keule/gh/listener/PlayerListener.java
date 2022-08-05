package de.keule.gh.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import de.keule.gh.config.ConfigKeys;
import de.keule.gh.config.ConfigManager;
import de.keule.gh.utils.NoFallDamageUtil;
import de.keule.gh.utils.RecipeUtil;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause() == DamageCause.FALL) {
				Player p = (Player) e.getEntity();
				if (NoFallDamageUtil.containsPlayer(p)) {
					e.setCancelled(true);
					NoFallDamageUtil.removePlayer(p);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCrafting(PrepareItemCraftEvent e) {
		if (e.getView().getPlayer() instanceof Player) {
			if (RecipeUtil.isEqual(e.getRecipe())) {
				Player p = (Player) e.getView().getPlayer();

				if (ConfigManager.getBoolean(ConfigKeys.CRAFTING)) {
					if (!p.isOp() && !p.hasPermission("grapplinghook.craft")) {
						e.getInventory().setResult(null);
						p.sendMessage(ConfigManager.getNoPerm());
					}
				} else
					e.getInventory().setResult(null);
			}
		}
	}
}
