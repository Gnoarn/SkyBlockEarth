package me.goodandevil.skyblock.playerdata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.scoreboard.Scoreboard;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.world.LocationUtil;
import me.goodandevil.skyblock.visit.Visit;
import me.goodandevil.skyblock.world.WorldManager;

public class PlayerDataManager {

	private final Main plugin;
	private Map<UUID, PlayerData> playerDataStorage = new HashMap<>();
	
	public PlayerDataManager(Main plugin) {
		this.plugin = plugin;
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			loadPlayerData(all);
			
			if (!hasPlayerData(all)) {
				createPlayerData(all);
				loadPlayerData(all);
			}
			
			storeIsland(all);
		}
	}
	
	public void onDisable() {
		for (UUID playerDataStorageList : playerDataStorage.keySet()) {
			playerDataStorage.get(playerDataStorageList).save();
		}
	}
	
	public void createPlayerData(Player player) {
		Config playerDataConfig = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/player-data"), player.getUniqueId() + ".yml"));
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml"));
		
		FileConfiguration playerDataConfigLoad = playerDataConfig.getFileConfiguration();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		try {
			Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
			Method getProfileMethod = entityPlayer.getClass().getMethod("getProfile", new Class<?>[0]);
			GameProfile gameProfile = (GameProfile) getProfileMethod.invoke(entityPlayer);
			Property property = gameProfile.getProperties().get("textures").iterator().next();
			playerDataConfigLoad.set("Texture.Signature", property.getSignature());
			playerDataConfigLoad.set("Texture.Value", property.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		playerDataConfigLoad.set("Statistics.Money.Balance", configLoad.getInt("PlayerData.Money.Balance"));
		playerDataConfigLoad.set("Statistics.Island.Playtime", 0);
		
		try {
			playerDataConfigLoad.save(playerDataConfig.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadPlayerData(Player player) {
		if (plugin.getFileManager().isFileExist(new File(plugin.getDataFolder().toString() + "/player-data", player.getUniqueId().toString() + ".yml"))) {
			PlayerData playerData = new PlayerData(player);
			playerDataStorage.put(player.getUniqueId(), playerData);
		}
	}
	
	public void unloadPlayerData(Player player) {
		if (hasPlayerData(player)) {
			plugin.getFileManager().unloadConfig(new File(new File(plugin.getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
			playerDataStorage.remove(player.getUniqueId());
		}
	}
	
	public void savePlayerData(Player player) {
		if (hasPlayerData(player)) {
			Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
			
			try {
				config.getFileConfiguration().save(config.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<UUID, PlayerData> getPlayerData() {
		return playerDataStorage;
	}

	public PlayerData getPlayerData(Player player) {
		if (hasPlayerData(player)) {
			return playerDataStorage.get(player.getUniqueId());
		}
		
		return null;
	}

	public boolean hasPlayerData(Player player) {
		return playerDataStorage.containsKey(player.getUniqueId());
	}
	
	public void storeIsland(Player player) {
		WorldManager worldManager = plugin.getWorldManager();
		
		if (hasPlayerData(player) && (player.getWorld().getName().equals(worldManager.getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(worldManager.getWorld(IslandLocation.World.Nether).getName()))) {
			IslandManager islandManager = plugin.getIslandManager();
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						PlayerData playerData = getPlayerData(player);
						playerData.setIsland(island.getOwnerUUID());
						
						if (worldList == IslandLocation.World.Normal) {
							if (!island.isWeatherSynchronised()) {
			    				player.setPlayerTime(island.getTime(), false);
			    				player.setPlayerWeather(island.getWeather());
							}
						}
						
						ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
						
						if (scoreboardManager != null) {
							if (!island.isRole(IslandRole.Member, player.getUniqueId()) && !island.isRole(IslandRole.Operator, player.getUniqueId()) && !island.isRole(IslandRole.Owner, player.getUniqueId())) {
								Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
								FileConfiguration configLoad = config.getFileConfiguration();
								
								for (Player all : Bukkit.getOnlinePlayers()) {
									PlayerData targetPlayerData = getPlayerData(all);
									
									if (targetPlayerData.getOwner().equals(island.getOwnerUUID())) {
										Scoreboard scoreboard = scoreboardManager.getScoreboard(all);
										scoreboard.cancel();
										
										if ((island.getRole(IslandRole.Member).size() + island.getRole(IslandRole.Operator).size() + 1) == 1) {
											scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Solo.Displayname")));
											scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Solo.Occupied.Displaylines"));
										} else {
											scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Team.Displayname")));
											scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Team.Occupied.Displaylines"));
											
											Map<String, String> displayVariables = new HashMap<>();
											displayVariables.put("%owner", configLoad.getString("Scoreboard.Island.Team.Word.Owner"));
											displayVariables.put("%operator", configLoad.getString("Scoreboard.Island.Team.Word.Operator"));
											displayVariables.put("%member", configLoad.getString("Scoreboard.Island.Team.Word.Member"));
											
											scoreboard.setDisplayVariables(displayVariables);
										}
										
										scoreboard.run();
									}
								}
							}
						}
						
						return;
					}
				}
			}
			
			HashMap<UUID, Visit> visitIslands = plugin.getVisitManager().getIslands();
			
			// TODO Check if player is banned from Island.
			
			for (UUID visitIslandList : visitIslands.keySet()) {
				Visit visit = visitIslands.get(visitIslandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), visit.getLocation(worldList), 85)) {
						if (visit.isOpen()) {
							PlayerData playerData = getPlayerData(player);
							playerData.setIsland(visitIslandList);
							
							islandManager.loadIsland(visitIslandList);
						} else {
							FileManager fileManager = plugin.getFileManager();
							
							Config config = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
							FileConfiguration configLoad = config.getFileConfiguration();
							
				    		Player targetPlayer = Bukkit.getServer().getPlayer(visitIslandList);
				    		String targetPlayerName;
				    		
				    		if (targetPlayer == null) {
				    			targetPlayerName = new OfflinePlayer(visitIslandList).getName();
				    		} else {
				    			targetPlayerName = targetPlayer.getName();
				    		}
							
				    		LocationUtil.teleportPlayerToSpawn(player);
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Visit.Closed.Island.Message").replace("%player", targetPlayerName)));
						}
						
						return;
					}
				}
			}
		}
	}
}
