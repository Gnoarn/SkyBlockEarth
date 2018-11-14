package me.goodandevil.skyblock.command.commands.admin;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.utils.ChatComponent;
import me.goodandevil.skyblock.utils.structure.StructureUtil;
import me.goodandevil.skyblock.utils.version.Sounds;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;

public class StructureCommand extends SubCommand {

	private final SkyBlockEarth plugin;
	private String info;
	
	public StructureCommand(SkyBlockEarth plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
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
					
					player.spigot().sendMessage(new ChatComponent(prefix.replace("%info", ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Tool.Info.Message"))) + "/island admin structure tool" + suffix.replace("%info", ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Tool.Info.Message"))), false, null, null, new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Tool.Info.Message"))).create())).getTextComponent());
					player.spigot().sendMessage(new ChatComponent(prefix.replace("%info", ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Info.Message"))) + "/island admin structure save" + suffix.replace("%info", ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Info.Message"))), false, null, null, new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Info.Message"))).create())).getTextComponent());
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', helpLines));
				}
			}
			
			player.playSound(player.getLocation(), Sounds.ARROW_HIT.bukkitSound(), 1.0F, 1.0F);
		} else {
			if (args[0].equalsIgnoreCase("tool")) {
				if (player.hasPermission("skyblock.admin.structure.tool") || player.hasPermission("skyblock.admin.structure.*") || player.hasPermission("skyblock.admin.*") || player.hasPermission("skyblock.*")) {
					try {
						ItemStack is = StructureUtil.getTool();
						
						for (ItemStack itemList : player.getInventory().getContents()) {
							if (itemList != null) {
								if ((itemList.getType() == is.getType()) && (itemList.hasItemMeta()) && (itemList.getItemMeta().getDisplayName().equals(is.getItemMeta().getDisplayName()))) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Tool.Inventory.Message")));
									player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
									
									return;
								}
							}
						}
						
						player.getInventory().addItem(is);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Tool.Equiped.Message")));
						player.playSound(player.getLocation(), Sounds.CHICKEN_EGG_POP.bukkitSound(), 1.0F, 1.0F);
					} catch (Exception e) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Structure.Tool.Material.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						
						Bukkit.getServer().getLogger().log(Level.WARNING, "SkyBlock | Error: The defined material in the configuration file for the Structure selection tool could not be found.");
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Tool.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				}
			} else if (args[0].equalsIgnoreCase("save")) {
				if (player.hasPermission("skyblock.admin.structure.save") || player.hasPermission("skyblock.admin.structure.*") || player.hasPermission("skyblock.admin.*") || player.hasPermission("skyblock.*")) {
					if (args.length == 2) {
						PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
						
						Location position1Location = playerData.getArea().getPosition(1);
						Location position2Location = playerData.getArea().getPosition(2);
						
						if (position1Location == null && position2Location == null) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Position.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else if ((position1Location == null && position2Location != null) || (position1Location != null && position2Location == null)) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Complete.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else {
							if (position1Location.getWorld().getName().equals(position2Location.getWorld().getName())) {
		                        try {
		                            File configFile = new File(plugin.getDataFolder().toString() + "/structures/" + args[1] + ".structure");
		                            StructureUtil.saveStructure(configFile, player.getLocation(), StructureUtil.getFixedLocations(position1Location, position2Location));
									
		                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Saved.Successful.Message").replace("%name", args[1])));
									player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
		                        } catch(Exception e) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Saved.Failed.Message")));
									player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		                            e.printStackTrace();
		                        }
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.World.Message")));
								player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
							}
						}
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Save.Invalid.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Structure.Tool.Save.Message")));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				}
			}
		}
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
	public Type getType() {
		return CommandManager.Type.Admin;
	}
}
