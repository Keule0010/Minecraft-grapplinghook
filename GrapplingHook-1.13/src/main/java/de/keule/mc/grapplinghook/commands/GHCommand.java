package de.keule.mc.grapplinghook.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.keule.mc.grapplinghook.config.ConfVars;
import de.keule.mc.grapplinghook.config.ConfigKey;
import de.keule.mc.grapplinghook.config.ConfigManager;
import de.keule.mc.grapplinghook.config.Settings;
import de.keule.mc.grapplinghook.main.GHPlugin;
import de.keule.mc.grapplinghook.main.GrapplingHook;
import de.keule.mc.grapplinghook.main.Permissions;
import net.md_5.bungee.api.ChatColor;

public class GHCommand implements CommandExecutor, TabCompleter {
	private GHPlugin pl;

	public GHCommand(GHPlugin pl) {
		this.pl = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return true;
		}

		final SubCommand subCmd = SubCommand.getCommand(args[0]);

		if (subCmd == null) {
			sender.sendMessage(getMessage(ConfigKey.UNKOWN_CMD));
			return true;
		}

		if (!sender.isOp() && !sender.hasPermission(Permissions.ALL_CMD_PERM.getPERM())
				&& !sender.hasPermission(subCmd.getPermission().getPERM())) {
			sender.sendMessage(getMessage(ConfigKey.NO_PERM));
			return true;
		}

		if (args.length - 1 < subCmd.getMinArgs()) {
			sender.sendMessage(getMessage(ConfigKey.WRONG_ARGS));
			if (subCmd.getUsage() != null && !subCmd.getUsage().isEmpty())
				sender.sendMessage(getMessage(ConfigKey.CMD_USAGE).replace(ConfVars.USAGE,
						ChatColor.translateAlternateColorCodes('&', subCmd.getUsage())));
			return true;
		}

		Player p = null;
		if (sender instanceof Player)
			p = (Player) sender;

		if (subCmd.playerIsRequired() && p == null) {
			sender.sendMessage(getMessage(ConfigKey.ONLY_PLAYERS));
			return true;
		}

		try {
			switch (subCmd) {
			case HELP:
				int helpPage = 0;
				if (args.length == 2)
					helpPage = getInt(args[1], 1) - 1;
				displayHelp(sender, helpPage);
				return true;

			case RELOAD:
				if (ConfigManager.reloadAll())
					sender.sendMessage(getMessage(ConfigKey.CONFIG_RELOADED));
				else
					sender.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));
				return true;

			case ALL_RODS:
				boolean newState = !Settings.isAllRods();
				if (args.length == 2)
					newState = getBoolean(args[1], newState);

