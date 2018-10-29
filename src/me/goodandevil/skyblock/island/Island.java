package me.goodandevil.skyblock.island;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.ban.Ban;
import me.goodandevil.skyblock.ban.BanManager;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandBiomeChangeEvent;
import me.goodandevil.skyblock.events.IslandLevelChangeEvent;
import me.goodandevil.skyblock.events.IslandLocationChangeEvent;
import me.goodandevil.skyblock.events.IslandMessageChangeEvent;
import me.goodandevil.skyblock.events.IslandOpenEvent;
import me.goodandevil.skyblock.events.IslandPasswordChangeEvent;
import me.goodandevil.skyblock.events.IslandRoleChangeEvent;
import me.goodandevil.skyblock.events.IslandWeatherChangeEvent;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.utils.StringUtil;
import me.goodandevil.skyblock.utils.structure.Structure;
import me.goodandevil.skyblock.utils.structure.StructureUtil;
import me.goodandevil.skyblock.utils.world.block.BlockDegreesType;
import me.goodandevil.skyblock.visit.Visit;
import me.goodandevil.skyblock.visit.VisitManager;

public class Island {

	private final Main plugin;
	
	private UUID ownerUUID;
	
	private List<IslandLocation> islandLocations = new ArrayList<IslandLocation>();
	private HashMap<IslandSettings.Role, HashMap<String, IslandSettings>> islandSettings = new HashMap<IslandSettings.Role, HashMap<String, IslandSettings>>();
	
	int r = 85;
	
	public Island(UUID ownerUUID, Location islandNormalLocation, Location islandNetherLocation, File structureFile) {
		this.plugin = Main.getInstance();
		
		FileManager fileManager = plugin.getFileManager();
		IslandManager islandManager = plugin.getIslandManager();
		
		this.ownerUUID = ownerUUID;
		
		islandLocations.add(new IslandLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island, islandNormalLocation));
		islandLocations.add(new IslandLocation(IslandLocation.World.Nether, IslandLocation.Environment.Island, islandNetherLocation));
		
		File configFile = new File(plugin.getDataFolder().toString() + "/island-data");
		Config config = fileManager.getConfig(new File(configFile, ownerUUID + ".yml"));
		
