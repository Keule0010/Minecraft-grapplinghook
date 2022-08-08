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
	REMOVE("grapplinghook.cmd.remove"),
	/* -> Grappling Hook Settings */
	ALL_WORLDS("grapplinghook.cmd.allWorlds"),
	ADD_WORLD("grapplinghook.cmd.addWorld"),
	RM_WORLD("grapplinghook.cmd.removeWorld"),
	SET_SOUND("grapplinghook.cmd.setSound"),
	COOLDOWN("grapplinghook.cmd.cooldown"),
	USE_AIR("grapplinghook.cmd.useAir"),
	CRAFTING("grapplinghook.cmd.crafting"),
	GLOW("grapplinghook.cmd.glow"),
	UNBREAKABLE("grapplinghook.cmd.unbreakable"),
	NO_FALL_DAMAGE("grapplinghook.cmd.noFallDamage"),
	USE_FLOATING_BLOCKS("useFloatingBlocks"),
	CANCEL_ON_ENTITY("grapplinghook.cmd.cancelOnEntityCatch"),
	DESTROY_NO_USES("grapplinghook.cmd.destroyWhenNoMoreUses"),
	MAX_USES("grapplinghook.cmd.maxUses"),
	DESTROY_DELAY("grapplinghook.cmd.destroyDelay"),
	GRAVITY("grapplinghook.cmd.gravity"),
	THROW_SPEED_MULTI("grapplinghook.cmd.multiplier"),
	PERMISSION("grapplinghook.cmd.permission"),
	DISPLAY_NAME("grapplinghook.cmd.displayName"),
	UNLIMITED_NAME("grapplinghook.cmd.unlimitedName"),
	UNLIMITED("grapplinghook.cmd.unlimited"),
	
	/* Grappling Hook */
	CRAFT("grapplinghook.craft"),
	GH_ALL_WORLDS("grapplinghook.worlds.*"),

	/* -> Grouped Permissions */
	ALL_CMD_PERM("grapplinghook.cmds.*"),
	DEFAULT_PLAYER("grapplinghook.player.default"),
	;

	private final String PERM;
	
	private Permissions(String PERM) {
		this.PERM = PERM;
	}

	public String getPERM() {
		return PERM;
	}
}