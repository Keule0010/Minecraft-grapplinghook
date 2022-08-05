package de.keule.grapplinghook.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import de.keule.grapplinghook.cooldown.Cooldown;
import de.keule.grapplinghook.cooldown.NoFallDamage;
import de.keule.grapplinghook.crafting.GrapplingHookRecipe;
import de.keule.grapplinghook.listener.CraftListener;
import de.keule.grapplinghook.listener.DamageListener;
import de.keule.grapplinghook.listener.Grapplinghook1_13_above;
import de.keule.grapplinghook.listener.Grapplinghook1_13_above_WorldGuard;
import de.keule.grapplinghook.updateChecker.UpdateChecker;
import de.keule.grapplinghook.worldGuard.AddWorldGuardFlag;

public class Main extends JavaPlugin {
	public static Main plugin;
	public static File configFile;
	public static YamlConfiguration cfg;

	public static GrapplingHookRecipe re = new GrapplingHookRecipe();
	public static List<String> worldList = new ArrayList<String>();
	public static List<String> lore = new ArrayList<String>();
	public static long cooldownTime = 2;
	public static String prefix = "";
	public static String noperm = "";

	public void onEnable() {
		sendConsoleMesssage("§7[§6GrapplingHook§7] §2Plugin loading...");
		plugin = this;
		checkUpdate();
		loadConfig();
		versionCheck();
		re.loadRecipe();
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(prefix + "§cPlugin unloaded!");
	}

