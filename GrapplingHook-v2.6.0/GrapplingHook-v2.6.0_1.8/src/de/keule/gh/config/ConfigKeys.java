package de.keule.gh.config;

import java.util.Arrays;

public enum ConfigKeys {
	PREFIX("Plugin.prefix", "&7[&6GrapplingHook&7]"), NAME("Plugin.name", "&6Grappling hook"),
	UNLIMITED_NAME("Plugin.unlimitedUsesName", "unlimited"), UNLIMTED_USES("Plugin.unlimitedUses", false),
	USE_AIR("Plugin.useAir", false), ALL_RODS("Plugin.allRods", false), CRAFTING("Plugin.crafting", false),
	ENCHANTED("Plugin.enchanted", false), UNBREAKABLE("Plugin.unbreakable", false),
	NO_FALL_DAMAGE("Plugin.noFallDamage", false), USE_ALL_WORLDS("Plugin.useInAllWorlds", false),
	BLOCK_FLOATING("Plugin.useFloatingBlocks", true), CANCEL_ON_ENTITY_CATCH("Plugin.cancleOnEntityCatch", true),
	DESTROY_NO_MORE_UESE("Plugin.destroyWhenNoMoreUses", false), MAX_USES("Plugin.maxUses", 10),
	DESTROY_DELAY("Plugin.destroyDelay", 10), COOLDOWN("Plugin.cooldown", 2D), GRAVITY("Plugin.gravity", 0.35D),
	SPEED_MULTIPLIER("Plugin.speedMultiplier", 0.25D), FALL_DAMAGE_RM("Plugin.fallDamageRemove", 1.25D),
	SOUND("Plugin.pullSound", "ENDERMAN_TELEPORT"),
	DESTROY_SOUND("Plugin.destroySound", "ITEM_BREAK"),

	RELOAD_MSG("Messages.reload", "%prefix% &7Config reloaded!"),
	SUCCESSFUL("Messages.successful", "%prefix% &2Operetion successful done."),
	NO_PERMS_MSG("Messages.noperm", "%prefix% &cYou have no permissions to do that."),
	UNKOWN_CMD_MSG("Messages.unkownCMD", "%prefix% &7Unknown command or wrong syntax."),
	UNKOWN_PLAYER_MSG("Messages.unkownPlayer", "%prefix% &cPlayer: &7%name%&c is unkown or offline!"),
	ONLY_NUMBERS_MSG("Messages.onlyNumbers", "%prefix% &cOnly numbers!"),
	ADD_WORLD_MSG("Messages.addWorld", "%prefix% &2The world: &7%world%&2 has been added to the list."),
	RM_WORLD_MSG("Messages.removeWorld", "%prefix% &cThe world: &7%world% &chas been removed from the list."),
	ALREADY_IN_LIST_MSG("Messages.alreadyInList", "%prefix% &7World: %world% is already in the list."),
	NOT_IN_LIST_MSG("Messages.isNotInList", "%prefix% &7World: %world% isn't in the list."),
	COOLDOWN_MSG("Messages.cooldown", "%prefix% &7One Moment!(%timeLeft%s)"),
	VALUE_CHANGED("Messages.valueChanged", "%prefix% &7You changed the value of &e%option%&7 to: %newValue%"),
	ITEM_NOT_IN_HAND("Messages.itemNotInHand", "%prefix% &cThe player has the grapplinghook not in his hand!"),

	RECIEP_SLOT1("Recipe.slot1", ""), RECIEP_SLOT2("Recipe.slot2", ""), RECIEP_SLOT3("Recipe.slot3", "STICK"),
	RECIEP_SLOT4("Recipe.slot4", ""), RECIEP_SLOT5("Recipe.slot5", "STICK"), RECIEP_SLOT6("Recipe.slot6", "STRING"),
	RECIEP_SLOT7("Recipe.slot7", "STICK"), RECIEP_SLOT8("Recipe.slot8", ""), RECIEP_SLOT9("Recipe.slot9", "IRON_INGOT"),

	LORE("Lore", Arrays.asList(new String[] { "&eYou have: &c%uses%&e uses left" })),
	WORLDS("Worlds", Arrays.asList(new String[] { "" }));

	public final String KEY;
	public final Object DEFAULT_VALUE;

	private ConfigKeys(String KEY, Object DEFAULT_VALUE) {
		this.KEY = KEY;
		this.DEFAULT_VALUE = DEFAULT_VALUE;
	}
}

//USE_ALL_WORLDS_MSG("Messages.useInAllWorlds", "%prefix%&7Use in all worlds is now: %state%."),
//SET_SOUND_MSG("Messages.setSound", "%prefix%&7You set the sound to: %sound%"),
//SET_COOLDOWN_MSG("Messages.setCooldown", "%prefix%&7New cooldown: %newValue%s"),
//SET_USE_AIR_MSG("Messages.useAir", "%prefix%&7UseAir is now: %state%"),
//SET_CRAFTING_MSG("Messages.crafting", "%prefix%&7Crafting is now: %state%"),
//SET_USES_MSG("Messages.setUses", "%prefix%&7You set the uses to: %newValue%"),
//SET_GRAVITY_MSG("Messages.setGravity", "%prefix%&7New gravity value: %newValue%"),
//SET_SPEED_MSG("Messages.setSpeed", "%prefix%&7New speed multiplier: %newValue%"),
//USES_LEFT_MSG("Messages.usesLeft", "&cUses left"),