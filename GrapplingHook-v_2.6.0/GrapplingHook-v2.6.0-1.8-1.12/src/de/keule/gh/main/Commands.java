package de.keule.gh.main;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.keule.gh.config.ConfigKeys;
import de.keule.gh.config.ConfigManager;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			final String subCmd = args[0];

			if (subCmd.equalsIgnoreCase("reload") || subCmd.equalsIgnoreCase("rl"))
				reloadConfig(s);
			else if (subCmd.equalsIgnoreCase("help") || subCmd.equalsIgnoreCase("hilfe"))
				sendHelp(s, 0);
			else if (subCmd.equalsIgnoreCase("resetConfig")) {
				File dataFolder = GHPlugin.getInstance().getDataFolder();
				new File(dataFolder, "config.yml").renameTo(new File(dataFolder, "config_old_BA.yml"));
				new File(dataFolder, "config.yml").delete();
				ConfigManager.restoreFactorySettings();
				s.sendMessage(ConfigManager.getMessage(ConfigKeys.SUCCESSFUL));
			} else if (subCmd.equalsIgnoreCase("allworlds")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.useInAllWorlds")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					final boolean currentState = ConfigManager.getBoolean(ConfigKeys.USE_ALL_WORLDS);
					ConfigManager.set(ConfigKeys.USE_ALL_WORLDS, !currentState);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED)
							.replace("%option%", "useInAllWorlds").replace("%newValue%", !currentState + ""));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("unlimited")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.unlimited")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					final boolean currentState = ConfigManager.getBoolean(ConfigKeys.UNLIMTED_USES);
					ConfigManager.set(ConfigKeys.UNLIMTED_USES, !currentState);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED)
							.replace("%newValue%", "" + !currentState).replace("%option%", "unlimitedUses"));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("useAir")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.useAir")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.USE_AIR);
					ConfigManager.set(ConfigKeys.USE_AIR, !currentSta);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "useAir")
							.replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("crafting")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.craft") || s.hasPermission("grapplinghook.cmds.*")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.CRAFTING);
					ConfigManager.set(ConfigKeys.CRAFTING, !currentSta);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "crafting")
							.replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("allRods")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmds.*")
						|| s.hasPermission("grapplinghook.cmd.allRods")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.ALL_RODS);
					ConfigManager.set(ConfigKeys.ALL_RODS, !currentSta);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "allRods")
							.replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("enchanted")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmds.*")
						|| s.hasPermission("grapplinghook.cmd.enchanted")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.ENCHANTED);
					ConfigManager.set(ConfigKeys.ENCHANTED, !currentSta);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "enchanted")
							.replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("unbreakable")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmds.*")
						|| s.hasPermission("grapplinghook.cmd.unbreakable")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.UNBREAKABLE);
					ConfigManager.set(ConfigKeys.UNBREAKABLE, !currentSta);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "unbreakable")
							.replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("noFallDamage")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmds.*")
						|| s.hasPermission("grapplinghook.cmd.noFallDamage")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.NO_FALL_DAMAGE);
					ConfigManager.set(ConfigKeys.NO_FALL_DAMAGE, !currentSta);
					ConfigManager.reload();
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "noFallDamage")
							.replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("setDestroy")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmds.*")
						|| s.hasPermission("grapplinghook.cmd.setDestroy")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.DESTROY_NO_MORE_UESE);
					ConfigManager.set(ConfigKeys.DESTROY_NO_MORE_UESE, !currentSta);
					ConfigManager.reload();
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED)
							.replace("%option%", "destroyWhenNoMoreUses").replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("useFloatingBlocks")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmds.*")
						|| s.hasPermission("grapplinghook.cmd.useFloatingBlocks")) {
					final boolean currentSta = ConfigManager.getBoolean(ConfigKeys.BLOCK_FLOATING);
					ConfigManager.set(ConfigKeys.BLOCK_FLOATING, !currentSta);
					ConfigManager.reload();
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED)
							.replace("%option%", "useFloatingBlocks").replace("%newValue%", "" + !currentSta));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (s instanceof Player) {
				final Player p = (Player) s;

				if (subCmd.equalsIgnoreCase("setworld") || subCmd.equalsIgnoreCase("addworld"))
					addWorld(s, p.getLocation().getWorld().getName());
				else if (subCmd.equalsIgnoreCase("removeworld") || subCmd.equalsIgnoreCase("rmworld")
						|| subCmd.equalsIgnoreCase("rmw"))
					removeWorld(s, p.getLocation().getWorld().getName());
				else if (subCmd.equalsIgnoreCase("give"))
					givePlayerGh(s, p);
				else
					p.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_CMD_MSG));
			} else
				s.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_CMD_MSG));

		} else if (args.length == 2) {
			final String subCmd = args[0];
			final String value = args[1];

			if(subCmd.equalsIgnoreCase("help") || subCmd.equalsIgnoreCase("hilfe"))
				sendHelp(s, value);
			else if (subCmd.equalsIgnoreCase("setPullSound")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setSound")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					ConfigManager.set(ConfigKeys.SOUND, value.toUpperCase());
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "pullSound")
							.replace("%newValue%", value.toUpperCase()));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("setDestroySound")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setSound")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					ConfigManager.set(ConfigKeys.DESTROY_SOUND, value.toUpperCase());
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "destroySound")
							.replace("%newValue%", value.toUpperCase()));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("setUses")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setUses")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					int newUses;
					try {
						newUses = Integer.parseInt(value);
					} catch (NumberFormatException e) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
						return false;
					}

					ConfigManager.set(ConfigKeys.MAX_USES, newUses);
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "maxUses")
							.replace("%newValue%", value));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("setCooldown")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setCooldown")
						|| s.hasPermission("grapplinghook.cmds.*")) {

					float newTime;
					try {
						newTime = Float.parseFloat(value);
					} catch (NumberFormatException e) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
						return false;
					}

					ConfigManager.set(ConfigKeys.COOLDOWN, newTime);
					ConfigManager.reload();
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "cooldown")
							.replace("%newValue%", "" + newTime));
				} else
					s.sendMessage(ConfigManager.getNoPerm());

			} else if (subCmd.equalsIgnoreCase("setGravity")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setGravity")
						|| s.hasPermission("grapplinghook.cmds.*")) {

					float newG;
					try {
						newG = Float.parseFloat(value);
					} catch (NumberFormatException e) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
						return false;
					}

					ConfigManager.set(ConfigKeys.GRAVITY, newG);
					ConfigManager.reload();
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "gravity")
							.replace("%newValue%", "" + newG));
				} else
					s.sendMessage(ConfigManager.getNoPerm());
			} else if (subCmd.equalsIgnoreCase("setSpeed")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setSpeed")
						|| s.hasPermission("grapplinghook.cmds.*")) {

					float newS;
					try {
						newS = Float.parseFloat(value);
					} catch (NumberFormatException e) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
						return false;
					}

					ConfigManager.set(ConfigKeys.SPEED_MULTIPLIER, newS);
					ConfigManager.reload();
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED)
							.replace("%option%", "speedMultiplier").replace("%newValue%", "" + newS));
				} else
					s.sendMessage(ConfigManager.getNoPerm());
			} else if (subCmd.equalsIgnoreCase("setDestroyDelay")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setDestroyDelay")
						|| s.hasPermission("grapplinghook.cmds.*")) {

					long newS;
					try {
						newS = Long.parseLong(value);
					} catch (NumberFormatException e) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
						return false;
					}

					ConfigManager.set(ConfigKeys.DESTROY_DELAY, newS);
					ConfigManager.reload();
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "destroyDelay")
							.replace("%newValue%", "" + newS));
				} else
					s.sendMessage(ConfigManager.getNoPerm());
			} else if (subCmd.equalsIgnoreCase("setPrefix")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setPrefix")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					ConfigManager.set(ConfigKeys.PREFIX, value.replaceAll("\"", ""));
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "prefix")
							.replace("%newValue%", value.replaceAll("&", "§").replaceAll("\"", "")));
				} else
					s.sendMessage(ConfigManager.getNoPerm());
			} else if (subCmd.equalsIgnoreCase("setName")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setName")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					ConfigManager.set(ConfigKeys.NAME, value.replaceAll("\"", ""));
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.VALUE_CHANGED).replace("%option%", "name")
							.replace("%newValue%", value.replaceAll("&", "§").replaceAll("\"", "")));
				} else
					s.sendMessage(ConfigManager.getNoPerm());
			} else if (subCmd.equalsIgnoreCase("give"))
				givePlayerGh(s, value);
			else if (subCmd.equalsIgnoreCase("setworld") || subCmd.equalsIgnoreCase("addworld"))
				addWorld(s, value);
			else if (subCmd.equalsIgnoreCase("removeworld") || subCmd.equalsIgnoreCase("rmworld")
					|| subCmd.equalsIgnoreCase("rmw"))
				removeWorld(s, value);
			else if (s instanceof Player) {
				if (subCmd.equalsIgnoreCase("addUses")) {
					if (s.isOp() || s.hasPermission("grapplinghook.cmd.addUses")
							|| s.hasPermission("grapplinghook.cmds.*")) {
						int newV;
						try {
							newV = Integer.parseInt(args[1]);
						} catch (NumberFormatException e) {
							s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
							return false;
						}
						addUses(s, (Player) s, newV);
					}
				} else if (subCmd.equalsIgnoreCase("setPlayerUses")) {
					if (s.isOp() || s.hasPermission("grapplinghook.cmd.setUses")
							|| s.hasPermission("grapplinghook.cmds.*")) {
						int newV;
						try {
							newV = Integer.parseInt(args[1]);
						} catch (NumberFormatException e) {
							s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
							return false;
						}
						setUses(s, (Player) s, newV);
					}
				} else
					s.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_CMD_MSG));
			} else
				s.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_CMD_MSG));
		} else if (args.length == 3) {
			final String subCmd = args[0];
			final String player = args[1];
			final String value = args[2];

			if (subCmd.equalsIgnoreCase("addUses")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.addUses")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					Player p = Bukkit.getPlayer(player);
					if (p == null) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_PLAYER_MSG).replace("%name%", player));
						return false;
					}

					int newV;
					try {
						newV = Integer.parseInt(value);
					} catch (NumberFormatException e) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
						return false;
					}
					addUses(s, p, newV);
				}
			} else if (subCmd.equalsIgnoreCase("setPlayerUses")) {
				if (s.isOp() || s.hasPermission("grapplinghook.cmd.setPlayerUses")
						|| s.hasPermission("grapplinghook.cmds.*")) {
					Player p = Bukkit.getPlayer(player);
					if (p == null) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_PLAYER_MSG).replace("%name%", player));
						return false;
					}

					int newV;
					try {
						newV = Integer.parseInt(value);
					} catch (NumberFormatException e) {
						s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
						return false;
					}
					setUses(s, p, newV);
				}
			}
		} else
			s.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_CMD_MSG));
		return true;
	}

	private void addUses(CommandSender s, Player p, int uses) {
		if (p == null)
			return;

		ItemStack is = p.getInventory().getItemInHand();
		if (!GrapplingHook.isEqual(is)) {
			s.sendMessage(ConfigManager.getMessage(ConfigKeys.ITEM_NOT_IN_HAND));
			return;
		}

		ItemMeta meta = is.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null || lore.isEmpty()) {
			lore = ConfigManager.getLore(uses);
		} else {
			final String l = lore.get(lore.size() - 1).replaceAll("\\§", "").replaceAll("\\s+", "");
			if (l.equals("a"))
				return;
			int oldUses = 0;
			try {
				oldUses = Integer.parseInt(l);
			} catch (NumberFormatException e) {
				GHPlugin.sendConsoleMesssage("&cThe lore of a grappling hook from player: " + p.getName()
						+ " is corupted! When you have maxUses enabled it won't work anymore!");
				return;
			}
			lore = ConfigManager.getLore(oldUses + uses);
		}

		meta.setLore(lore);
		is.setItemMeta(meta);
		s.sendMessage(ConfigManager.getMessage(ConfigKeys.SUCCESSFUL));
	}

	private void setUses(CommandSender s, Player p, int uses) {
		ItemStack is = p.getInventory().getItemInHand();
		if (!GrapplingHook.isEqual(is)) {
			s.sendMessage(ConfigManager.getMessage(ConfigKeys.ITEM_NOT_IN_HAND));
			return;
		}

		ItemMeta meta = is.getItemMeta();
		meta.setLore(ConfigManager.getLore(uses));
		is.setItemMeta(meta);
		s.sendMessage(ConfigManager.getMessage(ConfigKeys.SUCCESSFUL));
	}

	private void addWorld(CommandSender s, String worldName) {
		if (s.isOp() || s.hasPermission("grapplinghook.cmd.setWorld") || s.hasPermission("grapplinghook.cmds.*")) {
			List<String> worlds = ConfigManager.getList(ConfigKeys.WORLDS);
			if (!worlds.contains(worldName)) {
				worlds.add(worldName);
				ConfigManager.set(ConfigKeys.WORLDS, worlds);

				s.sendMessage(ConfigManager.getMessage(ConfigKeys.ADD_WORLD_MSG).replace("%world%", worldName));
			} else
				s.sendMessage(ConfigManager.getMessage(ConfigKeys.ALREADY_IN_LIST_MSG).replace("%world%", worldName));
		} else
			s.sendMessage(ConfigManager.getNoPerm());
	}

	private void removeWorld(CommandSender s, String worldName) {
		if (s.isOp() || s.hasPermission("grapplinghook.cmd.removeWorld") || s.hasPermission("grapplinghook.cmds.*")) {
			List<String> worlds = ConfigManager.getList(ConfigKeys.WORLDS);
			if (worlds.contains(worldName)) {
				worlds.remove(worldName);
				ConfigManager.set(ConfigKeys.WORLDS, worlds);

				s.sendMessage(ConfigManager.getMessage(ConfigKeys.RM_WORLD_MSG).replace("%world%", worldName));
			} else
				s.sendMessage(ConfigManager.getMessage(ConfigKeys.NOT_IN_LIST_MSG).replace("%world%", worldName));
		} else
			s.sendMessage(ConfigManager.getNoPerm());
	}

	private void givePlayerGh(CommandSender s, String p) {
		if (p.isEmpty())
			return;

		Player player = Bukkit.getPlayer(p);
		if (player == null)
			s.sendMessage(ConfigManager.getMessage(ConfigKeys.UNKOWN_PLAYER_MSG).replace("%name%", p));
		else
			givePlayerGh(s, player);
	}

	private void givePlayerGh(CommandSender s, Player p) {
		if (p == null)
			return;
		if (s.isOp() || s.hasPermission("grapplinghook.give") || s.hasPermission("grapplinghook.cmds.*")) {
			p.getInventory().addItem(GrapplingHook.getGrapplingHook());
			s.sendMessage(ConfigManager.getMessage(ConfigKeys.SUCCESSFUL));
		}else
			s.sendMessage(ConfigManager.getNoPerm());
	}

	private void reloadConfig(CommandSender s) {
		if (s.isOp() || s.hasPermission("grapplinghook.reload") || s.hasPermission("grapplinghook.cmds.*")) {
			ConfigManager.reload();
			s.sendMessage(ConfigManager.getMessage(ConfigKeys.RELOAD_MSG));
		} else
			s.sendMessage(ConfigManager.getNoPerm());
	}

	private void sendHelp(CommandSender s, String page) {
		try {
			sendHelp(s, Integer.parseInt(page));
		}catch(NumberFormatException e) {
			s.sendMessage(ConfigManager.getMessage(ConfigKeys.ONLY_NUMBERS_MSG));
		}
	}
	
	private void sendHelp(CommandSender s, int i) {
		if (!(s.isOp() || s.hasPermission("grapplinghook.help") || s.hasPermission("grapplinghook.cmds.*"))) {
			s.sendMessage(ConfigManager.getNoPerm());
			return;
		}

		s.sendMessage("§3-------------->" + ConfigManager.getPrefix() + "§7 Page: " + i + "§3<--------------");
		if(i == 0) {
			s.sendMessage("§7/gh help [page] | §2To see more commands.");
		}else if(i == 1) {
			s.sendMessage("§7/gh reload | §2Reload the config of this plugin.");
			s.sendMessage("§7/gh resetConfig | §2Restores the default values of the config.");
			s.sendMessage("§7/gh allWorlds | §2Enable/Disable the GH in all worlds.");
			s.sendMessage("§7/gh setWorld/addWorld | §2Add the world in wich you are.");
			s.sendMessage("§7/gh removeWorld/rmWorld | §2Remove the world in which you are.");
		}else if(i == 2) {
			s.sendMessage("§7/gh give | §2Gives you the grappling hook.");
			s.sendMessage("§7/gh give [PlayerName]| §2Gives the player the grappling hook.");
			s.sendMessage("§7/gh setPullSound [sound] | §2Set the pull sound of the GH.");
			s.sendMessage("§7/gh setDestroySound [sound] | §2Set the sound of the GH when it gets destroyed.");
			s.sendMessage("§7/gh setUses [Number] | §2Changes the maxUses in the config.");
		}else if(i == 3) {
			s.sendMessage("§7/gh setGravity [Number] | §2Changes the garvity.");
			s.sendMessage("§7/gh setSpeed [Number] | §2Changes the speed/distance.");
			s.sendMessage("§7/gh setDestroyDelay [Number] | §2Changes the destroy delay(in ticks).");
			s.sendMessage("§7/gh setPrefix [String] | §2Changes the prefix(When using spaces wrap the string in: \").");
			s.sendMessage("§7/gh setName [String] | §2Changes the name(When using spaces wrap the string in: \").");
		}else if(i == 4) {
			s.sendMessage("§7/gh setCooldown [seconds] | §2Set the Cooldown of the GH.");
			s.sendMessage("§7/gh addUses [Number] | §2Adds to the uses of the GH in your hand new uses.");
			s.sendMessage("§7/gh addUses [PlayerName] [Number] | §2Adds to the uses of the GH in [PlayerName] hand new uses.");
			s.sendMessage("§7/gh setPlayerUses [Number] | §2Sets the uses of the GH in your hand.");
			s.sendMessage("§7/gh setPlayerUses [PlayerName] [Number] | §2Sets the uses of the GH in [PlayerName] hand.");
		}else if(i == 5) {
			s.sendMessage("§7/gh useAir | §2Enable/Disable useAir when throwing.");
			s.sendMessage("§7/gh crafting | §2Enable/Disable crafting.");
			s.sendMessage("§7/gh unlimited | §2Enable/Disable unlimited uses for the GH.");
			s.sendMessage("§7/gh enchanted | §2Enable/Disable the glow look for the GH.");
			s.sendMessage("§7/gh noFallDamage | §2Enable/Disable falldamage when you land on the ground.");
		}else if(i == 6) {
			s.sendMessage("§7/gh setDestroy | §2Enable/Disable destroying the GH when no more uses left.");
			s.sendMessage("§7/gh useFloatingBlocks | §2Enable/Disable using floating blocks in the air as a pull object.");
			s.sendMessage("§7/gh unbreakable | §2Enable/Disable unbreakability for the GH.");
			s.sendMessage("§7/gh allRods | §2Enable/Disable all fishingrods as grappling hooks.");
		}else {
			s.sendMessage("§7This page is invalid.");
			s.sendMessage("§7/gh help [page] | §2To see more commands.");
		}
		s.sendMessage(
				"\n§7For more information look on the spigot website:\n§7https://www.spigotmc.org/resources/grappling-hook.55955/");
	}
}
