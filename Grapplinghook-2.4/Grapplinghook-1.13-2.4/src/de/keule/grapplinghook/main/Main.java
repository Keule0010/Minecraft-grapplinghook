package de.keule.grapplinghook.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import de.keule.grapplinghook.cooldown.Cooldown;
import de.keule.grapplinghook.crafting.Recipe;
import de.keule.grapplinghook.listener.CraftListener;
import de.keule.grapplinghook.listener.Grapplinghook1_13_above;
import de.keule.grapplinghook.listener.Grapplinghook1_13_above_WorldGuard;
import de.keule.grapplinghook.listener.JoinListener;
import de.keule.grapplinghook.updateChecker.UpdateChecker;
import de.keule.grapplinghook.worldGuard.AddWorldGuardFlag;

public class Main extends JavaPlugin {
	public static Main plugin;
	public static File configFile;
	public static YamlConfiguration cfg;

	public static Recipe re = new Recipe();
	public static List<String> worldList = new ArrayList<String>();
	public static List<String> lore = new ArrayList<String>();
	public static String prefix = "";
	public static String noperm = "";
	public static String serverVersion = "";
	public static long cooldownTime = 2;
	public static boolean isSupported = false;
	public static boolean error = false;

	public void onEnable() {
		sendConsoleMesssage("§7[§6GrapplingHook§7] §2Plugin loading...");
		plugin = this;
		checkUpdate();
		loadConfig();
		versionCheck();
		re.enableRecipe();
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(prefix + "§cPlugin unloaded!");
	}

	public void versionCheck() {
		serverVersion = Bukkit.getBukkitVersion().split("-")[0];
		if (serverVersion.contains("1.13") || serverVersion.contains("1.14") || serverVersion.contains("1.15")) {
			if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
				getServer().getPluginManager().registerEvents(new Grapplinghook1_13_above_WorldGuard(), this);
				new AddWorldGuardFlag();
			} else {
				getServer().getPluginManager().registerEvents(new Grapplinghook1_13_above(), this);
			}
			getServer().getPluginManager().registerEvents(new JoinListener(), this);
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
			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
				Bukkit.getConsoleSender().sendMessage(prefix + "There is not a new update available.");
			} else {
				Bukkit.getConsoleSender().sendMessage(prefix + "§2There is a new update available.");
			}
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

