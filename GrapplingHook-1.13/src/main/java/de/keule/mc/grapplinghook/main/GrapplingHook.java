package de.keule.mc.grapplinghook.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.keule.mc.grapplinghook.config.ConfVars;
import de.keule.mc.grapplinghook.config.ConfigKey;
import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.config.Settings;
import de.keule.mc.grapplinghook.crafting.GHRecipe;
import de.keule.mc.grapplinghook.crafting.Glow;
import de.keule.mc.grapplinghook.utils.Cooldown;
import de.keule.mc.grapplinghook.utils.NoFallDamage;
import de.keule.mc.grapplinghook.worldguard.WorldGuardLogic;

public class GrapplingHook {
	private static final List<GrapplingHook> grapplingHooks = new ArrayList<>();
	public static final GrapplingHook All_RODS = null;
	private static final char COLOR_CODE = (char) 167;

	private boolean cancleOnEntityCatch;
	private boolean permissionRequired;
	private boolean useFloatingBlocks;
	private boolean destroyOnNoUses;
	private boolean noFallDamage;
	private boolean unbreakable;
	private boolean allWorlds;
	private boolean unlimited;
	private boolean crafting;
	private boolean useAir;
	private boolean glow;

	private String unlimitedName;
	private String displayName;
	private String permission;
	private String configPath;

	private double multiplier;
	private double gravity;

	private List<String> worlds;
	private List<String> lore;

	private int destroyDelay;
	private int cooldown;
	private int maxUses;

	private Sound breakSound;
	private Sound pullSound;

	private Cooldown cooldownLogic;
	private GHRecipe recipe;
	private ItemStack gh;

