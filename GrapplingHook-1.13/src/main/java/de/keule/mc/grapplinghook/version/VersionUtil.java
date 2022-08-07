package de.keule.mc.grapplinghook.version;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.keule.mc.grapplinghhok.adapters.enchantment.Glow;
import de.keule.mc.grapplinghhok.adapters.enchantment.Glow_v_1_8;
import de.keule.mc.grapplinghhok.adapters.events.PlayerFischEventAdapter;
import de.keule.mc.grapplinghhok.adapters.events.PlayerFischEventAdapter_v_1_8;
import de.keule.mc.grapplinghhok.adapters.itemstack.CreateGrapplingHook;
import de.keule.mc.grapplinghhok.adapters.itemstack.CreateGrapplingHook_v_1_8;
import de.keule.mc.grapplinghhok.adapters.player.InventoryOperations;
import de.keule.mc.grapplinghhok.adapters.player.InventoryOperations_v_1_8;
import de.keule.mc.grapplinghhok.adapters.recipe.GHShapedRecipe;
import de.keule.mc.grapplinghhok.adapters.recipe.GHShapedRecipe_v_1_8;
import de.keule.mc.grapplinghhok.adapters.sound.DefaultSound_v_1_8;
import de.keule.mc.grapplinghhok.adapters.sound.DefaultSounds;
import de.keule.mc.grapplinghhok.adapters.worldguard.WorldGuardLogic;
import de.keule.mc.grapplinghhok.adapters.worldguard.WorldGuardLogic_v_1_8;
import de.keule.mc.grapplinghook.adapters.enchantment.Glow_v_1_13;
import de.keule.mc.grapplinghook.adapters.events.PlayerFischEventAdapter_v_1_13;
import de.keule.mc.grapplinghook.adapters.itemstack.CreateGrapplingHook_v_1_13;
import de.keule.mc.grapplinghook.adapters.player.InventoryOperations_v_1_13;
import de.keule.mc.grapplinghook.adapters.recipe.GHShapedRecipe_v_1_13;
import de.keule.mc.grapplinghook.adapters.sound.DefaultSound_v_1_13;
import de.keule.mc.grapplinghook.adapters.worldguard.WorldGuardLogic_v_1_13;
import de.keule.mc.grapplinghook.main.GHPlugin;

public class VersionUtil {
	private static PlayerFischEventAdapter fischEventAdapter;
	private static InventoryOperations invOperations;
	private static WorldGuardLogic worldguardLogic;
	private static GHShapedRecipe ghShapedRecipe;
	private static CreateGrapplingHook createGH;
	private static boolean unsupported = false;
	private static String fullServerVersion;
	private static boolean newApi = false;
	private static Glow glow;

	public static void versionCheck(Plugin pl) {
		fullServerVersion = Bukkit.getBukkitVersion().split("-")[0];
		int serverVersion = 13;
		try {
			serverVersion = Integer.parseInt(fullServerVersion.split("\\.")[1]);
		} catch (Exception e) {
			pl.getLogger().log(Level.SEVERE,
					"Couldn't get server version! Running plugin on new API version (1.13 and above).");
			newApi = true;
			return;
		}

		if (serverVersion >= 13) {
			newApi = true;
			GHPlugin.println("&2Server version " + fullServerVersion + " identified using new API (1.13 and above).");
			if (serverVersion > 19)
				unsupported = true;
		} else if (serverVersion >= 8 && serverVersion <= 12) {
			GHPlugin.println("&2Server version " + fullServerVersion + " identified using old API (1.8 until 1.12).");
		} else {
			unsupported = true;
			GHPlugin.println(
					"&4Unsupported server version (not tested): " + fullServerVersion + "! &7Running old API!");
		}
	}

	public static boolean isNewApi() {
		return newApi;
	}

	/* Get Version Dependent Objects */
	public static WorldGuardLogic getWorldGuardLogic(Plugin pl) {
		if (worldguardLogic != null)
			return worldguardLogic;

		if (isNewApi())
			worldguardLogic = new WorldGuardLogic_v_1_13(pl);
		else
			worldguardLogic = new WorldGuardLogic_v_1_8(pl);
		return worldguardLogic;
	}

	public static CreateGrapplingHook getCreateGrapplingHook() {
		if (createGH != null)
			return createGH;

		if (isNewApi())
			createGH = new CreateGrapplingHook_v_1_13();
		else
			createGH = new CreateGrapplingHook_v_1_8();
		return createGH;
	}

	public static InventoryOperations getInventoryOperations() {
		if (invOperations != null)
			return invOperations;

		if (isNewApi())
			invOperations = new InventoryOperations_v_1_13();
		else
			invOperations = new InventoryOperations_v_1_8();
		return invOperations;
	}

	public static PlayerFischEventAdapter getFishEventAdapter() {
		if (fischEventAdapter != null)
			return fischEventAdapter;

		if (isNewApi())
			fischEventAdapter = new PlayerFischEventAdapter_v_1_13();
		else
			fischEventAdapter = new PlayerFischEventAdapter_v_1_8();
		return fischEventAdapter;
	}

	public static GHShapedRecipe getGhShapedRecipe() {
		if (ghShapedRecipe != null)
			return ghShapedRecipe;

		if (isNewApi())
			ghShapedRecipe = new GHShapedRecipe_v_1_13();
		else
			ghShapedRecipe = new GHShapedRecipe_v_1_8();

		return ghShapedRecipe;
	}

	public static Glow getGlow() {
		if (glow != null)
			return glow;

		if (isNewApi())
			glow = new Glow_v_1_13();
		else
			glow = new Glow_v_1_8();

		return glow;
	}

	public static DefaultSounds getDefaultSounds() {
		if (isNewApi())
			return new DefaultSound_v_1_13();
		return new DefaultSound_v_1_8();
	}

	public static String getServerVersion() {
		return fullServerVersion;
	}

	public static boolean isUnsupported() {
		return unsupported;
	}
}