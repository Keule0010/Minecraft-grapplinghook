package de.keule.grapplinghook.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.keule.grapplinghook.main.Main;

public class JoinListener implements Listener{

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(Main.error == true && (p.hasPermission("grapplinghook.join") || p.isOp())) {
				p.sendMessage(Main.prefix + "§4An Error occured!§c See the console for more information.");
		}
	}
}

//if(Main.updateAv = true) {
//	p.sendMessage(Main.prefix + "§2There is a new update available.");
//}