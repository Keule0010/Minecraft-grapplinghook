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
	INVALID_VALUE("invalidValue"),
	OPERATION_SUCC("operationSucc"),
	NO_GRAPPLING_HOOK_FOUND("noGHFound"),
	GH_CREATE("ghCreated"),
	GH_REMOVED("ghRemoved"),
	GH_RECEIVED("ghReceived"),
	NO_USES_LEFT("noUsesLeft"),
	CONFIG_RELOADED("reload"),
	COOLDOWN_MSG("cooldownMsg"), 
	HELP_HEADER("helpHeader"),
	HELP_ITEM("helpItem"),
	LIST_HEADER("listHeader"),
	LIST_ITEM("listItem"),
	
	/* -> Option Changed */
	ADD_WORLD("addWorld"),
	RM_WORLD("removeWorld"),
	ALREADY_IN_LIST("alreadyInList"),
	IS_NOT_IN_LIST("isNotInList"),
	ALL_WORLDS("allWorlds"),
	PULL_SOUND("setSound", "pullSound"),
	BREAK_SOUND("setSound", "breakSound"),
	COOLDOWN("cooldown"),
	USE_AIR("useAir"),
	CRAFTING("crafting"),
	GLOW("glow"),
	UNBREAKABLE("unbreakable"),
	NO_FALL_DAMAGE("noFallDamage"),
	USE_FLOATING_BLOCKS("useFloatingBlocks"),
	CANCEL_ON_ENTITY("cancelOnEntityCatch"),
	DESTROY_NO_USES("destroyWhenNoMoreUses"),
	MAX_USES("maxUses"),
	DESTROY_DELAY("destroyDelay"),
	GRAVITY("gravity", ".gravity"),
	THROW_SPEED_MULTI("throw_speed_multiplier"),
	PERMISSION("permission"),
	DISPLAY_NAME("displayName"),
	UNLIMITED_USES_NAME("unlimitedUsesName"),
	UNLIMITED_USES("unlimitedUses"),
	;

	public final String PATH;
	private final String GH_PATH;

	private ConfigKey(String PATH) {
		this.PATH = PATH;
		this.GH_PATH = null;
	}
	private ConfigKey(String PATH, String GH_PATH) {
		this.PATH = PATH;
		this.GH_PATH = GH_PATH;
	}
	
	public String getGH_PATH() {
		if(GH_PATH == null)
			return PATH;
		return GH_PATH;
	}
}