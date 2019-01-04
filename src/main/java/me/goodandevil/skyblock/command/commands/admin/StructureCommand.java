package me.goodandevil.skyblock.command.commands.admin;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.message.MessageManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.sound.SoundManager;
import me.goodandevil.skyblock.utils.ChatComponent;
import me.goodandevil.skyblock.utils.structure.StructureUtil;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;

public class StructureCommand extends SubCommand {

	private final SkyBlock skyblock;
	private String info;

	public StructureCommand(SkyBlock skyblock) {
		this.skyblock = skyblock;
	}

	@Override
	public void onCommandByPlayer(Player player, String[] args) {
		MessageManager messageManager = skyblock.getMessageManager();
		SoundManager soundManager = skyblock.getSoundManager();

		Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();

		if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help"))) {
			for (String helpLines : configLoad.getStringList("Command.Island.Help.Lines")) {
				if (helpLines.contains("%type")) {
					helpLines = helpLines.replace("%type", "Structure");
				}

				if (helpLines.contains("%commands")) {
					String[] sections = helpLines.split("%commands");
					String prefix = "", suffix = "";

					if (sections.length >= 1) {
						prefix = ChatColor.translateAlternateColorCodes('&', sections[0]);
					}

					if (sections.length == 2) {
						suffix = ChatColor.translateAlternateColorCodes('&', sections[1]);
					}

					player.spigot()
							.sendMessage(
									new ChatComponent(
											prefix.replace("%info",
													ChatColor.translateAlternateColorCodes('&', configLoad.getString(
															"Command.Island.Admin.Structure.Tool.Info.Message")))
													+ "/island admin structure tool"
													+ suffix.replace("%info", ChatColor.translateAlternateColorCodes(
															'&',
															configLoad.getString(
																	"Command.Island.Admin.Structure.Tool.Info.Message"))),
											false, null, null,
											new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
													ChatColor.translateAlternateColorCodes('&', configLoad.getString(
															"Command.Island.Admin.Structure.Tool.Info.Message")))
																	.create())).getTextComponent());
					player.spigot()
							.sendMessage(
									new ChatComponent(
											prefix.replace("%info",
													ChatColor.translateAlternateColorCodes('&', configLoad.getString(
															"Command.Island.Admin.Structure.Save.Info.Message")))
													+ "/island admin structure save"
													+ suffix.replace("%info", ChatColor.translateAlternateColorCodes(
															'&',
															configLoad.getString(
																	"Command.Island.Admin.Structure.Save.Info.Message"))),
											false, null, null,
											new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
													ChatColor.translateAlternateColorCodes('&', configLoad.getString(
															"Command.Island.Admin.Structure.Save.Info.Message")))
																	.create())).getTextComponent());
				} else {
					messageManager.sendMessage(player, helpLines);
				}
			}

			soundManager.playSound(player, Sounds.ARROW_HIT.bukkitSound(), 1.0F, 1.0F);

			return;
		} else {
			if (args[0].equalsIgnoreCase("tool")) {
				if (player.hasPermission("skyblockearth.admin.structure.tool")
						|| player.hasPermission("skyblockearth.admin.structure.*")
						|| player.hasPermission("skyblockearth.admin.*") || player.hasPermission("skyblockearth.*")) {
					try {
						ItemStack is = StructureUtil.getTool();

						for (ItemStack itemList : player.getInventory().getContents()) {
							if (itemList != null) {
								if ((itemList.getType() == is.getType()) && (itemList.hasItemMeta()) && (itemList
										.getItemMeta().getDisplayName().equals(is.getItemMeta().getDisplayName()))) {
									messageManager.sendMessage(player, configLoad
											.getString("Command.Island.Admin.Structure.Tool.Inventory.Message"));
									soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);

									return;
								}
							}
						}

						player.getInventory().addItem(is);
						messageManager.sendMessage(player,
								configLoad.getString("Command.Island.Admin.Structure.Tool.Equiped.Message"));
						soundManager.playSound(player, Sounds.CHICKEN_EGG_POP.bukkitSound(), 1.0F, 1.0F);
					} catch (Exception e) {
						messageManager.sendMessage(player,
								configLoad.getString("Island.Structure.Tool.Material.Message"));
						soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);

						Bukkit.getServer().getLogger().log(Level.WARNING,
								"SkyBlock | Error: The defined material in the configuration file for the Structure selection tool could not be found.");
					}
				} else {
					messageManager.sendMessage(player,
							configLoad.getString("Command.Island.Admin.Structure.Tool.Permission.Message"));
					soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				}

				return;
			} else if (args[0].equalsIgnoreCase("save")) {
				if (player.hasPermission("skyblockearth.admin.structure.save")
						|| player.hasPermission("skyblockearth.admin.structure.*")
						|| player.hasPermission("skyblockearth.admin.*") || player.hasPermission("skyblockearth.*")) {
					if (args.length == 2) {
						PlayerData playerData = skyblock.getPlayerDataManager().getPlayerData(player);

						Location position1Location = playerData.getArea().getPosition(1);
						Location position2Location = playerData.getArea().getPosition(2);

						if (position1Location == null && position2Location == null) {
							messageManager.sendMessage(player,
									configLoad.getString("Command.Island.Admin.Structure.Save.Position.Message"));
							soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else if ((position1Location == null && position2Location != null)
								|| (position1Location != null && position2Location == null)) {
							messageManager.sendMessage(player,
									configLoad.getString("Command.Island.Admin.Structure.Save.Complete.Message"));
							soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else if (!position1Location.getWorld().getName()
								.equals(position2Location.getWorld().getName())) {
							messageManager.sendMessage(player, configLoad
									.getString("Command.Island.Admin.Structure.Save.Selection.World.Message"));
							soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else if (!player.getWorld().getName().equals(position1Location.getWorld().getName())) {
							messageManager.sendMessage(player,
									configLoad.getString("Command.Island.Admin.Structure.Save.Player.World.Message"));
							soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else if (!LocationUtil.isInsideArea(player.getLocation(), position1Location,
								position2Location)) {
							messageManager.sendMessage(player,
									configLoad.getString("Command.Island.Admin.Structure.Save.Player.Area.Message"));
							soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else {
							try {
								File configFile = new File(
										skyblock.getDataFolder().toString() + "/structures/" + args[1] + ".structure");
								StructureUtil.saveStructure(configFile, player.getLocation(),
										StructureUtil.getFixedLocations(position1Location, position2Location));

								messageManager.sendMessage(player,
										configLoad
												.getString(
														"Command.Island.Admin.Structure.Save.Saved.Successful.Message")
												.replace("%name", args[1]));
								soundManager.playSound(player, Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
							} catch (Exception e) {
								messageManager.sendMessage(player, configLoad
										.getString("Command.Island.Admin.Structure.Save.Saved.Failed.Message"));
								soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
								e.printStackTrace();
							}
						}
					} else {
						messageManager.sendMessage(player,
								configLoad.getString("Command.Island.Admin.Structure.Save.Invalid.Message"));
						soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					}
				} else {
					messageManager.sendMessage(player,
							configLoad.getString("Command.Island.Admin.Structure.Tool.Save.Message"));
					soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				}

				return;
			}

			messageManager.sendMessage(player, configLoad.getString("Command.Island.Argument.Unrecognised.Message"));
			soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
		sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
	}

	@Override
	public String getName() {
		return "structure";
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public SubCommand setInfo(String info) {
		this.info = info;

		return this;
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String[] getArguments() {
		return new String[] { "tool", "save" };
	}

	@Override
	public Type getType() {
		return CommandManager.Type.Admin;
	}
}