	public GrapplingHook(String path) {
		configPath = path;

		cancleOnEntityCatch = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".cancleOnEntityCatch");
		destroyOnNoUses = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".destroyWhenNoMoreUses");
		useFloatingBlocks = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".useFloatingBlocks");
		noFallDamage = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".noFallDamage");
		unbreakable = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".unbreakable");
		unlimited = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".unlimitedUses");
		allWorlds = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".allWorlds");
		crafting = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".crafting");
		useAir = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".useAir");
		glow = ConfigManager.getGrapplingHookConfig().getBlooean(path + ".glow");

		unlimitedName = ConfigManager.getGrapplingHookConfig().getTranslatedString(path + ".unlimitedUsesName");
		displayName = ConfigManager.getGrapplingHookConfig().getTranslatedString(path + ".displayName");
		permission = ConfigManager.getGrapplingHookConfig().getString(path + ".permission");
		permissionRequired = permission != null && !permission.isEmpty() && !permission.equalsIgnoreCase("none");

		pullSound = ConfigManager.getGrapplingHookConfig().getSound(path + ".pullSound",
				Sound.ENTITY_ENDERMAN_TELEPORT);
		breakSound = ConfigManager.getGrapplingHookConfig().getSound(path + ".breakSound", Sound.ENTITY_ITEM_BREAK);

		multiplier = ConfigManager.getGrapplingHookConfig().getDouble(path + ".throw_speed_multiplier");
		cooldown = ConfigManager.getGrapplingHookConfig().getInt(path + ".cooldown");
		gravity = ConfigManager.getGrapplingHookConfig().getDouble(path + ".gravity");

		worlds = ConfigManager.getGrapplingHookConfig().getStringList(path + ".worlds");
		lore = ConfigManager.getGrapplingHookConfig().getStringList(path + ".lore");

		destroyDelay = ConfigManager.getGrapplingHookConfig().getInt(path + ".destroyDelay");
		maxUses = ConfigManager.getGrapplingHookConfig().getInt(path + ".maxUses");

		cooldownLogic = new Cooldown(cooldown);
		recipe = new GHRecipe(this);
	}

	public ItemStack getGrapplingHook() {
		if (gh != null)
			return gh;

		gh = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta = gh.getItemMeta();

		meta.setDisplayName(displayName);
		meta.setLore(getLore(maxUses));

		if (glow)
			if (Glow.get() == null)
				meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
			else
				meta.addEnchant(Glow.get(), 1, true);

		if (unbreakable) {
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			meta.setUnbreakable(true);
		}

		gh.setItemMeta(meta);
		return gh;
	}

	private int getUsesLeft(Player p) {
		if (unlimited)
			return Integer.MAX_VALUE;

		final ItemStack gh = getPlayerGrapplingHook(p);
		if (gh == null)
			return 0;

		final ItemMeta meta = gh.getItemMeta();
		final List<String> lore = meta.getLore();
		if (lore == null || lore.isEmpty())
			return 0;

		final String l = lore.get(lore.size() - 1).replaceAll(COLOR_CODE + "", "").replaceAll("\\s+", "");
		if (l.equals("a"))
			return maxUses;

		int uses = 0;
		try {
			uses = Integer.parseInt(l);
		} catch (NumberFormatException e) {
		}
		return uses;
	}

	public List<String> getLore(int uses) {
		final List<String> newLore = new ArrayList<>();
		for (String line : lore)
			newLore.add(ChatColor.translateAlternateColorCodes('&', line).replace(ConfVars.PREFIX, Settings.getPrefix())
					.replace(ConfVars.USES, getUsesString(uses)).replace(ConfVars.MAX_USES, getUsesString(maxUses)));
		if (unlimited) {
			newLore.add(COLOR_CODE + "a");
			return newLore;
		}

		String l = " ";
		for (char c : (uses + "").toCharArray())
			l += ChatColor.getByChar(c) + " ";
		newLore.add(l);
		return newLore;
	}

	private CharSequence getUsesString(int uses) {
		return unlimited ? unlimitedName : uses + "";
	}

	public void updateLore(Player p, int uses) {
		final ItemStack gh = getPlayerGrapplingHook(p);
		if (gh == null)
			return;

		final ItemMeta meta = gh.getItemMeta();
		final List<String> lore = meta.getLore();
		if (lore == null || lore.isEmpty())
			return;

		meta.setLore(getLore(uses));
		gh.setItemMeta(meta);
		return;
	}

	private void destroyHookOnNoUses(Player p) {
		if (!destroyOnNoUses)
			return;

		final ItemStack is = getPlayerGrapplingHook(p);
		if (is == null)
			return;

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GHPlugin.getInstance(), () -> {
			p.getInventory().remove(is);
			p.playSound(p.getLocation(), breakSound, 10, 100);
		}, destroyDelay);
	}

	private ItemStack getPlayerGrapplingHook(Player p) {
		ItemStack is = p.getInventory().getItemInMainHand();
		if (!equals(is))
			is = p.getInventory().getItemInOffHand();

		if (!equals(is))
			return null;

		return is;
	}

	public void checkAndPull(Player p, FishHook hook) {
		final String pWorld = p.getLocation().getWorld().getName();

		/* Check Grappling Hook Enabled In This World/Region */
		WorldGuardLogic wg = GHPlugin.getInstance().getWorldGuardLogic();
		if (isAllWorlds() || getWorlds().contains(pWorld) || wg.isPermitted(p)) {
			if (wg.isForbidden(p))
				return;
		} else
			return;

		/* Check Permission For Grappling Hook */
		if (permissionRequired && !p.isOp() && !p.hasPermission(permission))
			return;

		/* Check Permissions For World */
		if (!p.isOp() && p.hasPermission("grapplinghook.removeWorld." + pWorld))
			return;

		/* Check Permissions For World */
		if (!p.isOp() && !(p.hasPermission(Permissions.GH_ALL_WORLDS.getPERM())
				|| p.hasPermission("grapplinghook.world." + pWorld)))
			return;

		/* Check Uses */
		final int usesLeft = getUsesLeft(p);
		if (usesLeft <= 0) {
			destroyHookOnNoUses(p);
			p.sendMessage(ConfigManager.getMsgConfig().getMessage(ConfigKey.NO_USES_LEFT));
			return;
		}

		/* Check Cooldown */
		long secondsLeft = cooldownLogic.getCooldown(p);
		if (secondsLeft > 0) {
			p.sendMessage(ConfigManager.getMsgConfig().getMessage(ConfigKey.COOLDOWN_MSG).replace(ConfVars.TIME_LEFT,
					"" + secondsLeft));
			return;
		}

		/* Pull Player */
		pullPlayer(p, hook, usesLeft - 1);
	}

	public void pullPlayer(Player p, FishHook hook, int newUses) {
		Location lc = p.getLocation();
		Location to = hook.getLocation();

		if (!useAir) {
			if (!useFloatingBlocks) {
				if (to.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					return;
				}
			} else if (to.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.WEST).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.EAST).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.NORTH).getType() == Material.AIR
					&& to.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
				return;
			}
		}

		/* Pull player */
		double m = multiplier * 2;
		double d = lc.distance(to);
		double v_x = (1.0D + m * d) * (to.getX() - lc.getX()) / d;
		double v_y = (1.0D + 0.3D * d) * (to.getY() - lc.getY()) / d - 0.5D * gravity * d;
		double v_z = (1.0D + m * d) * (to.getZ() - lc.getZ()) / d;
		Vector v = p.getVelocity();
		v.setX(v_x);
		v.setY(v_y);
		v.setZ(v_z);
		p.setVelocity(v);

		/* Update stuff */
		p.playSound(lc, pullSound, 10, 100);
		cooldownLogic.addPlayer(p);
		if (noFallDamage)
			NoFallDamage.addPlayer(p);
		if (newUses <= 0)
			destroyHookOnNoUses(p);
		updateLore(p, newUses);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof ItemStack))
			return false;

		final ItemStack is = (ItemStack) obj;

		return equals(is);
	}

	public boolean equals(ItemStack is) {
		if (is == null)
			return false;

		if (!is.getType().equals(Material.FISHING_ROD))
			return false;

		if (is.getItemMeta().getDisplayName() == null)
			return false;

		if (is.getItemMeta().getDisplayName().equals(displayName))
			return true;

		return false;
	}

	/* Getters */
	public String getConfigPath() {
		return configPath;
	}

	public GHRecipe getRecipe() {
		return recipe;
	}

	public boolean isCrafting() {
		return crafting;
	}

	public boolean cancelOnEntityCatch() {
		return cancleOnEntityCatch;
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public boolean isAllWorlds() {
		return allWorlds;
	}

	public boolean isPermissionRequired() {
		return permissionRequired;
	}
	
	public String getPermission() {
		return permission;
	}
	
	/* Static */
	public static GrapplingHook getGrapplingHook(String grapplingHookName) {
		for (GrapplingHook gh : GrapplingHook.getGrapplingHooks()) {
			if (gh.getConfigPath().equalsIgnoreCase(grapplingHookName)) {
				return gh;
			}
		}
		return null;
	}

	public static void reloadGrapplingHooks() {
		grapplingHooks.clear();

		for (String key : ConfigManager.getGrapplingHookConfig().getKeys()) {
			grapplingHooks.add(new GrapplingHook(key));
		}
	}

	public static List<GrapplingHook> getGrapplingHooks() {
		return grapplingHooks;
	}
}