package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandKickEvent;
import me.goodandevil.skyblock.island.Location;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Role;
import me.goodandevil.skyblock.island.Settings;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.scoreboard.Scoreboard;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class KickCommand extends SubCommand {

	private final Main plugin;
	private String info;
	
	public KickCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
		ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
		IslandManager islandManager = plugin.getIslandManager();
		FileManager fileManager = plugin.getFileManager();
		
		PlayerData playerData = playerDataManager.getPlayerData(player);
		
		Config languageConfig = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
		
		if (args.length == 1) {
			if (islandManager.hasIsland(player)) {
				me.goodandevil.skyblock.island.Island island = islandManager.getIsland(playerData.getOwner());
				
				boolean isOpen = island.isOpen();
				
				if (island.isRole(Role.Owner, player.getUniqueId()) || (island.isRole(Role.Operator, player.getUniqueId()) && island.getSetting(Settings.Role.Operator, "Kick").getStatus())) {
					UUID targetPlayerUUID = null;
					String targetPlayerName = null;
					
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					
					List<UUID> islandMembers = island.getRole(Role.Member), islandOperators = island.getRole(Role.Operator), islandVisitors = island.getVisitors();
					
					if (targetPlayer == null) {
						OfflinePlayer targetPlayerOffline = new OfflinePlayer(args[0]);
						targetPlayerUUID = targetPlayerOffline.getUniqueId();
						targetPlayerName = targetPlayerOffline.getName();
					} else {
						targetPlayerUUID = targetPlayer.getUniqueId();
						targetPlayerName = targetPlayer.getName();
					}
					
					if (targetPlayerUUID.equals(player.getUniqueId())) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Yourself.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else if (islandOperators.contains(player.getUniqueId()) && islandOperators.contains(targetPlayerUUID)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Role.Operator.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else if (island.getOwnerUUID().equals(targetPlayerUUID)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Role.Owner.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else if (isOpen && islandVisitors.contains(targetPlayerUUID) && targetPlayer != null) {
						IslandKickEvent islandKickEvent = new IslandKickEvent(island, Role.Visitor, targetPlayerUUID, player);
						Bukkit.getServer().getPluginManager().callEvent(islandKickEvent);
						
						if (!islandKickEvent.isCancelled()) {
							LocationUtil.teleportPlayerToSpawn(targetPlayer);
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Kicked.Sender.Message").replace("%player", targetPlayerName)));
							player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
							
							targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Kicked.Target.Message").replace("%player", player.getName())));
							targetPlayer.playSound(targetPlayer.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
						}
					} else if (islandMembers.contains(targetPlayerUUID) || islandOperators.contains(targetPlayerUUID)) {
						Role islandRole = Role.Member;
						
						if (islandOperators.contains(targetPlayerUUID)) {
							islandRole = Role.Operator;
						}
						
						IslandKickEvent islandKickEvent = new IslandKickEvent(island, islandRole, targetPlayerUUID, player);
						Bukkit.getServer().getPluginManager().callEvent(islandKickEvent);
						
						if (!islandKickEvent.isCancelled()) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Kicked.Sender.Message").replace("%player", targetPlayerName)));
							player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
							
							if (targetPlayer == null) {
								Config config = fileManager.getConfig(new File(new File(plugin.getDataFolder().toString() + "/player-data"), targetPlayerUUID.toString() + ".yml"));
								FileConfiguration configLoad = config.getFileConfiguration();
								
								configLoad.set("Statistics.Island.Playtime", null);
								configLoad.set("Statistics.Island.Join", null);
								configLoad.set("Island.Owner", null);
								
								try {
									configLoad.save(config.getFile());
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Kicked.Target.Message").replace("%player", player.getName())));
								targetPlayer.playSound(targetPlayer.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
								
								for (Location.World worldList : Location.World.values()) {
									if (LocationUtil.isLocationAtLocationRadius(targetPlayer.getLocation(), island.getLocation(worldList, Location.Environment.Island), 85)) {
										LocationUtil.teleportPlayerToSpawn(targetPlayer);
										
										break;
									}
								}
								
								if (scoreboardManager != null) {
									Scoreboard scoreboard = scoreboardManager.getScoreboard(targetPlayer);
									scoreboard.cancel();
									scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Tutorial.Displayname")));
									scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Tutorial.Displaylines"));
									scoreboard.run();	
								}
								
								playerData = playerDataManager.getPlayerData(targetPlayer);
								playerData.setPlaytime(0);
								playerData.setMemberSince(null);
								playerData.setOwner(null);
								playerData.setChat(false);
								playerData.save();
							}
							
							if (islandMembers.contains(targetPlayerUUID)) {
								island.removeRole(Role.Member, targetPlayerUUID);
							} else if (islandOperators.contains(targetPlayerUUID)) {
								island.removeRole(Role.Operator, targetPlayerUUID);
							}
							
							island.save();
							
							List<UUID> islandMembersOnline = islandManager.getMembersOnline(island);
							
							if (islandMembersOnline.size() == 1) {
								for (UUID islandMembersOnlineList : islandMembersOnline) {
									if (!islandMembersOnlineList.equals(player.getUniqueId())) {
										targetPlayer = Bukkit.getServer().getPlayer(islandMembersOnlineList);
										PlayerData targetPlayerData = playerDataManager.getPlayerData(targetPlayer);
										
										if (targetPlayerData.isChat()) {
											targetPlayerData.setChat(false);
											targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Chat.Untoggled.Message")));	
										}
									}
								}
							}
							
							if (scoreboardManager != null) {
								if (island.getRole(Role.Member).size() == 0 && island.getRole(Role.Operator).size() == 0) {
									Scoreboard scoreboard = scoreboardManager.getScoreboard(player);
									scoreboard.cancel();
									scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Island.Solo.Displayname")));
									
									if (island.getVisitors().size() == 0) {
										scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Solo.Empty.Displaylines"));
									} else {
										scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Solo.Occupied.Displaylines"));
									}
									
									scoreboard.run();
								}	
							}
						}
					} else {
						if (isOpen) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Occupant.Visit.Open.Message")));
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Occupant.Visit.Closed.Message")));	
						}
						
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					}
				} else {
					if (isOpen) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Permission.Visit.Open.Message")));
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Permission.Visit.Closed.Message")));
					}
					
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Owner.Message")));
				player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			}	
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Kick.Invalid.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}
	
	@Override
	public String getName() {
		return "kick";
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
