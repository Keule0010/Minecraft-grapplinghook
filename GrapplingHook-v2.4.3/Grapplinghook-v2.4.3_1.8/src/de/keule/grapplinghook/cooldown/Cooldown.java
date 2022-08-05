package de.keule.grapplinghook.cooldown;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.keule.grapplinghook.main.Main;

public class Cooldown {

	private static HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
	public static long cooldowntime = Main.getPlugin().getConfig().getLong("Plugin.cooldown");//Main.getPlugin().getConfig().getLong("Plugin.cooldown")
	
	public boolean cooldown(Player p) {
		if (cooldown.containsKey(p.getUniqueId())) {
			long secondsLeft = ((cooldown.get(p.getUniqueId()) / 1000) + cooldowntime)
					- (System.currentTimeMillis() / 1000);
			if (secondsLeft > 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Messages.cooldown").replace("%prefix%", Main.prefix)
				.replace("%timeLeft%", ""+secondsLeft)));
				return false;
			} else {
				cooldown.put(p.getUniqueId(), System.currentTimeMillis());
				return true;
			}
		}else {
			cooldown.put(p.getUniqueId(), System.currentTimeMillis());
			return true;
		}
	}
	
	public static void refresh() {
		cooldowntime = Main.getPlugin().getConfig().getLong("Plugin.cooldown");
		cooldown.clear();
	}
}