		if (fileManager.isFileExist(new File(configFile, ownerUUID + ".yml"))) {
			islandLocations.add(new IslandLocation(IslandLocation.World.Normal, IslandLocation.Environment.Main, fileManager.getLocation(config, "Location.Normal.Spawn.Main", true)));
			islandLocations.add(new IslandLocation(IslandLocation.World.Nether, IslandLocation.Environment.Main, fileManager.getLocation(config, "Location.Nether.Spawn.Main", true)));
			islandLocations.add(new IslandLocation(IslandLocation.World.Normal, IslandLocation.Environment.Visitor, fileManager.getLocation(config, "Location.Normal.Spawn.Visitor", true)));
			islandLocations.add(new IslandLocation(IslandLocation.World.Nether, IslandLocation.Environment.Visitor, fileManager.getLocation(config, "Location.Nether.Spawn.Visitor", true)));
			
			FileConfiguration configLoad = config.getFileConfiguration();
			
			for (IslandSettings.Role roleList : IslandSettings.Role.values()) {
				HashMap<String, IslandSettings> roleSettings = new HashMap<String, IslandSettings>();
				
				for (String settingList : configLoad.getConfigurationSection("Settings." + roleList.name()).getKeys(false)) {
					roleSettings.put(settingList, new IslandSettings(configLoad.getBoolean("Settings." + roleList.name() + "." + settingList)));
				}
				
				islandSettings.put(roleList, roleSettings);
			}
		} else {
			islandLocations.add(new IslandLocation(IslandLocation.World.Normal, IslandLocation.Environment.Main, islandNormalLocation));
			islandLocations.add(new IslandLocation(IslandLocation.World.Nether, IslandLocation.Environment.Main, islandNetherLocation));
			islandLocations.add(new IslandLocation(IslandLocation.World.Normal, IslandLocation.Environment.Visitor, islandNormalLocation));
			islandLocations.add(new IslandLocation(IslandLocation.World.Nether, IslandLocation.Environment.Visitor, islandNetherLocation));
			
			Config mainConfig = fileManager.getConfig(new File(plugin.getDataFolder(), "config.yml"));
			FileConfiguration mainConfigLoad = mainConfig.getFileConfiguration();
			
			fileManager.setLocation(config, "Location.Normal.Island", islandNormalLocation, true);
			fileManager.setLocation(config, "Location.Nether.Island", islandNetherLocation, true);
			fileManager.setLocation(config, "Location.Normal.Spawn.Main", islandNormalLocation, true);
			fileManager.setLocation(config, "Location.Nether.Spawn.Main", islandNetherLocation, true);
			fileManager.setLocation(config, "Location.Normal.Spawn.Visitor", islandNormalLocation, true);
			fileManager.setLocation(config, "Location.Nether.Spawn.Visitor", islandNetherLocation, true);
			
			configFile = config.getFile();
			FileConfiguration configLoad = config.getFileConfiguration();
			
			configLoad.set("Visitor.Open", mainConfigLoad.getBoolean("Island.Visitor.Open"));
			configLoad.set("Biome.Type", mainConfigLoad.getString("Island.Biome.Default.Type").toUpperCase());
			configLoad.set("Weather.Synchronised", mainConfigLoad.getBoolean("Island.Weather.Default.Synchronised"));
			configLoad.set("Weather.Time", mainConfigLoad.getInt("Island.Weather.Default.Time"));
			configLoad.set("Weather.Weather", mainConfigLoad.getString("Island.Weather.Default.Weather").toUpperCase());
			configLoad.set("Ownership.Original", ownerUUID.toString());
			
			Config settingsConfig = fileManager.getConfig(new File(plugin.getDataFolder(), "settings.yml"));
			
			for (IslandSettings.Role roleList : IslandSettings.Role.values()) {
				HashMap<String, IslandSettings> roleSettings = new HashMap<String, IslandSettings>();
				
				for (String settingList : settingsConfig.getFileConfiguration().getConfigurationSection(WordUtils.capitalize(roleList.name().toLowerCase())).getKeys(false)) {
					roleSettings.put(settingList, new IslandSettings(settingsConfig.getFileConfiguration().getBoolean(WordUtils.capitalize(roleList.name().toLowerCase()) + "." + settingList)));
				}
				
				islandSettings.put(roleList, roleSettings);
			}
			
			save();
			
			PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(Bukkit.getServer().getPlayer(ownerUUID));
			playerData.setPlaytime(0);
			playerData.setOwner(ownerUUID);
			playerData.setMemberSince(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
			playerData.save();
			
			new BukkitRunnable() {
				public void run() {
					islandNormalLocation.clone().subtract(0.0D, 1.0D, 0.0D).getBlock().setType(Material.STONE);
					islandManager.setSpawnProtection(islandNormalLocation);
					islandManager.setSpawnProtection(islandNetherLocation);
				}
			}.runTask(plugin);
			
			try {
				Structure structure = StructureUtil.loadStructure(structureFile);
				StructureUtil.pasteStructure(structure, islandNormalLocation, BlockDegreesType.ROTATE_360);
				StructureUtil.pasteStructure(structure, islandNetherLocation, BlockDegreesType.ROTATE_360);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//plugin.getBiomeManager().setBiome(null, islandNormalLocation, Biome.valueOf(mainConfigLoad.getString("Island.Biome.Default.Type").toUpperCase()));
		}
		
		VisitManager visitManager = plugin.getVisitManager();
		
		if (!visitManager.hasIsland(getOwnerUUID())) {
			visitManager.createIsland(getOwnerUUID(), new Location[] { getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island), getLocation(IslandLocation.World.Nether, IslandLocation.Environment.Island) }, getRole(IslandRole.Member).size() + getRole(IslandRole.Operator).size() + 1, getLevel(), getMessage(IslandMessage.Signature), isOpen());			
		}
		
		BanManager banManager = plugin.getBanManager();
		
		if (!banManager.hasIsland(getOwnerUUID())) {
			banManager.createIsland(getOwnerUUID());			
		}
	}
	