				ConfigManager.getConfig().set(ConfigKey.ALL_RODS, newState);
				if (ConfigManager.saveConfigReloadAll())
					sender.sendMessage(
							getMessage(ConfigKey.ALL_RODS).replace(ConfVars.NEW_VALUE, Settings.isAllRods() + ""));
				else
					sender.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));
				return true;

			case LIST:
				int listPage = 0;
				if (args.length == 2)
					listPage = getInt(args[1], 1) - 1;
				displayGrapplingHooks(sender, listPage);
				return true;

			case INFO:
				GrapplingHook gh = getGrapplingHook(sender, args[1]);
				if (gh != null)
					gh.printInfo(sender);
				return true;

			case GET:
				gh = getGrapplingHook(sender, args[1]);
				if (gh != null) {
					p.getInventory().addItem(gh.getGrapplingHook());
					p.sendMessage(getMessage(ConfigKey.GH_RECEIVED).replace(ConfVars.GH_NAME, args[1]));
				}
				return true;

			case GIVE:
				gh = getGrapplingHook(sender, args[1]);
				if (gh == null)
					return true;
				Player rec = Bukkit.getPlayer(args[2]);
				if (rec == null) {
					sender.sendMessage(getMessage(ConfigKey.PLAYER_IS_OFFLINE).replace(ConfVars.PLAYER, args[2]));
					return true;
				}

				rec.getInventory().addItem(gh.getGrapplingHook());
				rec.sendMessage(getMessage(ConfigKey.GH_RECEIVED).replace(ConfVars.GH_NAME, gh.getName())
						.replace(ConfVars.DISPLAY_NAME, gh.getDisplayName()));
				sender.sendMessage(getMessage(ConfigKey.OPERATION_SUCC));
				return true;

			case CREATE:
				createGH(sender, args[1].toLowerCase());
				return true;

			case REMOVE:
				gh = getGrapplingHook(sender, args[1]);
				if (gh == null)
					return true;
				ConfigManager.getGrapplingHookConfig().remove(gh.getName());
				if (ConfigManager.saveGHConfigReloadAll())
					sender.sendMessage(getMessage(ConfigKey.GH_REMOVED).replace(ConfVars.GH_NAME, gh.getName()));
				else
					sender.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));
				return true;

			case ALL_WORLDS:
				setGHValue(ConfigKey.ALL_WORLDS, sender, args, getBoolean(args[2], false));
				return true;

			case ADD_WORLD:
				addWorld(sender, args);
				return true;

			case RM_WORLD:
				rmWorld(sender, args);
				return true;

			case BREAK_SOUND:
				setGHValue(ConfigKey.BREAK_SOUND, sender, args, getSound(args[2], null));
				return true;

			case PULL_SOUND:
				setGHValue(ConfigKey.PULL_SOUND, sender, args, getSound(args[2], null));
				return true;

			case CANCEL_ON_ENTITY:
				setGHValue(ConfigKey.CANCEL_ON_ENTITY, sender, args, getBoolean(args[2], false));
				return true;

			case COOLDOWN:
				int cooldown = getInt(args[2], -1);
				setGHValue(ConfigKey.COOLDOWN, sender, args, cooldown < 0 ? null : cooldown);
				return true;

			case CRAFTING:
				setGHValue(ConfigKey.CRAFTING, sender, args, getBoolean(args[2], false));
				return true;

			case DESTROY_DELAY:
				int destroyDelay = getInt(args[2], -1);
				setGHValue(ConfigKey.DESTROY_DELAY, sender, args, destroyDelay < 0 ? null : destroyDelay);
				return true;

			case DESTROY_NO_USES:
				setGHValue(ConfigKey.DESTROY_NO_USES, sender, args, getBoolean(args[2], false));
				return true;

			case DISPLAY_NAME:
				setGHValue(ConfigKey.DISPLAY_NAME, sender, args, getString(args, 2));
				return true;

			case GLOW:
				setGHValue(ConfigKey.GLOW, sender, args, getBoolean(args[2], false));
				return true;

			case GRAVITY:
				double gravity = getDouble(args[2], -1);
				setGHValue(ConfigKey.GRAVITY, sender, args, gravity < 0 ? null : gravity);
				return true;

			case THROW_SPEED_MULTI:
				double multiplier = getDouble(args[2], -1);
				setGHValue(ConfigKey.THROW_SPEED_MULTI, sender, args, multiplier < 0 ? null : multiplier);
				return true;

			case MAX_USES:
				int maxUses = getInt(args[2], -1);
				setGHValue(ConfigKey.MAX_USES, sender, args, maxUses < 0 ? null : maxUses);
				return true;

			case NO_FALL_DAMAGE:
				setGHValue(ConfigKey.NO_FALL_DAMAGE, sender, args, getBoolean(args[2], false));
				return true;

			case PERMISSION:
				setGHValue(ConfigKey.PERMISSION, sender, args, getString(args, 2, 3));
				return true;

			case UNBREAKABLE:
				setGHValue(ConfigKey.UNBREAKABLE, sender, args, getBoolean(args[2], false));
				return true;

			case UNLIMITED:
				setGHValue(ConfigKey.UNLIMITED_USES, sender, args, getBoolean(args[2], false));
				return true;

			case UNLIMITED_NAME:
				setGHValue(ConfigKey.UNLIMITED_USES_NAME, sender, args, getString(args, 2));
				return true;

			case USE_AIR:
				setGHValue(ConfigKey.USE_AIR, sender, args, getBoolean(args[2], false));
				return true;

			case USE_FLOATING_BLOCKS:
				setGHValue(ConfigKey.USE_FLOATING_BLOCKS, sender, args, getBoolean(args[2], false));
				return true;

			default:
				sender.sendMessage(getMessage(ConfigKey.UNKOWN_CMD));
				return true;
			}
		} catch (Exception e) {
			sender.sendMessage(ConfigManager.getMsgConfig().getMessage(ConfigKey.SOMETHING_WENT_WRONG));
			pl.getLogger().log(Level.WARNING, "Something went wrong while executing command: " + SubCommand.CMD + " "
					+ Arrays.toString(args) + ", Executor: " + sender.getName(), e);
		}
		return true;
	}

	/* Commands */
	private void createGH(CommandSender sender, String name) {
		if (!GrapplingHook.checkName(name)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Settings.getPrefix()
					+ " &cThe name of the grappling hook is incorrect! &7You can only use the following characters (&cmax. 10&7):&a a-z 0-9 _ -"));
			return;
		}

		if (GrapplingHook.createNew(name))
			sender.sendMessage(getMessage(ConfigKey.GH_CREATE).replace(ConfVars.GH_NAME, name));
		else
			sender.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));
	}

	private void addWorld(CommandSender s, String[] args) {
		GrapplingHook gh = getGrapplingHook(s, args[1]);
		if (gh == null)
			return;

		String world;
		if (args.length == 3)
			world = args[2];
		else if (args.length == 2 && s instanceof Player)
			world = ((Player) s).getLocation().getWorld().getName();
		else {
			s.sendMessage(getMessage(ConfigKey.WRONG_ARGS));
			return;
		}

		if (gh.getWorlds().contains(world)) {
			s.sendMessage(getMessage(ConfigKey.ALREADY_IN_LIST).replace(ConfVars.GH_NAME, gh.getName())
					.replace(ConfVars.WORLD, world));
			return;
		}

		gh.getWorlds().add(world);
		ConfigManager.getGrapplingHookConfig().set(gh.getName() + ".worlds", gh.getWorlds());
		if (ConfigManager.saveGHConfig())
			s.sendMessage(getMessage(ConfigKey.ADD_WORLD).replace(ConfVars.GH_NAME, gh.getName())
					.replace(ConfVars.WORLD, world));
		else
			s.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));
	}

	private void rmWorld(CommandSender s, String[] args) {
		GrapplingHook gh = getGrapplingHook(s, args[1]);
		if (gh == null)
			return;

		String world;
		if (args.length == 3)
			world = args[2];
		else if (args.length == 2 && s instanceof Player)
			world = ((Player) s).getLocation().getWorld().getName();
		else {
			s.sendMessage(getMessage(ConfigKey.WRONG_ARGS));
			return;
		}

		if (!gh.getWorlds().contains(world)) {
			s.sendMessage(getMessage(ConfigKey.IS_NOT_IN_LIST).replace(ConfVars.GH_NAME, gh.getName())
					.replace(ConfVars.WORLD, world));
			return;
		}

		gh.getWorlds().remove(world);
		ConfigManager.getGrapplingHookConfig().set(gh.getName() + ".worlds", gh.getWorlds());
		if (ConfigManager.saveGHConfig())
			s.sendMessage(getMessage(ConfigKey.RM_WORLD).replace(ConfVars.GH_NAME, gh.getName()).replace(ConfVars.WORLD,
					world));
		else
			s.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));
	}

	private void displayGrapplingHooks(CommandSender sender, int listPage) {
		final int displayItems = 5;

		int maxPages = GrapplingHook.getGrapplingHooks().size();
		maxPages = calcMaxPages(displayItems, maxPages);

		if (listPage < 0)
			listPage = 0;
		if (listPage >= maxPages)
			listPage = maxPages - 1;

		sender.sendMessage(getMessage(ConfigKey.LIST_HEADER).replace("%page%", (listPage + 1) + "")
				.replace("%maxPages%", maxPages + ""));

		if (maxPages <= 0)
			return;
		final int startIndex = listPage * displayItems;
		for (int i = startIndex; i < startIndex + displayItems && i < GrapplingHook.getGrapplingHooks().size(); i++) {
			final GrapplingHook gh = GrapplingHook.getGrapplingHooks().get(i);
			sender.sendMessage(getMessage(ConfigKey.LIST_ITEM).replace(ConfVars.GH_NAME, gh.getName())
					.replace(ConfVars.DISPLAY_NAME, gh.getDisplayName() == null ? "" : gh.getDisplayName()));
		}
	}

	private void displayHelp(CommandSender sender, int helpPage) {
		final int displayItems = 5;

		int maxPages = 0;

		// Calculate max pages based on permission level
		for (SubCommand cmd : SubCommand.values()) {
			if (sender.isOp() || sender.hasPermission(Permissions.ALL_CMD_PERM.getPERM())
					|| sender.hasPermission(cmd.getPermission().getPERM()))
				maxPages++;
		}
		maxPages = calcMaxPages(displayItems, maxPages);

		if (helpPage < 0)
			helpPage = 0;
		if (helpPage >= maxPages)
			helpPage = maxPages - 1;

		sender.sendMessage(getMessage(ConfigKey.HELP_HEADER).replace("%page%", (helpPage + 1) + "")
				.replace("%maxPages%", maxPages + ""));
		if (maxPages <= 0)
			return;

		final int startIndex = helpPage * displayItems;
		for (int i = startIndex; i < startIndex + displayItems && i < SubCommand.values().length; i++) {
			final SubCommand subCommand = SubCommand.values()[i];
			if (sender.isOp() || sender.hasPermission(subCommand.getPermission().getPERM())
					|| sender.hasPermission(Permissions.ALL_CMD_PERM.getPERM())) {
				sender.sendMessage(getMessage(ConfigKey.HELP_ITEM).replace(ConfVars.SUB_CMD, subCommand.getCmd())
						.replace(ConfVars.USAGE, subCommand.getUsage()));
			}
		}
	}

	/* Helpers */
	private GrapplingHook getGrapplingHook(CommandSender sender, String name) {
		GrapplingHook gh = GrapplingHook.getGrapplingHook(name);
		if (gh == null)
			sender.sendMessage(getMessage(ConfigKey.NO_GRAPPLING_HOOK_FOUND).replace(ConfVars.GH_NAME, name));
		return gh;
	}

	private void setGHValue(ConfigKey option, CommandSender sender, String[] args, Object newValue) {
		GrapplingHook gh = getGrapplingHook(sender, args[1]);
		if (gh == null)
			return;

		if (newValue == null) {
			sender.sendMessage(getMessage(ConfigKey.INVALID_VALUE).replace(ConfVars.GH_NAME, gh.getName())
					.replace(ConfVars.VALUE, tr(args[2])));
			return;
		}

		final boolean isPrimitive = isPrimitive(newValue);
		ConfigManager.getGrapplingHookConfig().set(gh.getName() + "." + option.getGH_PATH(),
				(isPrimitive ? newValue : newValue.toString()));

		if (ConfigManager.saveGHConfigReloadAll())
			sender.sendMessage(getMessage(option).replace(ConfVars.GH_NAME, gh.getName()).replace(ConfVars.NEW_VALUE,
					tr(isPrimitive ? newValue + "" : newValue.toString())));
		else
			sender.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));

	}

	private boolean isPrimitive(Object newValue) {
		if (newValue == null)
			return false;

		Class<?> clazz = newValue.getClass();
		if (clazz == Boolean.class || clazz == Double.class || clazz == Integer.class || clazz == Float.class
				|| clazz == Long.class || clazz == Byte.class || clazz == Short.class || clazz == Character.class)
			return true;
		return false;
	}

	private int calcMaxPages(final int displayItems, int items) {
		int pages = items / displayItems;
		if (items % displayItems != 0)
			pages++;
		items = pages;
		return items;
	}

	private String getMessage(ConfigKey key) {
		return ConfigManager.getMsgConfig().getMessage(key);
	}

	private String tr(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	private String getString(String[] args, int offset) {
		return getString(args, offset, -1);
	}

	private String getString(String[] args, int offset, int end) {
		if (offset >= args.length)
			return null;

		StringBuilder str = new StringBuilder();
		for (int i = offset; i < args.length; i++) {
			if (end != -1 && i >= end)
				break;

			str.append(args[i]);
			str.append(" ");
		}
		if (str.isEmpty())
			return null;
		return str.substring(0, str.length() - 1);
	}

	private static int getInt(String s, int def) {
		if (s == null)
			return def;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	private static double getDouble(String s, double def) {
		if (s == null)
			return def;

		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	private static boolean getBoolean(String s, boolean def) {
		if (s == null)
			return def;

		try {
			return Boolean.parseBoolean(s);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	private static Sound getSound(String s, Sound def) {
		if (s == null)
			return def;

		s = s.toUpperCase();
		try {
			return Sound.valueOf(s);
		} catch (IllegalArgumentException e) {
		}
		return def;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command command, String label, String[] args) {
		final List<String> commands = new ArrayList<>();
		if (!s.isOp() && !(s.hasPermission(Permissions.ALL_CMD_PERM.getPERM())
				|| s.hasPermission(Permissions.TAB_COMPLETE.getPERM())))
			return commands;

		if (args.length == 1) {
			final String input = args[0].toLowerCase();
			for (SubCommand subCmd : SubCommand.values()) {
				if (subCmd.getCmdLowerCase().startsWith(input)
						&& (s.isOp() || s.hasPermission(subCmd.getPermission().getPERM())
								|| s.hasPermission(Permissions.ALL_CMD_PERM.getPERM())))
					commands.add(subCmd.getCmd());
			}
			return commands;
		}

		if (args.length == 2) {
			SubCommand cmd = SubCommand.getCommand(args[0]);
			if (cmd != null && (s.isOp() || s.hasPermission(cmd.getPermission().getPERM()))) {
				for (GrapplingHook gh : GrapplingHook.getGrapplingHooks())
					commands.add(gh.getName());
				return commands;
			}
		}

		if (args.length == 3)
			return null; /* Return player list */

		return commands;
	}
}

enum SubCommand {
	HELP("help", false, Permissions.HELP, 0, null), RELOAD("reload", false, Permissions.RELOAD, 0, null),
	ALL_RODS("allRods", false, Permissions.ALL_RODS, 0, null), LIST("list", false, Permissions.LIST, 0, "<Page>"),
	INFO("info", false, Permissions.INFO, 1, "<GrapplingHookName>"),
	GET("get", true, Permissions.GET, 1, "<GrapplingHookName>"),
	GIVE("give", false, Permissions.GIVE, 2, "<GrapplingHookName> <PlayerName>"),
	CREATE("create", false, Permissions.CREATE, 1, "<GrapplingHookName>"),
	REMOVE("remove", false, Permissions.REMOVE, 1, "<GrapplingHookName>"),
	/* Grappling Hook Settings */
	ALL_WORLDS("allWorlds", false, Permissions.ALL_WORLDS, 2, "<GrapplingHookName> <true/false>"),
	ADD_WORLD("addWorld", false, Permissions.ADD_WORLD, 1, "<GrapplingHookName> <WorldName>"),
	RM_WORLD("rmWorld", false, Permissions.RM_WORLD, 1, "<GrapplingHookName> <WorldName>"),
	BREAK_SOUND("breakSound", false, Permissions.SET_SOUND, 2, "<GrapplingHookName> <BreakSound>"),
	PULL_SOUND("pullSound", false, Permissions.SET_SOUND, 2, "<GrapplingHookName> <PullSound>"),
	COOLDOWN("cooldown", false, Permissions.COOLDOWN, 2, "<GrapplingHookName> <Seconds:Number>"),
	USE_AIR("useAir", false, Permissions.USE_AIR, 2, "<GrapplingHookName> <true/false>"),
	CRAFTING("crafting", false, Permissions.CRAFTING, 2, "<GrapplingHookName> <true/false>"),
	GLOW("glow", false, Permissions.GLOW, 2, "<GrapplingHookName> <true/false>"),
	UNBREAKABLE("unbreakable", false, Permissions.UNBREAKABLE, 2, "<GrapplingHookName> <true/false>"),
	NO_FALL_DAMAGE("noFallDamage", false, Permissions.NO_FALL_DAMAGE, 2, "<GrapplingHookName> <true/false>"),
	USE_FLOATING_BLOCKS("useFloatingBlocks", false, Permissions.USE_FLOATING_BLOCKS, 2,
			"<GrapplingHookName> <true/false>"),
	CANCEL_ON_ENTITY("cacnelEntityCatch", false, Permissions.CANCEL_ON_ENTITY, 2, "<GrapplingHookName> <true/false>"),
	DESTROY_NO_USES("destroyNoUses", false, Permissions.DESTROY_NO_USES, 2, "<GrapplingHookName> <true/false>"),
	MAX_USES("maxUses", false, Permissions.MAX_USES, 2, "<GrapplingHookName> <MaxUses>"),
	DESTROY_DELAY("destroyDelay", false, Permissions.DESTROY_DELAY, 2, "<GrapplingHookName> <Ticks:Number>"),
	GRAVITY("gravity", false, Permissions.GRAVITY, 2, "<GrapplingHookName> <Gravity:Decimal>"),
	THROW_SPEED_MULTI("multiplier", false, Permissions.THROW_SPEED_MULTI, 2,
			"<GrapplingHookName> <Multiplier:Decimal>"),
	PERMISSION("permission", false, Permissions.PERMISSION, 2, "<GrapplingHookName> <Permission:PlainText>"),
	DISPLAY_NAME("displayName", false, Permissions.DISPLAY_NAME, 2, "<GrapplingHookName> <DisplayName:Text>"),
	UNLIMITED_NAME("unlimitedName", false, Permissions.UNLIMITED_NAME, 2, "<GrapplingHookName> <UnlimitedName:Text>"),
	UNLIMITED("unlimited", false, Permissions.UNLIMITED, 2, "<GrapplingHookName> <true/false>"),;

	public static final String CMD = "/GrapplingHook";
	private static List<String> subCommands;
	private final Permissions permission;
	private final String subCmdLower;
	private final boolean player;
	private final String subCmd;
	private final String usage;
	private final int args;

	private SubCommand(String subCmd, boolean player, Permissions perm, int args, String usage) {
		this.permission = perm;
		this.player = player;
		this.subCmd = subCmd;
		this.subCmdLower = subCmd.toLowerCase();
		this.args = args;

		usage = "%cmd% %subCmd% " + (usage == null ? "" : usage);
		this.usage = ChatColor.translateAlternateColorCodes('&',
				usage.replace(ConfVars.CMD, CMD).replace(ConfVars.SUB_CMD, subCmd));
	}

	public String getCmdLowerCase() {
		return subCmdLower;
	}

	public String getCmd() {
		return subCmd;
	}

	public int getMinArgs() {
		return args;
	}

	public Permissions getPermission() {
		return permission;
	}

	public String getUsage() {
		return usage;
	}

	public boolean playerIsRequired() {
		return player;
	}

	/* Static */
	public static SubCommand getCommand(String subCmd) {
		subCmd = subCmd.toLowerCase();
		for (SubCommand cmd : values()) {
			if (cmd.getCmdLowerCase().equals(subCmd))
				return cmd;
		}
		return null;
	}

	public List<String> getSubCommandStringList() {
		if (subCommands != null)
			return subCommands;

		subCommands = new ArrayList<>();
		for (SubCommand cmd : values())
			subCommands.add(cmd.getCmd());
		return subCommands;
	}
}