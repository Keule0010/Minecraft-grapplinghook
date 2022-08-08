package de.keule.mc.grapplinghook.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.keule.mc.grapplinghhok.adapters.itemstack.CreateGrapplingHook;
import de.keule.mc.grapplinghhok.adapters.worldguard.WorldGuardLogic;
import de.keule.mc.grapplinghook.config.ConfVars;
import de.keule.mc.grapplinghook.config.ConfigKey;
import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.config.Settings;
import de.keule.mc.grapplinghook.recipe.GHRecipe;
import de.keule.mc.grapplinghook.utils.Cooldown;
import de.keule.mc.grapplinghook.utils.NoFallDamage;
import de.keule.mc.grapplinghook.version.VersionUtil;
import de.keule.mc.grapplinghook.worldguard.WorldGuardManager;

public class GrapplingHook {
	private static final Pattern VALID_NAME = Pattern.compile("[a-z0-9_-]+");

	private static final List<GrapplingHook> grapplingHooks = new ArrayList<>();
	public static GrapplingHook ALL_RODS;

	private boolean cancelOnEntityCatch;
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
	private String name;

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
		name = path;

		cancelOnEntityCatch = ConfigManager.getGrapplingHookConfig()
				.getBlooean(path + "." + ConfigKey.CANCEL_ON_ENTITY.getGH_PATH(), true);
		destroyOnNoUses = ConfigManager.getGrapplingHookConfig()
				.getBlooean(path + "." + ConfigKey.DESTROY_NO_USES.getGH_PATH(), false);
		useFloatingBlocks = ConfigManager.getGrapplingHookConfig()
				.getBlooean(path + "." + ConfigKey.USE_FLOATING_BLOCKS.getGH_PATH(), true);
		noFallDamage = ConfigManager.getGrapplingHookConfig()
				.getBlooean(path + "." + ConfigKey.NO_FALL_DAMAGE.getGH_PATH(), true);
		unbreakable = ConfigManager.getGrapplingHookConfig().getBlooean(path + "." + ConfigKey.UNBREAKABLE.getGH_PATH(),
				false);
		unlimited = ConfigManager.getGrapplingHookConfig()
				.getBlooean(path + "." + ConfigKey.UNLIMITED_USES.getGH_PATH(), false);
		allWorlds = ConfigManager.getGrapplingHookConfig().getBlooean(path + "." + ConfigKey.ALL_WORLDS.getGH_PATH(),
				true);
		crafting = ConfigManager.getGrapplingHookConfig().getBlooean(path + "." + ConfigKey.CRAFTING.getGH_PATH(),
				false);
		useAir = ConfigManager.getGrapplingHookConfig().getBlooean(path + "." + ConfigKey.USE_AIR.getGH_PATH(), false);
		glow = ConfigManager.getGrapplingHookConfig().getBlooean(path + "." + ConfigKey.GLOW.getGH_PATH(), false);

		unlimitedName = ConfigManager.getGrapplingHookConfig()
				.getTranslatedString(path + "." + ConfigKey.UNLIMITED_USES_NAME.getGH_PATH(), "&cunlimited");
		displayName = ConfigManager.getGrapplingHookConfig()
				.getTranslatedString(path + "." + ConfigKey.DISPLAY_NAME.getGH_PATH(), null);
		permission = ConfigManager.getGrapplingHookConfig().getString(path + "." + ConfigKey.PERMISSION.getGH_PATH(),
				"none");
		permissionRequired = permission != null && !permission.isEmpty() && !permission.equalsIgnoreCase("none");

		pullSound = ConfigManager.getGrapplingHookConfig().getSound(path + "." + ConfigKey.PULL_SOUND.getGH_PATH(),
				VersionUtil.getDefaultSounds().getPullSound());
		breakSound = ConfigManager.getGrapplingHookConfig().getSound(path + "." + ConfigKey.BREAK_SOUND.getGH_PATH(),
				VersionUtil.getDefaultSounds().getBreakSound());

