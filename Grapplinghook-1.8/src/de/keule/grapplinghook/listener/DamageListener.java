package de.keule.grapplinghook.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.keule.grapplinghook.cooldown.NoFallDamage;

public class DamageListener implements Listener {
	private NoFallDamage noFallDamage = NoFallDamage.getInstance();

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (noFallDamage.containsPlayer(p.getUniqueId())) {
					e.setCancelled(true);
					noFallDamage.removePlayer(p.getUniqueId());
				}
			}
		}
	}
}
