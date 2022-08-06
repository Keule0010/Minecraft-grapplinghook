package de.keule.mc.grapplinghhok.adapters.events;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerFishEvent;

public interface PlayerFischEventAdapter {

	public Location getHookLocation(PlayerFishEvent e);

}