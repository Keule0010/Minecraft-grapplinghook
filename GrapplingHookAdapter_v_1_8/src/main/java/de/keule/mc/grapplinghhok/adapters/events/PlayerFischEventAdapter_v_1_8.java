package de.keule.mc.grapplinghhok.adapters.events;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerFischEventAdapter_v_1_8 implements PlayerFischEventAdapter {

	@Override
	public Location getHookLocation(PlayerFishEvent e) {
		return e.getHook().getLocation();
	}
}