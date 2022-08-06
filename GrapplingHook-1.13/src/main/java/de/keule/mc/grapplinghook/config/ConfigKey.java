package de.keule.mc.grapplinghook.config;

public enum ConfigKey {
	/* Config */
	PREFIX("prefix"),
	FALL_DMG_RM("fallDamageRemove"),
	ALL_RODS("allFishingRods"),
	
	/* Messages */
	NO_PERM("noperm"),
	CMD_USAGE("usageCMD"),
	WRONG_ARGS("wrongArguments"),
	UNKOWN_CMD("unknownCMD"),
	ONLY_PLAYERS("onlyPlayers"),
	PLAYER_IS_OFFLINE("playerIsOffline"),
	SOMETHING_WENT_WRONG("somethingWentWrong"),
	OPERATION_SUCC("operationSucc"),
	OPERATION_FAIL("operationFailed"),
	NO_GRAPPLING_HOOK_FOUND("noGHFound"),
	GH_CREATE("ghCreated"),
	GH_RECEIVED("ghReceived"),
	NO_USES_LEFT("noUsesLeft"),
	CONFIG_RELOADED("reload"),
	COOLDOWN_MSG("cooldown"), 
	HELP_HEADER("helpHeader"),
	HELP_ITEM("helpItem"),
	LIST_HEADER("listHeader"),
	LIST_ITEM("listItem"),
	
	/* -> Option Changed */
	ALL_WORLDS("allWorlds"),
	SET_SOUND("setSound"),
	COOLDOWN("cooldown"),
	USE_AIR("useAir"),
	CRAFTING("crafting"),
	GLOW("glow"),
	UNBREAKABLE("unbreakable"),
	NO_FALL_DMG("noFallDamage"),
	USE_FLOATING_BLOCKS("useFloatingBlocks"),
	CANCEL_ON_ENTITY("cancelOnEntityCatch"),
	DESTROY_NO_USES("destroyWhenNoMoreUses"),
	MAX_USES("maxUses"),
	DESTROY_DELAY("destroyDelay"),
	GRAVITY("gravity"),
	THROW_SPEED_MULTI("throw_speed_multiplier"),
	PERMISSION("permission"),
	DISPLAY_NAME("displayName"),
	UNLIMITED_USES_NAME("unlimitedUsesName"),
	UNLIMITED_USES("unlimitedUses"),
	;

	
	
	public final String PATH;

	private ConfigKey(String PATH) {
		this.PATH = PATH;
	}

}