		multiplier = ConfigManager.getGrapplingHookConfig()
				.getDouble(path + "." + ConfigKey.THROW_SPEED_MULTI.getGH_PATH(), 0.25);
		gravity = ConfigManager.getGrapplingHookConfig().getDouble(path + "." + ConfigKey.GRAVITY.getGH_PATH(), 0.35);

		worlds = ConfigManager.getGrapplingHookConfig().getStringList(path + ".worlds");
		lore = ConfigManager.getGrapplingHookConfig().getStringList(path + ".lore");

		destroyDelay = ConfigManager.getGrapplingHookConfig().getInt(path + "." + ConfigKey.DESTROY_DELAY.getGH_PATH(),
				10);
		cooldown = ConfigManager.getGrapplingHookConfig().getInt(path + "." + ConfigKey.COOLDOWN.getGH_PATH(), 2);
		maxUses = ConfigManager.getGrapplingHookConfig().getInt(path + "." + ConfigKey.MAX_USES.getGH_PATH(), 10);

		cooldownLogic = new Cooldown(cooldown);
		if (!isAllRods() && ConfigManager.getGrapplingHookConfig().pathExists(path + ".recipe"))
			recipe = new GHRecipe(this);
		else
			recipe = new GHRecipe(null);
	}

	public ItemStack getGrapplingHook() {
		if (gh != null)
			return gh;

		CreateGrapplingHook createGH = VersionUtil.getCreateGrapplingHook();
		gh = createGH.create(VersionUtil.getGlow(), getDisplayName(), getLore(getMaxUses()), isGlow(), isUnbreakable());

		return gh;
	}

	private int getUsesLeft(Player p) {
		if (unlimited || isAllRods())
			return Integer.MAX_VALUE;

		final ItemStack gh = getPlayerGrapplingHook(p);
		if (gh == null)
			return 0;

		final ItemMeta meta = gh.getItemMeta();
		final List<String> lore = meta.getLore();
		if (lore == null || lore.isEmpty())
			return 0;

		final String l = lore.get(lore.size() - 1).replaceAll(ChatColor.COLOR_CHAR + "", "").replaceAll("\\s+", "");
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
		if (isAllRods())
			return null;

		final List<String> newLore = new ArrayList<>();
		for (String line : lore)
			newLore.add(ChatColor.translateAlternateColorCodes('&', line).replace(ConfVars.PREFIX, Settings.getPrefix())
					.replace(ConfVars.USES, getUsesString(uses)).replace(ConfVars.MAX_USES, getUsesString(maxUses)));

		if (unlimited) {
			newLore.add(ChatColor.COLOR_CHAR + "a");
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
		ItemStack is = VersionUtil.getInventoryOperations().getGrapplingHookFromActiveHand(p);
		if (!equals(is))
			return null;

		return is;
	}

	public void checkAndPull(Player p, Location destination) {
		final String pWorld = p.getLocation().getWorld().getName();

		/* Check Grappling Hook Enabled In This World/Region */
		if (WorldGuardManager.isWorldGuardEnabled()) {
			WorldGuardLogic wg = VersionUtil.getWorldGuardLogic(null);
			if (isAllWorlds() || getWorlds().contains(pWorld) || wg.isPermitted(p)) {
				if (wg.isForbidden(p))
					return;
			} else
				return;
		} else if (!isAllWorlds() && !getWorlds().contains(pWorld))
			return;

		/* Check Permission For Grappling Hook */
		if (permissionRequired && !p.isOp() && !p.hasPermission(permission))
			return;

		/* Check Permissions For World */
		if (!p.isOp() && p.hasPermission("grapplinghook.removeWorld." + pWorld))
			return;

		/* Check Permissions For World */
		if (!p.isOp() && !(p.hasPermission(Permissions.DEFAULT_PLAYER.getPERM())
				|| p.hasPermission(Permissions.GH_ALL_WORLDS.getPERM())
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
		pullPlayer(p, destination, usesLeft - 1);
	}

	public void pullPlayer(Player p, Location destination, int newUses) {
		Location lc = p.getLocation();
		Location to = destination;

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

	private boolean isAllRods() {
		return name.equals("allrods");
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

		if (is.getType() != Material.FISHING_ROD)
			return false;

		ItemMeta meta = is.getItemMeta();
		if (meta.getDisplayName() == null)
			return false;

		if (!meta.getDisplayName().equals(displayName))
			return false;

		return compareLore(this.getGrapplingHook().getItemMeta().getLore(), meta.getLore());
	}

	private boolean compareLore(List<String> lore1, List<String> lore2) {
		if (lore1 == null && lore2 == null)
			return true;
		if ((lore1 == null && lore2 != null) || (lore1 != null && lore2 == null))
			return false;

		if (lore1.size() != lore2.size())
			return false;

		for (int i = 0; i < lore.size() - 1; i++) {
			if (!lore1.get(i).equals(lore2.get(i)))
				return false;
		}
		return true;
	}

	/* Getters */
	public String getName() {
		return name;
	}

	public GHRecipe getRecipe() {
		return recipe;
	}

	public boolean isCrafting() {
		return crafting;
	}

	public boolean cancelOnEntityCatch() {
		return cancelOnEntityCatch;
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public String getDisplayName() {
		return isAllRods() ? null : displayName;
	}

	public boolean isUnbreakable() {
		return unbreakable;
	}

	public boolean isGlow() {
		return glow;
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

	public int getMaxUses() {
		return maxUses;
	}

	public void printInfo(CommandSender s) {
		s.sendMessage(Settings.getPrefix() + tr(" &7-------- &a" + getName() + " &7--------"));
		Field[] allFields = GrapplingHook.class.getDeclaredFields();
		for (Field field : allFields) {
			if (!Modifier.isStatic(field.getModifiers()) && Modifier.isPrivate(field.getModifiers())) {
				try {
					if (field.getType() == ItemStack.class || field.getType() == Cooldown.class)
						continue;

					ChatColor c = ChatColor.RESET;
					if (field.getType() == boolean.class || field.getType() == double.class
							|| field.getType() == int.class) {
						c = ChatColor.GREEN;
					}

					s.sendMessage(
							Settings.getPrefix() + tr(" &7" + field.getName() + ": " + c + field.get(this).toString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
			}
		}
	}

	private String tr(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	/* Static */
	public static GrapplingHook getGrapplingHook(String grapplingHookName) {
		for (GrapplingHook gh : GrapplingHook.getGrapplingHooks()) {
			if (gh.getName().equalsIgnoreCase(grapplingHookName)) {
				return gh;
			}
		}
		return null;
	}

	public static void reloadGrapplingHooks() {
		ALL_RODS = new GrapplingHook("allrods");
		grapplingHooks.clear();

		for (String key : ConfigManager.getGrapplingHookConfig().getKeys()) {
			if (key.equals("allrods"))
				continue;
			grapplingHooks.add(new GrapplingHook(key));
		}
		grapplingHooks.add(ALL_RODS);
	}

	public static List<GrapplingHook> getGrapplingHooks() {
		return grapplingHooks;
	}

	/**
	 * The name/config path may only contain {@code a-z 0-9 _ -} and may be a
	 * maximum of 10 characters long as well as lower case.
	 */
	public static boolean checkName(String name) {
		if (name.length() > 10)
			return false;

		if (!VALID_NAME.matcher(name).matches())
			return false;

		for (GrapplingHook grapplingHook : grapplingHooks) {
			if (grapplingHook.getName().equals(name))
				return false;
		}

		return true;
	}

	public static boolean createNew(String name) {
		Scanner s = null;
		try (InputStream inS = GrapplingHook.class.getResourceAsStream("/ghBlueprint.yml");
				FileOutputStream fOut = new FileOutputStream(ConfigManager.getGrapplingHookConfig().getConfigFile(),
						true)) {

			BufferedInputStream bis = new BufferedInputStream(inS);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			for (int result = bis.read(); result != -1; result = bis.read()) {
				buf.write((byte) result);
			}

			String res = "\n" + buf.toString().replaceAll(ConfVars.GH_NAME, name);
			fOut.write(res.getBytes());
			return ConfigManager.reloadAll();
		} catch (Exception e) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Couldn't read grappling hook blueprint!", e);
		} finally {
			if (s != null)
				s.close();
		}
		return false;
	}
}