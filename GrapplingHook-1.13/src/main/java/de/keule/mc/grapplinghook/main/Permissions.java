package de.keule.mc.grapplinghook.main;

public enum Permissions {
	/* CMDs */
	ALL_CMD_PERM("grapplinghook.cmds.*"), 
	TAB_COMPLETE("grapplinghook.tab.complete"), 
	HELP("grapplinghook.cmd.help"),
	RELOAD("grapplinghook.cmd.reload"),
	LIST("grapplinghook.cmd.list"),
	INFO("grapplinghook.cmd.info"),
	GET("grapplinghook.cmd.get"),
	GIVE("grapplinghook.cmd.give"),
	CREATE("grapplinghook.cmd.create"),
	DISPLAYNAME("grapplinghook.cmd.displayname"),
	
	CRAFT("grapplinghook.craft"),
	GH_ALL_WORLDS("grapplinghook.worlds.*"),

	;

	private final String PERM;

	private Permissions(String PERM) {
		this.PERM = PERM;
	}

	public String getPERM() {
		return PERM;
	}
}