	public void versionCheck() {
		final String serverVersion = Bukkit.getBukkitVersion().split("-")[0];
		if (!serverVersion.contains("1.8") && !serverVersion.contains("1.9") && !serverVersion.contains("1.10")
				&& !serverVersion.contains("1.11") && !serverVersion.contains("1.12")) {

			if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
				getServer().getPluginManager().registerEvents(new Grapplinghook1_13_above_WorldGuard(), this);
				new AddWorldGuardFlag();
			} else {
				getServer().getPluginManager().registerEvents(new Grapplinghook1_13_above(), this);
			}
			getServer().getPluginManager().registerEvents(new DamageListener(), this);
			getServer().getPluginManager().registerEvents(new CraftListener(), this);
			Bukkit.getConsoleSender().sendMessage(
					prefix + "§2Server version " + serverVersion + " identified. Plugin successfully loaded!");
		} else {
			Bukkit.getConsoleSender().sendMessage(prefix + "§4Unsupported version! §7Try the other .jar file!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	public void checkUpdate() {
		new UpdateChecker(this).getVersion(version -> {
			if (this.getDescription().getVersion().equalsIgnoreCase(version))
				Bukkit.getConsoleSender().sendMessage(prefix + "There is not a new version available.");
			else
				Bukkit.getConsoleSender().sendMessage(prefix + "§2There is a new version available: " + version);
		});
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		configFile = new File("plugins" + File.separator + "GrapplingHook", "config.yml");
		cfg = YamlConfiguration.loadConfiguration(configFile);

		worldList = getConfig().getStringList("WorldList");
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Plugin.prefix"));
		noperm = ChatColor.translateAlternateColorCodes('&',
				getConfig().getString("Messages.noperm").replace("%prefix%", prefix + ""));

		loadLore();
	}

	public static void sendConsoleMesssage(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public static Main getPlugin() {
		return plugin;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("enterhaken") || cmd.getName().equalsIgnoreCase("grapplinghook")
				|| cmd.getName().equalsIgnoreCase("gh") || cmd.getName().equalsIgnoreCase("eh")) {
			if (args.length == 1) {
				final String subCmd = args[0];

				if (subCmd.equalsIgnoreCase("reload"))
					rlConfig(s);
				else if (subCmd.equalsIgnoreCase("help") || subCmd.equalsIgnoreCase("hilfe"))
					sendHelp(s);

				else if (subCmd.equalsIgnoreCase("allworlds")) {
					if (s.isOp() || s.hasPermission("grapplinghook.useInAllWorlds")
							|| s.hasPermission("grapplinghook.cmds.*")) {
						final boolean currentState = getConfig().getBoolean("Plugin.useInAllWorlds");
						getConfig().set("Plugin.useInAllWorlds", !currentState);
						try {
							getConfig().save(configFile);
						} catch (Exception e) {

							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
						}
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.useInAllWorlds").replace("%prefix%", prefix + "")
										.replace("%state%", !currentState + "")));
					} else
						s.sendMessage(noperm);

				} else if (subCmd.equalsIgnoreCase("useair")) {
					if (s.isOp() || s.hasPermission("grapplinghook.useAir")
							|| s.hasPermission("grapplinghook.cmds.*")) {
						boolean currentState = getConfig().getBoolean("Plugin.useAir");
						getConfig().set("Plugin.useAir", !currentState);
						try {
							getConfig().save(configFile);
						} catch (Exception e) {

							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
						}
						s.sendMessage(
								ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.useAir")
										.replace("%prefix%", prefix + "").replace("%state%", "" + !currentState)));
					} else
						s.sendMessage(noperm);

				} else if (subCmd.equalsIgnoreCase("crafting")) {
					if (s.isOp() || s.hasPermission("grapplinghook.cmd.craft")
							|| s.hasPermission("grapplinghook.cmds.*")) {
						boolean currentCraftingSta = getConfig().getBoolean("Plugin.crafting");
						if (currentCraftingSta) {
							getConfig().set("Plugin.crafting", false);
						} else {
							getConfig().set("Plugin.crafting", true);
						}
						try {
							getConfig().save(configFile);
						} catch (Exception e) {

							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							e.printStackTrace();
						}
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.crafting").replace("%prefix%", prefix + "")
										.replace("%state%", "" + !currentCraftingSta)));
					} else
						s.sendMessage(noperm);

				} else if (s instanceof Player) {
					final Player p = (Player) s;

					if (subCmd.equalsIgnoreCase("setworld") || subCmd.equalsIgnoreCase("addworld"))
						addWorld(s, p.getLocation().getWorld().getName());
					else if (subCmd.equalsIgnoreCase("removeworld") || subCmd.equalsIgnoreCase("delworld")
							|| subCmd.equalsIgnoreCase("rmw"))
						removeWorld(s, p.getLocation().getWorld().getName());
					else if (subCmd.equalsIgnoreCase("give"))
						givePlayerGh(s, p);
					else
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.unknownCMD").replace("%prefix%", prefix + "")));
				} else
					s.sendMessage(ChatColor.translateAlternateColorCodes('&',
							getConfig().getString("Messages.unknownCMD").replace("%prefix%", prefix + "")));

			} else if (args.length == 2) {
				final String subCmd = args[0];
				final String option = args[1];

				if (subCmd.equalsIgnoreCase("sound")) {
					if (s.isOp() || s.hasPermission("grapplinghook.setSound")
							|| s.hasPermission("grapplinghook.cmds.*")) {
						getConfig().set("Sound.grapplinghookSound", option.toUpperCase());
						try {
							getConfig().save(configFile);
						} catch (Exception e) {

							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							e.printStackTrace();
						}
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.setSound").replace("%prefix%", prefix + "")
										.replace("%world%", "§4Matrix§7").replace("%sound%", option.toUpperCase())));
					} else
						s.sendMessage(noperm);

				} else if (subCmd.equalsIgnoreCase("setuses")) {
					if (s.isOp() || s.hasPermission("grapplinghook.setUses")
							|| s.hasPermission("grapplinghook.cmds.*")) {
						int newUses;
						try {
							newUses = Integer.parseInt(option);
						} catch (NumberFormatException e) {
							s.sendMessage(ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("Messages.onlyNumbers").replace("%prefix%", prefix + "")));
							return false;
						}

						getConfig().set("Plugin.maxUses", newUses);
						try {
							getConfig().save(configFile);
						} catch (Exception e) {
							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							e.printStackTrace();
						}
						loadLore();
						s.sendMessage(
								ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.setUses")
										.replace("%prefix%", prefix).replace("%newvalue%", option)));
					} else
						s.sendMessage(noperm);

				} else if (subCmd.equalsIgnoreCase("setCooldown")) {
					if (s.isOp() || s.hasPermission("grapplinghook.setCooldown")
							|| s.hasPermission("grapplinghook.cmds.*")) {

						float newCooldownTime;
						try {
							newCooldownTime = Float.parseFloat(option);
						} catch (NumberFormatException e) {
							s.sendMessage(ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("Messages.onlyNumbers").replace("%prefix%", prefix + "")));
							return false;
						}

						getConfig().set("Plugin.cooldown", newCooldownTime);
						try {
							getConfig().save(configFile);
							Cooldown.refresh();// TODO Refresh complete config
						} catch (Exception e) {

							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							e.printStackTrace();
						}
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.setCooldown").replace("%prefix%", prefix + "")
										.replace("%newTime%", "" + newCooldownTime)));
					} else
						s.sendMessage(noperm);

				} else if (subCmd.equalsIgnoreCase("give"))
					givePlayerGh(s, Bukkit.getPlayer(option));
				else if (subCmd.equalsIgnoreCase("setworld") || subCmd.equalsIgnoreCase("addworld"))
					addWorld(s, option);
				else if (subCmd.equalsIgnoreCase("removeworld") || subCmd.equalsIgnoreCase("delworld")
						|| subCmd.equalsIgnoreCase("rmw"))
					removeWorld(s, option);
				else
					s.sendMessage(ChatColor.translateAlternateColorCodes('&',
							getConfig().getString("Messages.unknownCMD").replace("%prefix%", prefix + "")));
			} else
				s.sendMessage(ChatColor.translateAlternateColorCodes('&',
						getConfig().getString("Messages.unknownCMD").replace("%prefix%", prefix + "")));
			return true;
		} else
			return false;
	}

	private void rlConfig(CommandSender s) {
		if (s.isOp() || s.hasPermission("grapplinghook.reload") || s.hasPermission("grapplinghook.cmds.*")) {
			try {
				getConfig().load(configFile);
			} catch (Exception e) {
				s.sendMessage(prefix
						+ "§4An error has occurred! Couldn't load config.yml file! Restoring default config.yml file.");
				getConfig().options().copyDefaults();
				saveDefaultConfig();
			}
			updateVars();
			s.sendMessage(ChatColor.translateAlternateColorCodes('&',
					getConfig().getString("Messages.reload").replace("%prefix%", prefix + "")));
		} else
			s.sendMessage(noperm);
	}

	private void updateVars() {
		worldList = getConfig().getStringList("WorldList");
		loadLore();
		
		if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null)
			Grapplinghook1_13_above_WorldGuard.updateVars();
		else
			Grapplinghook1_13_above.updateVars();

		NoFallDamage.getInstance().refresh();
		Cooldown.refresh();
	}

	private void loadLore() {
		lore.clear();
		Set<String> l = getConfig().getConfigurationSection("Lore").getKeys(false);
		final int maxUses = getConfig().getInt("Plugin.maxUses");
		if (maxUses != 0)
			lore.add(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.usesLeft")) + ": "
					+ maxUses);

		for (String loreKey : l) {
			if (loreKey.toLowerCase().contains("lore")) {
				String loreS = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Lore." + loreKey))
						.replace("%prefix%", prefix);
				if (loreS != null && !loreS.equalsIgnoreCase(""))
					lore.add(loreS);
			}
		}
	}
	
	private void addWorld(CommandSender s, String worldName) {
		if (s.isOp() || s.hasPermission("grapplinghook.setworld") || s.hasPermission("grapplinghook.cmds.*")) {
			if (!worldList.contains(worldName)) {
				worldList.add(worldName);
				getConfig().set("WorldList", worldList);
				try {
					getConfig().save(configFile);
				} catch (Exception e) {

					s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
				}
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.setWorld")
						.replace("%prefix%", prefix + "").replace("%world%", worldName)));
			} else
				s.sendMessage(
						ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.alreadyInList")
								.replace("%prefix%", prefix + "").replace("%world%", worldName)));
		} else
			s.sendMessage(noperm);
	}

	private void removeWorld(CommandSender s, String worldName) {
		if (s.isOp() || s.hasPermission("grapplinghook.removeworld") || s.hasPermission("grapplinghook.cmds.*")) {
			if (worldList.contains(worldName)) {
				worldList.remove(worldName);
				getConfig().set("WorldList", worldList);
				try {
					getConfig().save(configFile);
				} catch (Exception e) {

					s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
					e.printStackTrace();
				}
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.removeWorld")
						.replace("%prefix%", prefix + "").replace("%world%", worldName)));
			} else
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.isNotInList")
						.replace("%prefix%", prefix + "").replace("%world%", worldName)));
		} else
			s.sendMessage(noperm);
	}

	private void givePlayerGh(CommandSender s, Player p) {
		if (p == null)
			return;
		if (s.isOp() || s.hasPermission("grapplinghook.give") || s.hasPermission("grapplinghook.cmds.*"))
			p.getInventory().addItem(getGrapplingHook());
		else
			s.sendMessage(noperm);
	}

	private ItemStack getGrapplingHook() {
		ItemStack is = new ItemStack(Material.FISHING_ROD);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor
				.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Plugin.grapplingHookName"))
				.replace("%prefix%", Main.prefix));
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	private void sendHelp(CommandSender s) {
		if (!(s.isOp() || s.hasPermission("grapplinghook.help") || s.hasPermission("grapplinghook.cmds.*"))) {
			s.sendMessage(noperm);
			return;
		}

		s.sendMessage("§3-------------->" + prefix + "§3<--------------");
		s.sendMessage("§8/gh reload | §2Reload the config of this plugin! \n§8[per:§4grapplinghook.setworld§8]");
		s.sendMessage("§8/gh setworld | §2Add the world in wich you are. \n§8[per:§4grapplinghook.removeworld§8]");
		s.sendMessage(
				"§8/gh removeworld/delworld | §2Remove the world in which you are. \n§8[per:§4grapplinghook.reload§8]");
		s.sendMessage("§8/gh sound [sound] | §2Set the sound of the GH. \n§8[per:§4grapplinghook.setSound§8]");
		s.sendMessage(
				"§8/gh allWorlds | §2Enable/Disable the GH in all worlds. \n§8[per:§4grapplinghook.useInAllWorlds§8]");
		s.sendMessage(
				"§8/gh setCooldown [seconds] | §2Set the Cooldown of the GH. \n§8[per:§4grapplinghook.setCooldown§8]");
		s.sendMessage("§8/gh useAir | §2Enable/Disable useAir when throwing. \n§8[per:§4grapplinghook.useAir§8]");
		s.sendMessage("§8/gh crafting | §2Enable/Disable crafting. \n§8[per:§4grapplinghook.cmd.craft§8]");
		s.sendMessage("§8/gh give | §2Gives you the Grappling hook. \n§8[per:§4grapplinghook.give§8]");
	}
}