	public UUID getOwnerUUID() {
		return ownerUUID;
	}
	
	public void setOwnerUUID(UUID uuid) {
		getVisit().setOwnerUUID(uuid);
		ownerUUID = uuid;
	}
	
	public UUID getOriginalOwnerUUID() {
		return UUID.fromString(plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getString("Ownership.Original"));
	}
	
	public boolean hasPassword() {
		return plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getString("Ownership.Password") != null;
	}
	
	public String getPassword() {
		return plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getString("Ownership.Password");
	}
	
	public void setPassword(String password) {
		IslandPasswordChangeEvent islandPasswordChangeEvent = new IslandPasswordChangeEvent(this, getPassword(), password);
		Bukkit.getServer().getPluginManager().callEvent(islandPasswordChangeEvent);
		
		if (!islandPasswordChangeEvent.isCancelled()) {
			plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().set("Ownership.Password", password);			
		}
	}
	
	public Location getLocation(IslandLocation.World world, IslandLocation.Environment environment) {
		for (IslandLocation islandLocationList : islandLocations) {
			if (islandLocationList.getWorld() == world && islandLocationList.getEnvironment() == environment) {
				return islandLocationList.getLocation();
			}
		}
		
		return null;
	}
	
	public void setLocation(IslandLocation.World world, IslandLocation.Environment environment, Location location) {
		for (IslandLocation islandLocationList : islandLocations) {
			if (islandLocationList.getWorld() == world && islandLocationList.getEnvironment() == environment) {
				Bukkit.getServer().getPluginManager().callEvent(new IslandLocationChangeEvent(this, islandLocationList, new IslandLocation(world, environment, location)));
				
				FileManager fileManager = plugin.getFileManager();
				fileManager.setLocation(fileManager.getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), getOwnerUUID().toString() + ".yml")), "Location." + world.name() + ".Spawn." + environment.name(), location, true);
				
				islandLocationList.setLocation(location);
				