		Set<String> l = Main.getPlugin().getConfig().getConfigurationSection("Lore").getKeys(false);
		for (String loreKey : l) {
			if (loreKey.toLowerCase().contains("lore")) {
				String loreS = ChatColor
						.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Lore." + loreKey))
						.replace("%prefix%", Main.prefix);
				if (loreS != null && !loreS.equalsIgnoreCase(""))
					lore.add(loreS);

			}
		}
	}

	public static void sendConsoleMesssage(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public static Main getPlugin() {
		return plugin;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		Player p = null;
		if (s instanceof Player) {
			p = (Player) s;
			if (cmd.getName().equalsIgnoreCase("enterhaken") || cmd.getName().equalsIgnoreCase("grapplinghook")
					|| cmd.getName().equalsIgnoreCase("gh") || cmd.getName().equalsIgnoreCase("eh")) {
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("sound")) {
						if (p.isOp() || p.hasPermission("grapplinghook.setSound")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							String sound = args[1].toUpperCase();
							getConfig().set("Sound.grapplinghookSound", sound);
							try {
								getConfig().save(configFile);
							} catch (Exception e) {
								error = true;
								p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							}
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("Messages.setSound").replace("%prefix%", prefix + "")
											.replace("%world%", p.getLocation().getWorld().getName())
											.replace("%sound%", sound)));
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("setCooldown")) {
						if (p.isOp() || p.hasPermission("grapplinghook.setCooldown")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							try {
								float cooldownTime = Float.parseFloat(args[1]);
								getConfig().set("Plugin.cooldown", cooldownTime);
								try {
									getConfig().save(configFile);
									Cooldown.refresh();
								} catch (Exception e) {
									error = true;
									p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
								}
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										getConfig().getString("Messages.setCooldown").replace("%prefix%", prefix + "")
												.replace("%newTime%", "" + cooldownTime)));
							} catch (NumberFormatException e) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig()
										.getString("Messages.onlyNumbers").replace("%prefix%", prefix + "")));
							}
						} else {
							p.sendMessage(noperm);
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.unknownCMD").replace("%prefix%", prefix + "")
										.replace("%world%", p.getLocation().getWorld().getName())));
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("crafting")) {
						if (p.isOp() || p.hasPermission("grapplinghook.cmd.craft")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							boolean currentCraftingSta = getConfig().getBoolean("Plugin.crafting");
							if (currentCraftingSta) {
								getConfig().set("Plugin.crafting", false);
							} else {
								getConfig().set("Plugin.crafting", true);
							}
							try {
								getConfig().save(configFile);
							} catch (Exception e) {
								error = true;
								p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							}
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("Messages.crafting").replace("%prefix%", prefix + "")
											.replace("%state%", "" + !currentCraftingSta)));
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("give")) {
						if (p.isOp() || p.hasPermission("grapplinghook.give")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							ItemStack is = new ItemStack(Material.FISHING_ROD);
							ItemMeta im = is.getItemMeta();
							im.setDisplayName(ChatColor
									.translateAlternateColorCodes('&',
											Main.getPlugin().getConfig().getString("Plugin.grapplingHookName"))
									.replace("%prefix%", Main.prefix));
							im.setLore(lore);
							is.setItemMeta(im);
							p.getInventory().addItem(is);
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("setworld")) {
						if (p.isOp() || p.hasPermission("grapplinghook.setworld")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							if (!worldList.contains(p.getLocation().getWorld().getName())) {
								Location loc = p.getLocation();
								worldList.add(loc.getWorld().getName());
								getConfig().set("WorldList", worldList);
								try {
									getConfig().save(configFile);
								} catch (Exception e) {
									error = true;
									p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
								}
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										getConfig().getString("Messages.setWorld").replace("%prefix%", prefix + "")
												.replace("%world%", p.getLocation().getWorld().getName())));
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										getConfig().getString("Messages.alreadyInList").replace("%prefix%", prefix + "")
												.replace("%world%", p.getLocation().getWorld().getName())));
							}
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("removeworld") || args[0].equalsIgnoreCase("delworld")) {
						if (p.isOp() || p.hasPermission("grapplinghook.removeworld")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							if (worldList.contains(p.getLocation().getWorld().getName())) {
								worldList.remove(p.getLocation().getWorld().getName());
								getConfig().set("WorldList", worldList);
								try {
									getConfig().save(configFile);
								} catch (Exception e) {
									error = true;
									p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
								}
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										getConfig().getString("Messages.removeWorld").replace("%prefix%", prefix + "")
												.replace("%world%", p.getLocation().getWorld().getName())));
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										getConfig().getString("Messages.isNotInList").replace("%prefix%", prefix + "")
												.replace("%world%", p.getLocation().getWorld().getName())));
							}
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (p.isOp() || p.hasPermission("grapplinghook.reload")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							try {
								getConfig().load(configFile);
							} catch (Exception e) {
								p.sendMessage(prefix
										+ "§4An error has occurred! Couldn't load config.yml file! Restoring default config.yml file.");
								getConfig().options().copyDefaults();
								saveDefaultConfig();
							}
							worldList = getConfig().getStringList("WorldList");
							if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null){
								Grapplinghook1_13_above_WorldGuard.g = Main.getPlugin().getConfig().getDouble("Plugin.gravity");
								Grapplinghook1_13_above_WorldGuard.multiplier = Main.getPlugin().getConfig().getDouble("Plugin.throw_speed_multiplier") * 2;
							}else {
								Grapplinghook1_13_above.g = Main.getPlugin().getConfig().getDouble("Plugin.gravity");
								Grapplinghook1_13_above.multiplier = Main.getPlugin().getConfig().getDouble("Plugin.throw_speed_multiplier") * 2;
							}
							Cooldown.refresh();
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("Messages.reload").replace("%prefix%", prefix + "")
											.replace("%world%", p.getLocation().getWorld().getName())));
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("AllWorlds")) {
						if (p.isOp() || p.hasPermission("grapplinghook.useInAllWorlds")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							getConfig().set("Plugin.useInAllWorlds", true);
							try {
								getConfig().save(configFile);
							} catch (Exception e) {
								error = true;
								p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							}
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("Messages.useInAllWorlds").replace("%prefix%", prefix + "")
											.replace("%world%", p.getLocation().getWorld().getName())));
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("IndividualWorlds")) {
						if (p.isOp() || p.hasPermission("grapplinghook.useInIndividualWorlds")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							getConfig().set("Plugin.useInAllWorlds", false);
							try {
								getConfig().save(configFile);
							} catch (Exception e) {
								error = true;
								p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							}
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									getConfig().getString("Messages.useInIndividualWorlds")
											.replace("%prefix%", prefix + "")
											.replace("%world%", p.getLocation().getWorld().getName())));
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("hilfe")) {
						if (p.isOp() || p.hasPermission("grapplinghook.help")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							p.sendMessage("§3-------------->" + prefix + "§3<--------------");
							p.sendMessage(
									"§7/gh reload | \n§2The config.yml will be reloaded! \n§7[per:§4grapplinghook.setworld§7]");
							p.sendMessage(
									"§7/gh setworld | \n§2Add the world in wich you are. \n§7[per:§4grapplinghook.removeworld§7]");
							p.sendMessage(
									"§7/gh removeworld/delworld | \n§2Remove the world in which you are. \n§7[per:§4grapplinghook.reload§7]");
							p.sendMessage(
									"§7/gh sound [sound] | \n§2Set the sound of the grappling hook. \n§7[per:§4grapplinghook.setSound§7]");
							p.sendMessage(
									"§7/gh allWorlds | \n§2You can use the grppling hook in all worlds. \n§7[per:§4grapplinghook.useInAllWorlds§7]");
							p.sendMessage(
									"§7/gh individualWorlds | \n§2You can now use the grappling hook only in the worlds wich are in the config.yml(Add with setworld). \n§7[per:§4grapplinghook.useInIndividualWorlds§7]");
							p.sendMessage(
									"§7/gh setCooldown [seconds] | \n§2Set the Cooldown of the grappling hook. \n§7[per:§4grapplinghook.setCooldown§7]");
							p.sendMessage(
									"§7/gh useAir | \n§2The grappling hook works now also in the air. \n§7[per:§4grapplinghook.useAir§7]");
							p.sendMessage(
									"§7/gh crafting | \n§2You can now craft the grappling hook and only this one works. \n§7[per:§4grapplinghook.cmd.craft§7]");
							p.sendMessage(
									"§7/gh give | \n§2You get the Grappling hook with the custom name from the config. \n§7[per:§4grapplinghook.give§7]");
							p.sendMessage("§3-------------->" + prefix + "§3<--------------");
						} else {
							p.sendMessage(noperm);
						}
					} else if (args[0].equalsIgnoreCase("useAir")) {
						if (p.isOp() || p.hasPermission("grapplinghook.useAir")
								|| p.hasPermission("grapplinghook.cmds.*")) {
							boolean useAir = getConfig().getBoolean("Plugin.useAir");
							if (useAir) {
								getConfig().set("Plugin.useAir", false);
							} else {
								getConfig().set("Plugin.useAir", true);
							}
							try {
								getConfig().save(configFile);
							} catch (Exception e) {
								error = true;
								p.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							}
							p.sendMessage(
									ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.useAir")
											.replace("%prefix%", prefix + "").replace("%state%", "" + !useAir)));
						} else {
							p.sendMessage(noperm);
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.unknownCMD").replace("%prefix%", prefix + "")
										.replace("%world%", p.getLocation().getWorld().getName())));
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							getConfig().getString("Messages.unknownCMD").replace("%prefix%", prefix + "")
									.replace("%world%", p.getLocation().getWorld().getName())));
				}
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (s.isOp() || s.hasPermission("grapplinghook.reload") || s.hasPermission("grapplinghook.cmds.*")) {
				try {
					getConfig().load(configFile);
				} catch (Exception e) {
					s.sendMessage(prefix
							+ "§4An error has occurred! Couldn't load config.yml file! Restoring default config.yml file.");
					getConfig().options().copyDefaults(true);
					saveDefaultConfig();
				}
				worldList = getConfig().getStringList("WorldList");
				Cooldown.refresh();
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.reload")
						.replace("%prefix%", prefix + "").replace("%world%", "§4Matrix§7")));
			} else {
				s.sendMessage(noperm);
			}
		} else if (args[0].equalsIgnoreCase("AllWorlds")) {
			if (s.isOp() || s.hasPermission("grapplinghook.useInAllWorlds")
					|| s.hasPermission("grapplinghook.cmds.*")) {
				getConfig().set("Plugin.useInAllWorlds", true);
				try {
					getConfig().save(configFile);
				} catch (Exception e) {
					error = true;
					s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
					e.printStackTrace();
				}
				s.sendMessage(
						ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.useInAllWorlds")
								.replace("%prefix%", prefix + "").replace("%world%", "§4Matrix§7")));
			} else {
				s.sendMessage(noperm);
			}
		} else if (args[0].equalsIgnoreCase("crafting")) {
			if (s.isOp() || s.hasPermission("grapplinghook.cmd.craft") || s.hasPermission("grapplinghook.cmds.*")) {
				boolean currentCraftingSta = getConfig().getBoolean("Plugin.crafting");
				if (currentCraftingSta) {
					getConfig().set("Plugin.crafting", false);
				} else {
					getConfig().set("Plugin.crafting", true);
				}
				try {
					getConfig().save(configFile);
				} catch (Exception e) {
					error = true;
					s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
					e.printStackTrace();
				}
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.crafting")
						.replace("%prefix%", prefix + "").replace("%state%", "" + !currentCraftingSta)));
			} else {
				s.sendMessage(noperm);
			}
		} else if (args[0].equalsIgnoreCase("useAir")) {
			if (s.isOp() || s.hasPermission("grapplinghook.useAir") || s.hasPermission("grapplinghook.cmds.*")) {
				boolean useAir = getConfig().getBoolean("Plugin.useAir");
				if (useAir) {
					getConfig().set("Plugin.useAir", false);
				} else {
					getConfig().set("Plugin.useAir", true);
				}
				try {
					getConfig().save(configFile);
				} catch (Exception e) {
					error = true;
					s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
					e.printStackTrace();
				}
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.useAir")
						.replace("%prefix%", prefix + "").replace("%state%", "" + !useAir)));
			} else {
				s.sendMessage(noperm);
			}
		} else if (args[0].equalsIgnoreCase("IndividualWorlds")) {
			if (s.isOp() || s.hasPermission("grapplinghook.useInIndividualWorlds")
					|| s.hasPermission("grapplinghook.cmds.*")) {
				getConfig().set("Plugin.useInAllWorlds", false);
				try {
					getConfig().save(configFile);
				} catch (Exception e) {
					error = true;
					s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
					e.printStackTrace();
				}
				s.sendMessage(ChatColor.translateAlternateColorCodes('&',
						getConfig().getString("Messages.useInIndividualWorlds").replace("%prefix%", prefix + "")
								.replace("%world%", "§4Matrix§7")));
			} else {
				s.sendMessage(noperm);
			}
		} else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("hilfe")) {
			if (s.isOp() || s.hasPermission("grapplinghook.help") || s.hasPermission("grapplinghook.cmds.*")) {
				s.sendMessage("§3-------------->" + prefix + "§3<--------------");
				s.sendMessage(
						"§7/gh reload | \n§2The config.yml will be reloaded! \n§7[per:§4grapplinghook.setworld§7]");
				s.sendMessage(
						"§7/gh setworld | \n§2Add the world in wich you are. \n§7[per:§4grapplinghook.removeworld§7]");
				s.sendMessage(
						"§7/gh removeworld/delworld | \n§2Remove the world in which you are. \n§7[per:§4grapplinghook.reload§7]");
				s.sendMessage(
						"§7/gh sound [sound] | \n§2Set the sound of the grappling hook. \n§7[per:§4grapplinghook.setSound§7]");
				s.sendMessage(
						"§7/gh allWorlds | \n§2You can use the grppling hook in all worlds. \n§7[per:§4grapplinghook.useInAllWorlds§7]");
				s.sendMessage(
						"§7/gh individualWorlds | \n§2You can now use the grappling hook only in the worlds wich are in the config.yml(Add with setworld). \n§7[per:§4grapplinghook.useInIndividualWorlds§7]");
				s.sendMessage(
						"§7/gh setCooldown [seconds] | \n§2Set the Cooldown of the grappling hook. \n§7[per:§4grapplinghook.setCooldown§7]");
				s.sendMessage(
						"§7/gh useAir | \n§2The grappling hook works now also in the air. \n§7[per:§4grapplinghook.useAir§7]");
				s.sendMessage(
						"§7/gh crafting | \n§2You can now craft the grappling hook and only this one works. \n§7[per:§4grapplinghook.cmd.craft§7]");
				s.sendMessage(
						"§7/gh give | \n§2You get the Grappling hook with the custom name from the config. \n§7[per:§4grapplinghook.give§7]");
				s.sendMessage("§3-------------->" + prefix + "§3<--------------");
			} else {
				s.sendMessage(noperm);
			}
		} else if (args[0].equalsIgnoreCase("setworld")) {
			if (args.length == 2) {
				if (s.isOp() || s.hasPermission("grapplinghook.setworld") || s.hasPermission("grapplinghook.cmds.*")) {
					if (!worldList.contains(args[1])) {
						worldList.add(args[1]);
						getConfig().set("WorldList", worldList);
						try {
							getConfig().save(configFile);
						} catch (Exception e) {
							error = true;
							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							e.printStackTrace();
						}
						s.sendMessage(
								ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.setWorld")
										.replace("%prefix%", prefix + "").replace("%world%", args[1])));
					} else {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.alreadyInList").replace("%prefix%", prefix + "")
										.replace("%world%", args[1])));
					}
				} else {
					s.sendMessage(noperm);
				}
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.unknownCMD")
						.replace("%prefix%", prefix + "").replace("%world%", "§4Matrix§7")));
			}

		} else if (args[0].equalsIgnoreCase("give")) {
			if (args.length == 2) {
				if (s.isOp() || s.hasPermission("grapplinghook.give") || s.hasPermission("grapplinghook.cmds.*")) {
					ItemStack is = new ItemStack(Material.FISHING_ROD);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(ChatColor
							.translateAlternateColorCodes('&',
									Main.getPlugin().getConfig().getString("Plugin.grapplingHookName"))
							.replace("%prefix%", Main.prefix));
					im.setLore(lore);
					is.setItemMeta(im);

					Bukkit.getPlayer(args[1]).getInventory().addItem(is);
				} else {
					s.sendMessage(noperm);
				}
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.unknownCMD")
						.replace("%prefix%", prefix + "").replace("%world%", "§4Matrix§7")));
			}
		} else if (args[0].equalsIgnoreCase("removeworld") || args[0].equalsIgnoreCase("delworld")) {
			if (args.length == 2) {
				if (s.isOp() || s.hasPermission("grapplinghook.removeworld")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					if (worldList.contains(args[1])) {
						worldList.remove(args[1]);
						getConfig().set("WorldList", worldList);
						try {
							getConfig().save(configFile);
						} catch (Exception e) {
							error = true;
							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							e.printStackTrace();
						}
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.removeWorld").replace("%prefix%", prefix + "")
										.replace("%world%", args[1])));
					} else {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.isNotInList").replace("%prefix%", prefix + "")
										.replace("%world%", args[1])));
					}
				} else {
					s.sendMessage(noperm);
				}
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.unknownCMD")
						.replace("%prefix%", prefix + "").replace("%world%", "§4Matrix§7")));
			}
		} else if (args[0].equalsIgnoreCase("sound")) {
			if (args.length == 2) {
				if (s.isOp() || s.hasPermission("grapplinghook.setSound") || s.hasPermission("grapplinghook.cmds.*")) {
					String sound = args[1].toUpperCase();
					getConfig().set("Sound.grapplinghookSound", sound);
					try {
						getConfig().save(configFile);
					} catch (Exception e) {
						error = true;
						s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
						e.printStackTrace();
					}
					s.sendMessage(ChatColor.translateAlternateColorCodes('&',
							getConfig().getString("Messages.setSound").replace("%prefix%", prefix + "")
									.replace("%world%", "§4Matrix§7").replace("%sound%", sound)));
				} else {
					s.sendMessage(noperm);
				}
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.unknownCMD")
						.replace("%prefix%", prefix + "").replace("%world%", "§4Matrix§7")));
			}
		} else if (args[0].equalsIgnoreCase("setCooldown")) {
			if (args.length == 2) {
				if (s.isOp() || s.hasPermission("grapplinghook.setCooldown")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					try {
						float cooldownTime = Float.parseFloat(args[1]);
						getConfig().set("Plugin.cooldown", cooldownTime);
						try {
							getConfig().save(configFile);
							Cooldown.refresh();
						} catch (Exception e) {
							error = true;
							s.sendMessage(prefix + "§4An error has occurred! Couldn't save config.yml file!");
							e.printStackTrace();
						}
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.setCooldown").replace("%prefix%", prefix + "")
										.replace("%newTime%", "" + cooldownTime)));
					} catch (NumberFormatException e) {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Messages.onlyNumbers").replace("%prefix%", prefix + "")));
					}
				} else {
					s.sendMessage(noperm);
				}
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.unknownCMD")
						.replace("%prefix%", prefix + "").replace("%world%", "§4Matrix§7")));
			}

		} else {
			s.sendMessage(prefix + "§4You are not a player!");
		}
		return false;
	}
}