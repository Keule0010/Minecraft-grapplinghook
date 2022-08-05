package de.keule.gh.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import de.keule.gh.config.ConfigKeys;
import de.keule.gh.config.ConfigManager;
import de.keule.gh.main.GHPlugin;
import de.keule.gh.main.GrapplingHook;
import de.keule.gh.utils.CooldownUtil;
import de.keule.gh.utils.NoFallDamageUtil;
import de.keule.gh.utils.WorldGuardUtil;

public class GHListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onRectract(PlayerFishEvent e) {
		if (e.getState().equals(PlayerFishEvent.State.FISHING) || e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)
				|| e.getState().toString().equalsIgnoreCase("BITE"))
			return;

		if (ConfigManager.getBoolean(ConfigKeys.CANCEL_ON_ENTITY_CATCH))
			if (e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY))
				return;

		final Player p = e.getPlayer();
		final String pWorld = p.getLocation().getWorld().getName();

		if(!ConfigManager.getBoolean(ConfigKeys.ALL_RODS))
			if(!GrapplingHook.isEqual(p.getItemInHand()))
				return;
		
		if (GHPlugin.worldGuardExists()) {
			if (ConfigManager.getList(ConfigKeys.WORLDS).contains(pWorld)
					|| ConfigManager.getBoolean(ConfigKeys.USE_ALL_WORLDS) || WorldGuardUtil.isPermitted(p)) {
				if (!WorldGuardUtil.isForbidden(p))
					permissionControl(p, pWorld, e.getHook());
			}
		} else {
			if (ConfigManager.getList(ConfigKeys.WORLDS).contains(pWorld)
					|| ConfigManager.getBoolean(ConfigKeys.USE_ALL_WORLDS))
				permissionControl(p, pWorld, e.getHook());

		}
	}

	private void permissionControl(Player p, String pWorld, FishHook h) {
		if (p.isOp() || p.hasPermission("grapplinghook.worlds.*") || p.hasPermission("grapplinghook.world." + pWorld)) {

			if (!(p.isOp() || p.hasPermission("grapplinghook.worlds.*"))
					&& p.hasPermission("grapplinghook.removeWorld." + pWorld))
				return;

			if (CooldownUtil.cooldown(p)) {
				if (ConfigManager.getBoolean(ConfigKeys.ALL_RODS))
					pullPlayer(p, h);
				else if (GrapplingHook.isEqual(p.getInventory().getItemInHand()))
					pullPlayer(p, h);
			}
		}
	}

	private void pullPlayer(Player p, FishHook hook) {
		Location lc = p.getLocation();
		Location to = hook.getLocation();

		if (!ConfigManager.getBoolean(ConfigKeys.USE_AIR))
			if (!ConfigManager.getBoolean(ConfigKeys.BLOCK_FLOATING)) {
				if (to.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					CooldownUtil.removePlayer(p);
					return;
				}
			} else if (to.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.WEST).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.EAST).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.NORTH).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
				CooldownUtil.removePlayer(p);
				return;
			}

		if (!GrapplingHook.updateLore(p)) {
			CooldownUtil.removePlayer(p);
			return;
		}

		try {
			p.playSound(lc, Sound.valueOf(ConfigManager.getString(ConfigKeys.SOUND)), 10, 100);
		} catch (Exception e) {
			if (p.isOp())
				GHPlugin.sendConsoleMesssage("The sound is misspeled!");
		}

		double m = ConfigManager.getDouble(ConfigKeys.SPEED_MULTIPLIER) * 2;
		double d = lc.distance(to);
		double v_x = (1.0D + m * d) * (to.getX() - lc.getX()) / d;
		double v_y = (1.0D + 0.3D * d) * (to.getY() - lc.getY()) / d
				- 0.5D * ConfigManager.getDouble(ConfigKeys.GRAVITY) * d;
		double v_z = (1.0D + m * d) * (to.getZ() - lc.getZ()) / d;
		Vector v = p.getVelocity();
		v.setX(v_x);
		v.setY(v_y);
		v.setZ(v_z);
		p.setVelocity(v);
		NoFallDamageUtil.addPlayer(p);
	}
}
