package me.goodandevil.skyblock.playerdata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
import me.goodandevil.skyblock.visit.VisitManager;
import me.goodandevil.skyblock.world.WorldManager;

public class PlayerDataManager {

	private HashMap<UUID, PlayerData> playerDataStorage = new HashMap<UUID, PlayerData>();
	
	public PlayerDataManager() {
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
		Config playerDataConfig = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), player.getUniqueId() + ".yml"));
		Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml"));
		
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
		if (((FileManager) Main.getInstance(Main.Instance.FileManager)).isFileExist(new File(Main.getInstance().getDataFolder().toString() + "/player-data", player.getUniqueId().toString() + ".yml"))) {
			PlayerData playerData = new PlayerData(player);
			playerDataStorage.put(player.getUniqueId(), playerData);
		}
	}
	
	public void unloadPlayerData(Player player) {
		if (hasPlayerData(player)) {
			((FileManager) Main.getInstance(Main.Instance.FileManager)).unloadConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
			playerDataStorage.remove(player.getUniqueId());
		}
	}
	
	public void savePlayerData(Player player) {
		if (hasPlayerData(player)) {
			Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
			
			try {
				config.getFileConfiguration().save(config.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<UUID, PlayerData> getPlayerData() {
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
		WorldManager worldManager = ((WorldManager) Main.getInstance(Main.Instance.WorldManager));
		
		if (hasPlayerData(player) && (player.getWorld().getName().equals(worldManager.getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(worldManager.getWorld(IslandLocation.World.Nether).getName()))) {
			IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.getInstance().isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						PlayerData playerData = getPlayerData(player);
						playerData.setIsland(island.getOwnerUUID());
						
						if (worldList == IslandLocation.World.Normal) {
							if (!island.isWeatherSynchronised()) {
			    				player.setPlayerTime(island.getTime(), false);
			    				player.setPlayerWeather(island.getWeather());
							}
						}
						
						if (Main.getInstance(Main.Instance.ScoreboardManager) != null) {
							if (!island.isRole(IslandRole.Member, player.getUniqueId()) && !island.isRole(IslandRole.Operator, player.getUniqueId()) && !island.isRole(IslandRole.Owner, player.getUniqueId())) {
								Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
								FileConfiguration configLoad = config.getFileConfiguration();
								
								for (Player all : Bukkit.getOnlinePlayers()) {
									PlayerData targetPlayerData = getPlayerData(all);
									
									if (targetPlayerData.getOwner().equals(island.getOwnerUUID())) {
										Scoreboard scoreboard = ((ScoreboardManager) Main.getInstance(Main.Instance.ScoreboardManager)).getScoreboard(all);
										scoreboard.cancel();
										
										if ((island.getRole(IslandRole.Member).size() + island.getRole(IslandRole.Operator).size() + 1) == 1) {
											scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Solo.Displayname")));
											scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Solo.Occupied.Displaylines"));
										} else {
											scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Team.Displayname")));
											scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Team.Occupied.Displaylines"));
											
											HashMap<String, String> displayVariables = new HashMap<String, String>();
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
			
			HashMap<UUID, Visit> visitIslands = ((VisitManager) Main.getInstance(Main.Instance.VisitManager)).getIslands();
			
			// TODO Check if player is banned from Island.
			
			for (UUID visitIslandList : visitIslands.keySet()) {
				Visit visit = visitIslands.get(visitIslandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.getInstance().isLocationAtLocationRadius(player.getLocation(), visit.getLocation(worldList), 85)) {
						if (visit.isOpen()) {
							PlayerData playerData = getPlayerData(player);
							playerData.setIsland(visitIslandList);
							
							islandManager.loadIsland(visitIslandList);
						} else {
							FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
							
							Config config = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
							FileConfiguration configLoad = config.getFileConfiguration();
							
				    		Player targetPlayer = Bukkit.getServer().getPlayer(visitIslandList);
				    		String targetPlayerName;
				    		
				    		if (targetPlayer == null) {
				    			targetPlayerName = new OfflinePlayer(visitIslandList).getName();
				    		} else {
				    			targetPlayerName = targetPlayer.getName();
				    		}
							
				    		LocationUtil.getInstance().teleportPlayerToSpawn(player);
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Visit.Closed.Island.Message").replace("%player", targetPlayerName)));
						}
						
						return;
					}
				}
			}
		}
	}
}
