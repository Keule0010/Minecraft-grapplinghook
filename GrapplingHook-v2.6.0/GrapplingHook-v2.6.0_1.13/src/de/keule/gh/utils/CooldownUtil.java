package de.keule.gh.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.keule.gh.config.ConfigKeys;
import de.keule.gh.config.ConfigManager;

public class CooldownUtil {
	private static HashMap<UUID, Long> cooldown;
	public static long cooldowntime;
	
	static {
		cooldown = new HashMap<UUID, Long>();
		refresh();
	}

	public static boolean cooldown(Player p) {
		if (cooldown.containsKey(p.getUniqueId())) {
			final long secondsLeft = ((cooldown.get(p.getUniqueId()) / 1000) + cooldowntime)
					- (System.currentTimeMillis() / 1000);
			if (secondsLeft > 0) {
				p.sendMessage(
						ConfigManager.getMessage(ConfigKeys.COOLDOWN_MSG).replace("%timeLeft%", "" + secondsLeft));
				return false;
			} else {
				cooldown.put(p.getUniqueId(), System.currentTimeMillis());
				return true;
			}
		} else {
			cooldown.put(p.getUniqueId(), System.currentTimeMillis());
			return true;
		}
	}

	public static void refresh() {
		cooldowntime = ConfigManager.getInt(ConfigKeys.COOLDOWN);
		cooldown.clear();
	}

	public static void removePlayer(Player p) {
		if(cooldown.containsKey(p.getUniqueId()))
			cooldown.remove(p.getUniqueId());
	}
}
