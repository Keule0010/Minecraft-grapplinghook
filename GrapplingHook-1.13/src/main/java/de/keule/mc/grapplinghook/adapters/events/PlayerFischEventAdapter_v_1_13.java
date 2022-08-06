package de.keule.mc.grapplinghook.adapters.events;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerFishEvent;

import de.keule.mc.grapplinghhok.adapters.events.PlayerFischEventAdapter;

public class PlayerFischEventAdapter_v_1_13 implements PlayerFischEventAdapter {

	@Override
	public Location getHookLocation(PlayerFishEvent e) {
		return e.getHook().getLocation();
	}
}