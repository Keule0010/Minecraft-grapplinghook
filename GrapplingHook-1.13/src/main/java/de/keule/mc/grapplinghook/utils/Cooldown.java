package de.keule.mc.grapplinghook.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Cooldown {
	private HashMap<UUID, Long> cooldown;
	public int cooldownTime;

	public Cooldown(int cooldowntime) {
		this.cooldownTime = cooldowntime;

		if (cooldownTime > 0)
			this.cooldown = new HashMap<>();
	}

	public long getCooldown(Player p) {
		if (cooldownTime <= 0)
			return 0;

		if (!cooldown.containsKey(p.getUniqueId()))
			return 0;

		final long secondsLeft = ((cooldown.get(p.getUniqueId()) / 1000) + cooldownTime)
				- (System.currentTimeMillis() / 1000);

		if (secondsLeft > 0)
			return secondsLeft;

		return 0;
	}

	public void addPlayer(Player p) {
		if (cooldownTime <= 0)
			return;
		cooldown.put(p.getUniqueId(), System.currentTimeMillis());
	}

	public void removePlayer(Player p) {
		if (cooldownTime <= 0)
			return;
		cooldown.remove(p.getUniqueId());
	}

	@Override
	public String toString() {
		return cooldownTime + "";
	}
}
