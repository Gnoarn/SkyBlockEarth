package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandLeaveEvent;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.scoreboard.Scoreboard;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class LeaveCommand extends SubCommand {

	private String info;
	
	@Override
	public void onCommand(Player player, String[] args) {
		PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
		IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
		FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
		
		PlayerData playerData = playerDataManager.getPlayerData(player);
		
		Config languageConfig = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		
		if (islandManager.hasIsland(player)) {
			me.goodandevil.skyblock.island.Island island = islandManager.getIsland(playerData.getOwner());
			
			if (island.isRole(IslandRole.Owner, player.getUniqueId())) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Leave.Owner.Message")));
				player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			} else {
				IslandLeaveEvent islandLeaveEvent = new IslandLeaveEvent(player, island);
				Bukkit.getServer().getPluginManager().callEvent(islandLeaveEvent);
				
				if (!islandLeaveEvent.isCancelled()) {
					for (IslandLocation.World worldList : IslandLocation.World.values()) {
						if (LocationUtil.getInstance().isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
							LocationUtil.getInstance().teleportPlayerToSpawn(player);
							
							break;
						}
					}
					
					if (island.isRole(IslandRole.Member, player.getUniqueId())) {
						island.removeRole(IslandRole.Member, player.getUniqueId());
					} else if (island.isRole(IslandRole.Operator, player.getUniqueId())) {
						island.removeRole(IslandRole.Operator, player.getUniqueId());
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
									targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Chat.Untoggled.Message")));	
								}
							}
						}
					}
					
					islandManager.unloadIsland(player.getUniqueId());
					
					for (Player all : Bukkit.getOnlinePlayers()) {
						if (!all.getUniqueId().equals(player.getUniqueId())) {
							if (island.isRole(IslandRole.Member, all.getUniqueId()) || island.isRole(IslandRole.Operator, all.getUniqueId()) || island.isRole(IslandRole.Owner, all.getUniqueId())) {
								all.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Leave.Left.Broadcast.Message").replace("%player", player.getName())));
								all.playSound(all.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 5.0F, 5.0F);
								
								if (island.getRole(IslandRole.Member).size() == 0 && island.getRole(IslandRole.Operator).size() == 0) {
									Scoreboard scoreboard = ((ScoreboardManager) Main.getInstance(Main.Instance.ScoreboardManager)).getScoreboard(all);
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
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Leave.Left.Sender.Message")));
					player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 5.0F, 5.0F);
					
					Scoreboard scoreboard = ((ScoreboardManager) Main.getInstance(Main.Instance.ScoreboardManager)).getScoreboard(player);
					scoreboard.cancel();
					scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Tutorial.Displayname")));
					scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Tutorial.Displaylines"));
					scoreboard.run();
				}
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Leave.Member.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
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
