package de.keule.mc.grapplinghook.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import de.keule.mc.grapplinghook.config.ConfigKey;
import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.main.GrapplingHook;
import de.keule.mc.grapplinghook.main.Permissions;
import de.keule.mc.grapplinghook.utils.NoFallDamage;

public class PlayerEvents implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player) || e.getCause() != DamageCause.FALL)
			return;

		Player p = (Player) e.getEntity();
		if (NoFallDamage.containsPlayer(p)) {
			e.setCancelled(true);
			NoFallDamage.removePlayer(p);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCrafting(PrepareItemCraftEvent e) {
		if (!(e.getView().getPlayer() instanceof Player))
			return;

		if (e.getRecipe() == null)
			return;

		if (e.getRecipe().getResult().getType() != Material.FISHING_ROD)
			return;

		for (GrapplingHook gh : GrapplingHook.getGrapplingHooks()) {
			if (gh.getRecipe().isEqual(e.getRecipe())) {
				if (gh.isCrafting()) {
					Player p = (Player) e.getView().getPlayer();

					if (!p.isOp() && !p.hasPermission(Permissions.CRAFT.getPERM())) {
						e.getInventory().setResult(null);
						p.sendMessage(ConfigManager.getMsgConfig().getMessage(ConfigKey.NO_PERM));
					} else if (!p.isOp() && gh.isPermissionRequired() && !p.hasPermission(gh.getPermission())) {
						e.getInventory().setResult(null);
						p.sendMessage(ConfigManager.getMsgConfig().getMessage(ConfigKey.NO_PERM));
					}
				} else
					e.getInventory().setResult(null);
				break;
			}
		}
	}
}
