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
	GH_RECEIVED("ghReceived"),
	NO_USES_LEFT("noUsesLeft"),
	CONFIG_RELOADED("reload"),
	COOLDOWN_MSG("cooldown"),
	;

	
	
	public final String PATH;

	private ConfigKey(String PATH) {
		this.PATH = PATH;
	}

}