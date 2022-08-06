package de.keule.mc.grapplinghook.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
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
		if (subCmd.playerIsRequired()) {
			if (sender instanceof Player)
				p = (Player) sender;
			else {
				sender.sendMessage(getMessage(ConfigKey.ONLY_PLAYERS));
				return true;
			}
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
				ConfigManager.getConfig().set(ConfigKey.ALL_RODS, !Settings.isAllRods());
				if (ConfigManager.saveReloadAll())
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
				GrapplingHook gh = GrapplingHook.getGrapplingHook(args[1]);
				if (gh == null) {
					sender.sendMessage(
							getMessage(ConfigKey.NO_GRAPPLING_HOOK_FOUND).replace(ConfVars.GH_NAME, args[1]));
					return true;
				}

				gh.printInfo(sender);
				return true;

			case GET:
				gh = GrapplingHook.getGrapplingHook(args[1]);
				if (gh == null) {
					sender.sendMessage(
							getMessage(ConfigKey.NO_GRAPPLING_HOOK_FOUND).replace(ConfVars.GH_NAME, args[1]));
					return true;
				}
				p.getInventory().addItem(gh.getGrapplingHook());
				p.sendMessage(getMessage(ConfigKey.GH_RECEIVED).replace(ConfVars.GH_NAME, args[1]));
				return true;

			case GIVE:
				gh = GrapplingHook.getGrapplingHook(args[1]);
				if (gh == null) {
					sender.sendMessage(
							getMessage(ConfigKey.NO_GRAPPLING_HOOK_FOUND).replace(ConfVars.GH_NAME, args[1]));
					return true;
				}
				Player rec = Bukkit.getPlayer(args[2]);
				if (rec == null) {
					sender.sendMessage(getMessage(ConfigKey.PLAYER_IS_OFFLINE).replace(ConfVars.PLAYER, args[2]));
					return true;
				}

				rec.getInventory().addItem(gh.getGrapplingHook());
				rec.sendMessage(getMessage(ConfigKey.GH_RECEIVED).replace(ConfVars.GH_NAME, args[1]));
				sender.sendMessage(getMessage(ConfigKey.OPERATION_SUCC));
				return true;

			case CREATE:
				final String name = args[1].toLowerCase();
				if (!GrapplingHook.checkName(name)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Settings.getPrefix()
							+ " &cThe name of the grappling hook is incorrect! &7You can only use the following characters(&cmax. 10&7):&a a-z 0-9 _ -"));
					return true;
				}

				if (GrapplingHook.createNew(name))
					sender.sendMessage(getMessage(ConfigKey.GH_CREATE).replace(ConfVars.GH_NAME, name));
				else
					sender.sendMessage(getMessage(ConfigKey.SOMETHING_WENT_WRONG));
				return true;

			case DISPLAYNAME:
				return true;

			case COOLDOWN:

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

		final int startIndex = listPage * displayItems;

		for (int i = startIndex; i < startIndex + displayItems && i < GrapplingHook.getGrapplingHooks().size(); i++) {
			final GrapplingHook gh = GrapplingHook.getGrapplingHooks().get(i);
			sender.sendMessage(getMessage(ConfigKey.LIST_ITEM).replace(ConfVars.GH_NAME, gh.getConfigPath())
					.replace(ConfVars.DISPLAY_NAME, gh.getDisplayName()));
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

//	private String getString(String[] args, int offset, int end) {
//		StringBuilder str = new StringBuilder();
//		for (int i = offset; i < args.length; i++) {
//			if (end != -1 && i >= end)
//				break;
//
//			str.append(args[i]);
//			str.append(" ");
//		}
//		return str.substring(0, str.length() - 1);
//	}
//
	private static int getInt(String s, int def) {
		if (s == null)
			return def;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command command, String label, String[] args) {
		final List<String> commands = new ArrayList<>();
		if (!s.isOp() && !s.hasPermission(Permissions.ALL_CMD_PERM.getPERM())
				&& !s.hasPermission(Permissions.TAB_COMPLETE.getPERM()))
			return commands;

		if (args.length == 1) {
			final String input = args[0].toLowerCase();
			for (SubCommand subCmd : SubCommand.values()) {
				if (subCmd.getCmd().startsWith(input) && s.hasPermission(subCmd.getPermission().getPERM()))
					commands.add(subCmd.getCmd());
			}
			return commands;
		}

		if (args.length == 2) {
			SubCommand cmd = SubCommand.getCommand(args[0]);
			if (cmd != null && (s.isOp() || s.hasPermission(cmd.getPermission().getPERM()))) {
				for (GrapplingHook gh : GrapplingHook.getGrapplingHooks())
					commands.add(gh.getConfigPath());
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
	DISPLAYNAME("displayName", false, Permissions.DISPLAYNAME, 2, "<GrapplingHookName> <DisplayName>"),
	COOLDOWN("cooldown", false, Permissions.COOLDOWN, 2, "<GrapplingHookName> <CoolDown>"),;

	public static final String CMD = "/GrapplingHook";
	private static List<String> subCommands;
	private final Permissions permission;
	private final boolean player;
	private final String subCmd;
	private final String usage;
	private final int args;

	private SubCommand(String subCmd, boolean player, Permissions perm, int args, String usage) {
		this.permission = perm;
		this.player = player;
		this.subCmd = subCmd.toLowerCase();
		this.args = args;

		usage = "%cmd% %subCmd% " + (usage == null ? "" : usage);
		this.usage = ChatColor.translateAlternateColorCodes('&',
				usage.replace(ConfVars.CMD, CMD).replace(ConfVars.SUB_CMD, subCmd));
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
		for (SubCommand cmd : values()) {
			if (cmd.getCmd().equalsIgnoreCase(subCmd))
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