				break;
			}
		}
	}
	
	public Biome getBiome() {
		return Biome.valueOf(plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getString("Biome.Type"));
	}
	
	public String getBiomeName() {
		return StringUtil.capatilizeUppercaseLetters(WordUtils.capitalize(getBiome().name().toLowerCase()).replace("_", " "));
	}
	
	public void setBiome(Biome biome) {
		Bukkit.getServer().getPluginManager().callEvent(new IslandBiomeChangeEvent(this, getBiome(), biome));
		plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().set("Biome.Type", biome.name());
	}
	
	public boolean isWeatherSynchronised() {
		return plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getBoolean("Weather.Synchronised");
	}
	
	public void setWeatherSynchronised(boolean synchronised) {
		Bukkit.getServer().getPluginManager().callEvent(new IslandWeatherChangeEvent(this, getWeather(), getTime(), synchronised));
		plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().set("Weather.Synchronised", synchronised);
	}
	
	public WeatherType getWeather() {
		return WeatherType.valueOf(plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getString("Weather.Weather"));
	}
	
	public String getWeatherName() {
		return WordUtils.capitalize(getWeather().name().toLowerCase());
	}
	
	public void setWeather(WeatherType weatherType) {
		Bukkit.getServer().getPluginManager().callEvent(new IslandWeatherChangeEvent(this, weatherType, getTime(), isWeatherSynchronised()));
		plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().set("Weather.Weather", weatherType.name());
	}
	
	public int getTime() {
		return plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getInt("Weather.Time");
	}
	
	public void setTime(int time) {
		Bukkit.getServer().getPluginManager().callEvent(new IslandWeatherChangeEvent(this, getWeather(), time, isWeatherSynchronised()));
		plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().set("Weather.Time", time);
	}
	
	public List<UUID> getRole(IslandRole role) {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		List<UUID> islandRoles = new ArrayList<UUID>();
		
		if (configLoad.getString(role.name() + "s") != null) {
			for (String operatorList : configLoad.getStringList(role.name() + "s")) {
				islandRoles.add(UUID.fromString(operatorList));
			}
		}
		
		return islandRoles;
	}
	
	public void setRole(IslandRole role, UUID uuid) {
		if (!(role == IslandRole.Visitor || role == IslandRole.Owner)) {
			if (!isRole(role, uuid)) {
				if (role == IslandRole.Member) {
					if (isRole(IslandRole.Operator, uuid)) {
						Bukkit.getServer().getPluginManager().callEvent(new IslandRoleChangeEvent(uuid, this, IslandRole.Operator, role));
						removeRole(IslandRole.Operator, uuid);
					}
				} else if (role == IslandRole.Operator) {
					if (isRole(IslandRole.Member, uuid)) {
						Bukkit.getServer().getPluginManager().callEvent(new IslandRoleChangeEvent(uuid, this, IslandRole.Member, role));
						removeRole(IslandRole.Member, uuid);
					}
				}
				
				Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), getOwnerUUID().toString() + ".yml"));
				File configFile = config.getFile();
				FileConfiguration configLoad = config.getFileConfiguration();
				
				List<String> islandMembers;
				
				if (configLoad.getString(role.name() + "s") == null) {
					islandMembers = new ArrayList<String>();
				} else {
					islandMembers = configLoad.getStringList(role.name() + "s");
				}
				
				islandMembers.add(uuid.toString());
				configLoad.set(role.name() + "s", islandMembers);
				
				try {
					configLoad.save(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				getVisit().setMembers(getRole(IslandRole.Member).size() + getRole(IslandRole.Operator).size() + 1);
			}
		}
	}
	
	public void removeRole(IslandRole role, UUID uuid) {
		if (!(role == IslandRole.Visitor || role == IslandRole.Owner)) {
			if (isRole(role, uuid)) {
				Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
				File configFile = config.getFile();
				FileConfiguration configLoad = config.getFileConfiguration();
				List<String> islandMembers = configLoad.getStringList(role.name() + "s");
				
				islandMembers.remove(uuid.toString());
				configLoad.set(role.name() + "s", islandMembers);
				
				try {
					configLoad.save(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				getVisit().setMembers(getRole(IslandRole.Member).size() + getRole(IslandRole.Operator).size() + 1);
			}
		}
	}
	
	public boolean isRole(IslandRole role, UUID uuid) {
		if (role == IslandRole.Owner) {
			return getOwnerUUID().equals(uuid);
		}
		
		return getRole(role).contains(uuid);
	}
	
	public void setLevelPoints(int levelPoints) {
		Bukkit.getServer().getPluginManager().callEvent(new IslandLevelChangeEvent(this, getLevel(), levelPoints / plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getInt("Island.Levelling.Division"), getLevelPoints(), levelPoints, getLevelMaterials()));
		
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		configLoad.set("Levelling.Points", levelPoints);
		
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		getVisit().setLevel(getLevel());
	}
	
	public void setLevelMaterials(HashMap<Material, Integer> levelMaterials) {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		configLoad.set("Levelling.Materials", null);
		
		for (Material levelMaterialList : levelMaterials.keySet()) {
			configLoad.set("Levelling.Materials." + levelMaterialList.name() + ".Points", levelMaterials.get(levelMaterialList));
		}
		
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getLevelPoints() {
		return plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getInt("Levelling.Points");
	}
	
	public HashMap<Material, Integer> getLevelMaterials() {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		HashMap<Material, Integer> levelMaterials = new HashMap<Material, Integer>();
		
		if (configLoad.getString("Levelling.Materials") != null) {
			for (String levelMaterialList : configLoad.getConfigurationSection("Levelling.Materials").getKeys(false)) {
				levelMaterials.put(Material.valueOf(levelMaterialList), configLoad.getInt("Levelling.Materials." + levelMaterialList + ".Points"));
			}	
		}
		
		return levelMaterials;
	}
	
	public int getLevel() {
		return getLevelPoints() / plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getInt("Island.Levelling.Division");
	}
	
	public IslandSettings getSetting(IslandSettings.Role role, String setting) {
		if (islandSettings.containsKey(role)) {
			HashMap<String, IslandSettings> roleSettings = islandSettings.get(role);
			
			if (roleSettings.containsKey(setting)) {
				return roleSettings.get(setting);
			}
		}
		
		return null;
	}
	
	public HashMap<String, IslandSettings> getSettings(IslandSettings.Role role) {
		if (islandSettings.containsKey(role)) {
			return islandSettings.get(role);
		}
		
		return null;
	}
	
	public void setOpen(boolean open) {
		IslandOpenEvent islandOpenEvent = new IslandOpenEvent(this, open);
		Bukkit.getServer().getPluginManager().callEvent(islandOpenEvent);
		
		if (!islandOpenEvent.isCancelled()) {
			plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().set("Visitor.Open", open);	
			getVisit().setOpen(open);
		}
	}
	
	public boolean isOpen() {
		return plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml")).getFileConfiguration().getBoolean("Visitor.Open");
	}
	
	public List<UUID> getVisitors() {
		HashMap<UUID, PlayerData> playerDataStorage = plugin.getPlayerDataManager().getPlayerData();
		List<UUID> islandVisitors = new ArrayList<UUID>();
		
		for (UUID playerDataStorageList : playerDataStorage.keySet()) {
			PlayerData playerData = playerDataStorage.get(playerDataStorageList);
			UUID islandOwnerUUID = playerData.getIsland();
			
			if (islandOwnerUUID != null && islandOwnerUUID.equals(getOwnerUUID())) {
				if (playerData.getOwner() == null || !playerData.getOwner().equals(getOwnerUUID())) {
					if (Bukkit.getServer().getPlayer(playerDataStorageList) != null) {
						islandVisitors.add(playerDataStorageList);	
					}	
				}
			}
		}
		
		return islandVisitors;
	}
	
	public List<String> getMessage(IslandMessage message) {
		List<String> islandMessage = new ArrayList<String>();
		
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (configLoad.getString("Visitor." + message.name() + ".Message") != null) {
			islandMessage = configLoad.getStringList("Visitor." + message.name() + ".Message");
		}
		
		return islandMessage;
	}
	
	public String getMessageAuthor(IslandMessage message) {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (configLoad.getString("Visitor." + message.name() + ".Author") != null) {
			return configLoad.getString("Visitor." + message.name() + ".Author");
		}
		
		return "";
	}
	
	public void setMessage(IslandMessage message, String author, List<String> islandMessage) {
		IslandMessageChangeEvent islandMessageChangeEvent = new IslandMessageChangeEvent(this, message, islandMessage, author);
		Bukkit.getServer().getPluginManager().callEvent(islandMessageChangeEvent);
		
		if (!islandMessageChangeEvent.isCancelled()) {
			Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			configLoad.set("Visitor." + message.name() + ".Message", islandMessage);
			configLoad.set("Visitor." + message.name() + ".Author", author);
		}
	}
	
	public Visit getVisit() {
		return plugin.getVisitManager().getIsland(getOwnerUUID());
	}
	
	public Ban getBan() {
		return plugin.getBanManager().getIsland(getOwnerUUID());
	}
	
	public void save() {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), ownerUUID.toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		for (IslandSettings.Role roleList : IslandSettings.Role.values()) {
			if (islandSettings.containsKey(roleList)) {
				HashMap<String, IslandSettings> roleSettings = islandSettings.get(roleList);
				
				for (String roleSettingList : roleSettings.keySet()) {
					configLoad.set("Settings." + roleList.name() + "." + roleSettingList, roleSettings.get(roleSettingList).getStatus());
				}
			}
		}
		
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
