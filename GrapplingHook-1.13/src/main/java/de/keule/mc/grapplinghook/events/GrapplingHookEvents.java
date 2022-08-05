package de.keule.mc.grapplinghook.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import de.keule.mc.grapplinghook.config.Settings;
import de.keule.mc.grapplinghook.main.GrapplingHook;

public class GrapplingHookEvents implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onRectract(PlayerFishEvent e) {
		if (e.getState() == PlayerFishEvent.State.FISHING || e.getState() == PlayerFishEvent.State.CAUGHT_FISH
				|| e.getState() == PlayerFishEvent.State.BITE)
			return;

		final Player p = e.getPlayer();

		if (Settings.isAllRods()) {
			GrapplingHook.All_RODS.checkAndPull(p, e.getHook());
			return;
		}

		final GrapplingHook gh = getGrapplingHook(e);
		if (gh == null)
			return;

		if (gh.cancelOnEntityCatch() && e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY)
			return;

		gh.checkAndPull(p, e.getHook());
	}

	private GrapplingHook getGrapplingHook(PlayerFishEvent e) {
		ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
		if (is.getType() != Material.FISHING_ROD)
			is = e.getPlayer().getInventory().getItemInOffHand();
		if (is.getType() != Material.FISHING_ROD)
			return null;

		for (GrapplingHook gh : GrapplingHook.getGrapplingHooks()) {
			if (gh.equals(is)) {
				return gh;
			}
		}
		return null;
	}
}