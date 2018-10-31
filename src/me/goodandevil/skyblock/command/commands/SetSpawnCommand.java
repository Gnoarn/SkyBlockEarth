package me.goodandevil.skyblock.command.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.island.IslandSettings;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class SetSpawnCommand extends SubCommand {

	private final Main plugin;
	
	private String info;
	private IslandLocation.Environment locationEnvironment;
	
	public SetSpawnCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		IslandManager islandManager = plugin.getIslandManager();
		FileManager fileManager = plugin.getFileManager();
		
		Config config = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (args.length == 1) {
			if (islandManager.hasIsland(player)) {
				if (args[0].equalsIgnoreCase("Main")) {
					locationEnvironment = IslandLocation.Environment.Main;
				} else if (args[0].equalsIgnoreCase("Visitor")) {
					locationEnvironment = IslandLocation.Environment.Visitor;
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Spawn.Message")));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					
					return;
				}
				
				me.goodandevil.skyblock.island.Island island = islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
				
				if (island.isRole(IslandRole.Operator, player.getUniqueId()) || island.isRole(IslandRole.Owner, player.getUniqueId())) {
					if ((island.isRole(IslandRole.Operator, player.getUniqueId()) && (island.getSetting(IslandSettings.Role.Operator, locationEnvironment.name() + "Spawn").getStatus())) || island.isRole(IslandRole.Owner, player.getUniqueId())) {
						for (IslandLocation.World worldList : IslandLocation.World.values()) {
							if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
								Location location = player.getLocation();
								
								if (fileManager.getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Spawn.Protection")) {
									if (location.clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.AIR || location.clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Materials.LEGACY_PISTON_MOVING_PIECE.getPostMaterial()) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Protection.Block.Message")));
										player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
										
										return;
									} else if (location.getY() - location.getBlockY() != 0.0D) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Protection.Ground.Message")));
										player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
										
										return;
									} else if (location.getBlock().isLiquid() || location.clone().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid()) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Protection.Liquid.Message")));
										player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
										
										return;
									} else if (location.getBlock().getType() == Materials.NETHER_PORTAL.parseMaterial() || location.clone().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Materials.NETHER_PORTAL.parseMaterial()) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Protection.Portal.Message")));
										player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
										
										return;
									} else {
										if (LocationUtil.isLocationCentreOfBlock(location)) {
											new BukkitRunnable() {
												public void run() {
													if (location.getBlock().getType() != Material.AIR) {
														location.getWorld().dropItemNaturally(location, new ItemStack(location.getBlock().getType()));
													}
													
													if (location.clone().add(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR) {
														location.getWorld().dropItemNaturally(location.clone().add(0.0D, 1.0D, 0.0D), new ItemStack(location.clone().add(0.0D, 1.0D, 0.0D).getBlock().getType()));
													}
													
													islandManager.removeSpawnProtection(island.getLocation(worldList, locationEnvironment));
													islandManager.setSpawnProtection(location);
												}
											}.runTask(plugin);
										} else {
											player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Protection.Centre.Message")));
											player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
											
											return;
										}
									}
								}
								
								island.setLocation(worldList, locationEnvironment, location);
								
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Set.Message").replace("%spawn", locationEnvironment.name().toLowerCase())));
								player.playSound(player.getLocation(), Sounds.NOTE_PLING.bukkitSound(), 1.0F, 1.0F);
								
								return;
							}
						}
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Island.Message").replace("%spawn", locationEnvironment.name().toLowerCase())));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Permission.Message").replace("%spawn", locationEnvironment.name().toLowerCase())));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Role.Message").replace("%spawn", locationEnvironment.name().toLowerCase())));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Owner.Message")));
				player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.SetSpawn.Invalid.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "setspawn";
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
		return CommandManager.Type.Default;
	}
}
