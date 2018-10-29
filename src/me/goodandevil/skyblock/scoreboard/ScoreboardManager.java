package me.goodandevil.skyblock.scoreboard;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;

public class ScoreboardManager {

	private HashMap<UUID, Scoreboard> scoreboardStorage = new HashMap<UUID, Scoreboard>();
	
	public ScoreboardManager() {
		new BukkitRunnable() {
			public void run() {
				PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
				IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
				FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
				
				if (fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Scoreboard.Enable")) {
					Config languageConfig = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
					
					for (Player all : Bukkit.getOnlinePlayers()) {
						Scoreboard scoreboard = new Scoreboard(all);
						
						if (islandManager.hasIsland(all)) {
							Island island = islandManager.getIsland(playerDataManager.getPlayerData(all).getOwner());
							
							if (island.getRole(IslandRole.Member).size() == 0 && island.getRole(IslandRole.Operator).size() == 0) {
								scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Island.Solo.Displayname")));
								
								if (island.getVisitors().size() == 0) {
									scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Solo.Empty.Displaylines"));
								} else {
									scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Solo.Occupied.Displaylines"));
								}
							} else {
								scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Displayname")));
								
								if (island.getVisitors().size() == 0) {
									scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Team.Empty.Displaylines"));
								} else {
									scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Team.Occupied.Displaylines"));
								}
								
								HashMap<String, String> displayVariables = new HashMap<String, String>();
								displayVariables.put("%owner", languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Word.Owner"));
								displayVariables.put("%operator", languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Word.Operator"));
								displayVariables.put("%member", languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Word.Member"));
								
								scoreboard.setDisplayVariables(displayVariables);
							}
						} else {
							scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Tutorial.Displayname")));
							scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Tutorial.Displaylines"));
						}
						
						scoreboard.run();
						storeScoreboard(all, scoreboard);
					}	
				}
			}
		}.runTaskLater(Main.getInstance(), 20L);
	}
	
	public void storeScoreboard(Player player, Scoreboard scoreboard) {
		scoreboardStorage.put(player.getUniqueId(), scoreboard);
	}
	
	public void unloadPlayer(Player player) {
		if (hasScoreboard(player)) {
			scoreboardStorage.remove(player.getUniqueId());
		}
	}

	public Scoreboard getScoreboard(Player player) {
		if (hasScoreboard(player)) {
			return scoreboardStorage.get(player.getUniqueId());
		}
		
		return null;
	}

	public boolean hasScoreboard(Player player) {
		return scoreboardStorage.containsKey(player.getUniqueId());
	}
}
