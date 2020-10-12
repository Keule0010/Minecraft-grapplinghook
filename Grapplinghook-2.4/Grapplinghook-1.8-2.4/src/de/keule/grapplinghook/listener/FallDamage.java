package de.keule.grapplinghook.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FallDamage implements Listener {
	private static List<UUID> fallDamage = new ArrayList<UUID>();
	//Fall Damage gucke nach ein paar millisekunden ob spieler auf dem Boden ist wenn ja dann aus der noFallDamage liste entferenen
	
	@EventHandler
	public void onFallDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				if (fallDamage.contains(p.getUniqueId())) {
					e.setCancelled(true);
					fallDamage.remove(p.getUniqueId());
				}
			}
		}
	}

	public static void setPlayerNoFall(Player p, boolean state) {
		if (state)
			fallDamage.remove(p.getUniqueId());
		else
			fallDamage.add(p.getUniqueId());
	}
}
