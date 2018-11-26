package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandLeaveEvent;
import me.goodandevil.skyblock.island.Location;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Role;
import me.goodandevil.skyblock.message.MessageManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.scoreboard.Scoreboard;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;
import me.goodandevil.skyblock.sound.SoundManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class LeaveCommand extends SubCommand {

	private final SkyBlock skyblock;
	private String info;
	
	public LeaveCommand(SkyBlock skyblock) {
		this.skyblock = skyblock;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		PlayerDataManager playerDataManager = skyblock.getPlayerDataManager();
		ScoreboardManager scoreboardManager = skyblock.getScoreboardManager();
		MessageManager messageManager = skyblock.getMessageManager();
		IslandManager islandManager = skyblock.getIslandManager();
		SoundManager soundManager = skyblock.getSoundManager();
		FileManager fileManager = skyblock.getFileManager();
		
		PlayerData playerData = playerDataManager.getPlayerData(player);
		
		Config languageConfig = fileManager.getConfig(new File(skyblock.getDataFolder(), "language.yml"));
		
		if (islandManager.hasIsland(player)) {
			me.goodandevil.skyblock.island.Island island = islandManager.getIsland(playerData.getOwner());
			
			if (island.isRole(Role.Owner, player.getUniqueId())) {
				messageManager.sendMessage(player, languageConfig.getFileConfiguration().getString("Command.Island.Leave.Owner.Message"));
				soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			} else {
				IslandLeaveEvent islandLeaveEvent = new IslandLeaveEvent(player, island);
				Bukkit.getServer().getPluginManager().callEvent(islandLeaveEvent);
				
				if (!islandLeaveEvent.isCancelled()) {
					for (Location.World worldList : Location.World.values()) {
						if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, Location.Environment.Island), island.getRadius())) {
							LocationUtil.teleportPlayerToSpawn(player);
							
							break;
						}
					}
					
					if (island.isRole(Role.Member, player.getUniqueId())) {
						island.removeRole(Role.Member, player.getUniqueId());
					} else if (island.isRole(Role.Operator, player.getUniqueId())) {
						island.removeRole(Role.Operator, player.getUniqueId());
					}
					
					island.save();
					
					playerData.setPlaytime(0);
					playerData.setOwner(null);
					playerData.setMemberSince(null);
					playerData.setChat(false);
					playerData.save();
					
					List<UUID> islandMembersOnline = islandManager.getMembersOnline(island);
					
					if (islandMembersOnline.size() == 1) {
						for (UUID islandMembersOnlineList : islandMembersOnline) {
							if (!islandMembersOnlineList.equals(player.getUniqueId())) {
								Player targetPlayer = Bukkit.getServer().getPlayer(islandMembersOnlineList);
								PlayerData targetPlayerData = playerDataManager.getPlayerData(targetPlayer);
								
								if (targetPlayerData.isChat()) {
									targetPlayerData.setChat(false);
									messageManager.sendMessage(targetPlayer, fileManager.getConfig(new File(skyblock.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Chat.Untoggled.Message"));	
								}
							}
						}
					}
					
					islandManager.unloadIsland(player.getUniqueId());
					
					for (Player all : Bukkit.getOnlinePlayers()) {
						if (!all.getUniqueId().equals(player.getUniqueId())) {
							if (island.isRole(Role.Member, all.getUniqueId()) || island.isRole(Role.Operator, all.getUniqueId()) || island.isRole(Role.Owner, all.getUniqueId())) {
								all.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Leave.Left.Broadcast.Message").replace("%player", player.getName())));
								soundManager.playSound(all, Sounds.IRONGOLEM_HIT.bukkitSound(), 5.0F, 5.0F);
								
								if (scoreboardManager != null) {
									if (island.getRole(Role.Member).size() == 0 && island.getRole(Role.Operator).size() == 0) {
										Scoreboard scoreboard = scoreboardManager.getScoreboard(all);
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
						}
					}
					
					messageManager.sendMessage(player, languageConfig.getFileConfiguration().getString("Command.Island.Leave.Left.Sender.Message"));
					soundManager.playSound(player, Sounds.IRONGOLEM_HIT.bukkitSound(), 5.0F, 5.0F);
					
					if (scoreboardManager != null) {
						Scoreboard scoreboard = scoreboardManager.getScoreboard(player);
						scoreboard.cancel();
						scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Tutorial.Displayname")));
						scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Tutorial.Displaylines"));
						scoreboard.run();
					}
				}
			}
		} else {
			messageManager.sendMessage(player, languageConfig.getFileConfiguration().getString("Command.Island.Leave.Member.Message"));
			soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "leave";
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
