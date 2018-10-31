package me.goodandevil.skyblock.island;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.ban.BanManager;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandCreateEvent;
import me.goodandevil.skyblock.events.IslandDeleteEvent;
import me.goodandevil.skyblock.events.IslandLoadEvent;
import me.goodandevil.skyblock.events.IslandOwnershipTransferEvent;
import me.goodandevil.skyblock.events.IslandUnloadEvent;
import me.goodandevil.skyblock.invite.Invite;
import me.goodandevil.skyblock.invite.InviteManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.scoreboard.Scoreboard;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;
import me.goodandevil.skyblock.structure.Structure;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.world.LocationUtil;
import me.goodandevil.skyblock.utils.world.WorldBorder;
import me.goodandevil.skyblock.visit.VisitManager;
import me.goodandevil.skyblock.world.WorldManager;

public class IslandManager {
	
	private final Main plugin;
	
	private double x = 0, offset = 400;
	
	private Map<IslandLocation.World, Location> islandWorldLocations = new EnumMap<>(IslandLocation.World.class);
	private Map<UUID, Island> islandStorage = new HashMap<>();
	
	public IslandManager(Main plugin) {
		this.plugin = plugin;
		
		WorldManager worldManager = plugin.getWorldManager();
		
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		for (IslandLocation.World worldList : IslandLocation.World.values()) {
			ConfigurationSection configSection = configLoad.getConfigurationSection("World." + worldList.name() + ".nextAvailableLocation");
			islandWorldLocations.put(worldList, new Location(worldManager.getWorld(worldList), configSection.getDouble("x"), 72, configSection.getDouble("z")));
		}
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			loadIsland(all.getUniqueId());
		}
	}

	public void onDisable() {
		saveNextAvailableLocation();
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			unloadIsland(all.getUniqueId());
		}
	}
	
	public void saveNextAvailableLocation() {
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml"));
		
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		for (IslandLocation.World worldList : IslandLocation.World.values()) {
			Location islandWorldLocation = islandWorldLocations.get(worldList);
			ConfigurationSection configSection = configLoad.createSection("World." + worldList.name() + ".nextAvailableLocation");
			configSection.set("x", islandWorldLocation.getX());
			configSection.set("z", islandWorldLocation.getZ());
		}
		
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setNextAvailableLocation(IslandLocation.World world, Location location) {
		islandWorldLocations.put(world, location);
	}
	
	public Location prepareNextAvailableLocation(IslandLocation.World world) {
		Location islandWorldLocation = islandWorldLocations.get(world);
		
		double x = islandWorldLocation.getX() + offset, z = islandWorldLocation.getZ();
		
		if (x > Math.abs(this.x)) {
			z += offset;
			islandWorldLocation.setX(this.x);
			x = islandWorldLocation.getX() + offset;
			islandWorldLocation.setZ(z);
		}
		
		islandWorldLocations.put(world, islandWorldLocation);
		
		return new Location(islandWorldLocation.getWorld(), x, 72, z);
	}
	
	public void createIsland(Player player, Structure structure) {
		Island island = new Island(player.getUniqueId(), prepareNextAvailableLocation(IslandLocation.World.Normal), prepareNextAvailableLocation(IslandLocation.World.Nether), new File(new File(plugin.getDataFolder().toString() + "/structures"), structure.getFileName()));
		islandStorage.put(player.getUniqueId(), island);
		
		Bukkit.getServer().getPluginManager().callEvent(new IslandCreateEvent(player, island));
		
		for (IslandLocation.World worldList : IslandLocation.World.values()) {
			setNextAvailableLocation(worldList, island.getLocation(worldList, IslandLocation.Environment.Island));
		}
		
		saveNextAvailableLocation();
		
		plugin.getPlayerDataManager().getPlayerData(player).setIsland(player.getUniqueId());
		
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		Scoreboard scoreboard = plugin.getScoreboardManager().getScoreboard(player);
		scoreboard.cancel();
		scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Solo.Displayname")));
		scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Solo.Empty.Displaylines"));
		scoreboard.run();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				player.teleport(island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Main));
			}
		}.runTask(plugin);
	}
	
	public void giveIslandOwnership(UUID uuid) {
		FileManager fileManager = plugin.getFileManager();
		PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
		
		Player targetPlayer = Bukkit.getServer().getPlayer(uuid);
		UUID islandOwnerUUID;
		
		if (targetPlayer == null) {
			OfflinePlayer offlinePlayer = new OfflinePlayer(uuid);
			islandOwnerUUID = offlinePlayer.getOwner();
		} else {
			islandOwnerUUID = playerDataManager.getPlayerData(targetPlayer).getOwner();
		}
		
		if (containsIsland(islandOwnerUUID)) {
			Island island = getIsland(islandOwnerUUID);
			island.setOwnerUUID(uuid);
			
			Config config = fileManager.getConfig(new File(plugin.getDataFolder(), "config.yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			if (configLoad.getBoolean("Island.Ownership.Password.Reset")) {
				island.setPassword(null);
			}
			
			File oldIslandDataFile = new File(new File(plugin.getDataFolder().toString() + "/island-data"), islandOwnerUUID.toString() + ".yml");
			File newIslandDataFile = new File(new File(plugin.getDataFolder().toString() + "/island-data"), uuid.toString() + ".yml");
			
			fileManager.unloadConfig(oldIslandDataFile);
			fileManager.unloadConfig(newIslandDataFile);
			oldIslandDataFile.renameTo(newIslandDataFile);
			
			plugin.getVisitManager().transfer(uuid, islandOwnerUUID);
			plugin.getBanManager().transfer(uuid, islandOwnerUUID);
			plugin.getInviteManager().tranfer(uuid, islandOwnerUUID);
			
			if (configLoad.getBoolean("Island.Ownership.Transfer.Operator")) {
				island.setRole(IslandRole.Operator, islandOwnerUUID);
			} else {
				island.setRole(IslandRole.Member, islandOwnerUUID);
			}
			
			if (island.isRole(IslandRole.Member, uuid)) {
				island.removeRole(IslandRole.Member, uuid);
			} else {
				island.removeRole(IslandRole.Operator, uuid);
			}
			
			removeIsland(islandOwnerUUID);
			islandStorage.put(uuid, island);
			
			Bukkit.getServer().getPluginManager().callEvent(new IslandOwnershipTransferEvent(island, islandOwnerUUID, uuid));
			
			ArrayList<UUID> islandMembers = new ArrayList<>();
			islandMembers.addAll(island.getRole(IslandRole.Member));
			islandMembers.addAll(island.getRole(IslandRole.Operator));
			islandMembers.add(uuid);
			
			for (UUID islandMemberList : islandMembers) {
				targetPlayer = Bukkit.getServer().getPlayer(islandMemberList);
				
				if (targetPlayer == null) {
					File configFile = new File(new File(plugin.getDataFolder().toString() + "/player-data"), islandMemberList.toString() + ".yml");
					configLoad = YamlConfiguration.loadConfiguration(configFile);
					configLoad.set("Island.Owner", uuid.toString());
					
					try {
						configLoad.save(configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					PlayerData playerData = playerDataManager.getPlayerData(targetPlayer);
					playerData.setOwner(uuid);
					playerData.save();
				}
			}
		}
	}

	public void deleteIsland(Island island) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (island.isRole(IslandRole.Member, all.getUniqueId()) || island.isRole(IslandRole.Operator, all.getUniqueId()) || island.isRole(IslandRole.Owner, all.getUniqueId())) {
				PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(all);
				playerData.setOwner(null);
				playerData.setMemberSince(null);
				playerData.setChat(false);
				playerData.save();
			}
			
			InviteManager inviteManager = plugin.getInviteManager();
			
			if (inviteManager.hasInvite(all.getUniqueId())) {
				Invite invite = inviteManager.getInvite(all.getUniqueId());
				
				if (invite.getOwnerUUID().equals(island.getOwnerUUID())) {
					inviteManager.removeInvite(all.getUniqueId());
				}
			}
		}
		
		plugin.getVisitManager().removeVisitors(island, VisitManager.Removal.Deleted);
		plugin.getFileManager().deleteConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), island.getOwnerUUID().toString() + ".yml"));
		
		Bukkit.getServer().getPluginManager().callEvent(new IslandDeleteEvent(island));
		
		islandStorage.remove(island.getOwnerUUID());
	}

	public void loadIsland(UUID uuid) {
		FileManager fileManager = plugin.getFileManager();
		UUID islandOwnerUUID = null;
		
		if (isIslandExist(uuid)) {
			islandOwnerUUID = uuid;
		} else {
			Config config = fileManager.getConfig(new File(new File(plugin.getDataFolder().toString() + "/player-data"), uuid.toString() + ".yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			if (configLoad.getString("Island.Owner") != null) {
				islandOwnerUUID = UUID.fromString(configLoad.getString("Island.Owner"));
			}
		}
		
		if (islandOwnerUUID != null && !hasIsland(islandOwnerUUID)) {
			File configFile = new File(plugin.getDataFolder().toString() + "/island-data");
			Config config = fileManager.getConfig(new File(configFile, islandOwnerUUID.toString() + ".yml"));
			
			Location islandNormalLocation = fileManager.getLocation(config, "Location.Normal.Island", true);
			Location islandNetherLocation = fileManager.getLocation(config, "Location.Nether.Island", true);
			
			Island island = new Island(islandOwnerUUID, new Location(islandNormalLocation.getWorld(), islandNormalLocation.getBlockX(), 72, islandNormalLocation.getBlockZ()), new Location(islandNetherLocation.getWorld(), islandNetherLocation.getBlockX(), 72, islandNetherLocation.getBlockZ()), null);
			islandStorage.put(islandOwnerUUID, island);
			
			Bukkit.getServer().getPluginManager().callEvent(new IslandLoadEvent(island));
		}
	}

	public void unloadIsland(UUID uuid) {
		ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
		FileManager fileManager = plugin.getFileManager();
		
		if (hasIsland(uuid)) {
			UUID islandOwnerUUID = uuid;
			
			if (!isIslandExist(uuid)) {
				Config config = fileManager.getConfig(new File(new File(plugin.getDataFolder().toString() + "/player-data"), uuid.toString() + ".yml"));
				FileConfiguration configLoad = config.getFileConfiguration();
				
				if (configLoad.getString("Island.Owner") != null) {
					islandOwnerUUID = UUID.fromString(configLoad.getString("Island.Owner"));
				}
			}
			
			Island island = getIsland(islandOwnerUUID);
			island.save();
			
			Config config = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			int islandMembers = island.getRole(IslandRole.Member).size() + island.getRole(IslandRole.Operator).size() + 1, islandVisitors = island.getVisitors().size();
			
			try {
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (!uuid.equals(islandOwnerUUID)) {
						if (all.getUniqueId().equals(uuid)) {
							continue;
						}
					}
					
					if (island.isRole(IslandRole.Member, all.getUniqueId()) || island.isRole(IslandRole.Operator, all.getUniqueId()) || island.isRole(IslandRole.Owner, all.getUniqueId())) {
						if (islandMembers == 1 && islandVisitors == 0) {
							Scoreboard scoreboard = scoreboardManager.getScoreboard(all);
							scoreboard.cancel();
							scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Solo.Displayname")));
							scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Solo.Empty.Displaylines"));
							scoreboard.run();
						} else if (islandVisitors == 0) {
							Scoreboard scoreboard = scoreboardManager.getScoreboard(all);
							scoreboard.cancel();
							scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Team.Displayname")));
							scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Team.Empty.Displaylines"));
							
							HashMap<String, String> displayVariables = new HashMap<>();
							displayVariables.put("%owner", configLoad.getString("Scoreboard.Island.Team.Word.Owner"));
							displayVariables.put("%operator", configLoad.getString("Scoreboard.Island.Team.Word.Operator"));
							displayVariables.put("%member", configLoad.getString("Scoreboard.Island.Team.Word.Member"));
							
							scoreboard.setDisplayVariables(displayVariables);
							scoreboard.run();
						}
						
						return;
					}
				}
			} catch (IllegalPluginAccessException e) {}
			
			boolean unloadIsland = fileManager.getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Visitor.Unload");
			
			if (unloadIsland) {
				VisitManager visitManager = plugin.getVisitManager();
				visitManager.removeVisitors(island, VisitManager.Removal.Unloaded);
				visitManager.unloadIsland(islandOwnerUUID);
				
				BanManager banManager = plugin.getBanManager();
				banManager.unloadIsland(islandOwnerUUID);
			} else {
				if (island.getVisitors().size() != 0) {
					return;
				}
			}
			
			fileManager.unloadConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), islandOwnerUUID + ".yml"));
			islandStorage.remove(islandOwnerUUID);
			
			Bukkit.getServer().getPluginManager().callEvent(new IslandUnloadEvent(island));
		}
	}
	
	public void visitIsland(Player player, Island island) {
		FileManager fileManager = plugin.getFileManager();
		
		Config languageConfig = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = languageConfig.getFileConfiguration();
		
		if (island.isRole(IslandRole.Member, player.getUniqueId()) || island.isRole(IslandRole.Operator, player.getUniqueId()) || island.isRole(IslandRole.Owner, player.getUniqueId())) {
			player.teleport(island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Visitor));
		} else {
			int islandVisitors = island.getVisitors().size(), islandMembers = island.getRole(IslandRole.Member).size() + island.getRole(IslandRole.Operator).size() + 1;
			
			if (islandVisitors == 0) {
				ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
				
				for (Player all : Bukkit.getOnlinePlayers()) {
					PlayerData targetPlayerData = plugin.getPlayerDataManager().getPlayerData(all);
					
					if (targetPlayerData.getOwner() != null && targetPlayerData.getOwner().equals(island.getOwnerUUID())) {
						Scoreboard scoreboard = scoreboardManager.getScoreboard(all);
						scoreboard.cancel();
						
						if (islandMembers == 1) {
							scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Solo.Displayname")));
							scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Solo.Occupied.Displaylines"));
						} else {
							scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Island.Team.Displayname")));
							scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Island.Team.Occupied.Displaylines"));
							
							HashMap<String, String> displayVariables = new HashMap<>();
							displayVariables.put("%owner", configLoad.getString("Scoreboard.Island.Team.Word.Owner"));
							displayVariables.put("%operator", configLoad.getString("Scoreboard.Island.Team.Word.Operator"));
							displayVariables.put("%member", configLoad.getString("Scoreboard.Island.Team.Word.Member"));
							
							scoreboard.setDisplayVariables(displayVariables);
						}
						
						scoreboard.run();
					}
				}
			}
			
			player.teleport(island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Visitor));
			
			List<String> islandWelcomeMessage = island.getMessage(IslandMessage.Welcome);
			
			if (plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Visitor.Welcome.Enable") && islandWelcomeMessage.size() != 0) {
				for (String islandWelcomeMessageList : islandWelcomeMessage) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', islandWelcomeMessageList));
				}
			}
		}
		
		player.closeInventory();
	}
	
	public void closeIsland(Island island) {
		island.setOpen(false);
		
		UUID islandOwnerUUID = island.getOwnerUUID();
		Player islandOwnerPlayer = Bukkit.getServer().getPlayer(islandOwnerUUID);
		String islandOwnerPlayerName;
		
		if (islandOwnerPlayer == null) {
			islandOwnerPlayerName = new OfflinePlayer(islandOwnerUUID).getName();
		} else {
			islandOwnerPlayerName = islandOwnerPlayer.getName();
		}
		
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		for (UUID visitorList : island.getVisitors()) {
    		Player targetPlayer = Bukkit.getServer().getPlayer(visitorList);
    		
			LocationUtil.teleportPlayerToSpawn(targetPlayer);
			
			targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Visit.Closed.Island.Message").replace("%player", islandOwnerPlayerName)));
		}
	}
	
	public Island getIsland(UUID islandOwnerUUID) {
		if (islandStorage.containsKey(islandOwnerUUID)) {
			return islandStorage.get(islandOwnerUUID);
		}
		
		return null;
	}
	
	public void removeIsland(UUID islandOwnerUUID) {
		islandStorage.remove(islandOwnerUUID);
	}

	public Map<UUID, Island> getIslands() {
		return islandStorage;
	}
	
	public boolean isIslandExist(UUID uuid) {
		return plugin.getFileManager().isFileExist(new File(new File(plugin.getDataFolder().toString() + "/island-data"), uuid.toString() + ".yml"));
	}
	
	public boolean hasIsland(UUID uuid) {
		UUID islandOwnerUUID = uuid;
		
		if (!isIslandExist(uuid)) {
			Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/player-data"), uuid.toString() + ".yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			if (configLoad.getString("Island.Owner") != null) {
				islandOwnerUUID = UUID.fromString(configLoad.getString("Island.Owner"));
			}
		}
		
		return islandStorage.containsKey(islandOwnerUUID);
	}
	
	public boolean hasIsland(Player player) {
		PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
		return (playerData == null) ? hasIsland(player.getUniqueId()) : islandStorage.containsKey(playerData.getOwner());
	}
	
	public boolean containsIsland(UUID uuid) {
		return islandStorage.containsKey(uuid);
	}
	
	public boolean hasPermission(Player player, String setting) {
		if (hasIsland(player)) {
			Island island = getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
			
			for (IslandLocation.World worldList : IslandLocation.World.values()) {
				if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
					if (island.isRole(IslandRole.Member, player.getUniqueId())) {
						if (!island.getSetting(IslandSettings.Role.Member, setting).getStatus()) {
							return false;
						}
					}
					
					return true;
				}
			}
		}
		
		for (UUID islandList : getIslands().keySet()) {
			Island island = getIslands().get(islandList);
			
			for (IslandLocation.World worldList : IslandLocation.World.values()) {
				if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
					if (!island.getSetting(IslandSettings.Role.Visitor, setting).getStatus()) {
						return false;
					}
					
					return true;
				}
			}
		}
		
		return true;
	}
	
	public void setSpawnProtection(Location location) {
		location.getBlock().setType(Materials.LEGACY_PISTON_MOVING_PIECE.getPostMaterial());
		location.clone().add(0.0D, 1.0D, 0.0D).getBlock().setType(Materials.LEGACY_PISTON_MOVING_PIECE.getPostMaterial());
	}
	
	public void removeSpawnProtection(Location location) {
		location.getBlock().setType(Material.AIR);
		location.clone().add(0.0D, 1.0D, 0.0D).getBlock().setType(Material.AIR);
	}
	
	public List<UUID> getMembersOnline(Island island) {
		List<UUID> membersOnline = new ArrayList<>();
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (island.isRole(IslandRole.Member, all.getUniqueId()) || island.isRole(IslandRole.Operator, all.getUniqueId()) || island.isRole(IslandRole.Owner, all.getUniqueId())) {
				membersOnline.add(all.getUniqueId());
			}
		}
		
		return membersOnline;
	}
	
	public List<UUID> getPlayersAtIsland(Island island) {
		List<UUID> playersAtIsland = new ArrayList<>();
		Map<UUID, PlayerData> playerData = plugin.getPlayerDataManager().getPlayerData();
		
		for (UUID playerDataList : playerData.keySet()) {
			UUID islandOwnerUUID = playerData.get(playerDataList).getIsland();
			
			if (islandOwnerUUID != null && island.getOwnerUUID().equals(islandOwnerUUID)) {
				playersAtIsland.add(playerDataList);
			}
		}
		
		return playersAtIsland;
	}
	
	public List<Player> getPlayersAtIsland(Island island, IslandLocation.World world) {
		List<Player> playersAtIsland = new ArrayList<>();
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (LocationUtil.isLocationAtLocationRadius(all.getLocation(), island.getLocation(world, IslandLocation.Environment.Island), 85)) {
				playersAtIsland.add(all);
			}
		}
		
		return playersAtIsland;
	}
	
	public void loadPlayer(Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName())) {
					Island island = null;
					
					if (hasIsland(player)) {
						island = getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
						
						if (!LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island), 85)) {
							island = null;
						}
					}
					
					if (island == null) {
						for (UUID islandList : getIslands().keySet()) {
							Island targetIsland = getIslands().get(islandList);
							
							if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), targetIsland.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island), 85)) {
								island = targetIsland;
								
								break;
							}
						}
					}
					
					if (island != null) {
						player.setPlayerTime(island.getTime(), false);
						player.setPlayerWeather(island.getWeather());
						
						if (plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.WorldBorder.Enable")) {
							WorldBorder.send(player, 172, island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island));
						}
					}
				}
			}
		}.runTaskAsynchronously(plugin);
	}
}
