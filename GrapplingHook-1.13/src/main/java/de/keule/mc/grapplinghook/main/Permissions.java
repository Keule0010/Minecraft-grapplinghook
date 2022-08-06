package de.keule.mc.grapplinghook.main;

public enum Permissions {
	/* CMDs */
	TAB_COMPLETE("grapplinghook.tab.complete"), 
	HELP("grapplinghook.cmd.help"),
	RELOAD("grapplinghook.cmd.reload"),
	ALL_RODS("grapplinghook.cmd.allRods"),
	LIST("grapplinghook.cmd.list"),
	INFO("grapplinghook.cmd.info"),
	GET("grapplinghook.cmd.get"),
	GIVE("grapplinghook.cmd.give"),
	CREATE("grapplinghook.cmd.create"),
	DISPLAYNAME("grapplinghook.cmd.displayname"),
	COOLDOWN("grapplinghook.cmd.cooldown"),
	
	/* GRappling Hook */
	CRAFT("grapplinghook.craft"),
	GH_ALL_WORLDS("grapplinghook.worlds.*"),

	/* -> Grouped Permissions */
	ALL_CMD_PERM("grapplinghook.cmds.*"),
	DEFAULT_PLAYER("grapplinghook.default.player"),
	;

	private final String PERM;

	private Permissions(String PERM) {
		this.PERM = PERM;
	}

	public String getPERM() {
		return PERM;
	}
}