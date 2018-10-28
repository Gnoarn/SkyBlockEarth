package me.goodandevil.skyblock.ban;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class BanManager {

	private HashMap<UUID, Ban> banStorage = new HashMap<UUID, Ban>();
	
	public BanManager() {
		loadIslands();
	}
	
	public void onDisable() {
		HashMap<UUID, Ban> banIslands = getIslands();
		
		for (UUID banIslandList : banIslands.keySet()) {
			Ban ban = banIslands.get(banIslandList);
			ban.save();
		}
	}
	
	public void loadIslands() {
		FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
		
		if (!fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Visitor.Unload")) {
			File configFile = new File(Main.getInstance().getDataFolder().toString() + "/island-data");
			
			if (configFile.exists()) {
				for (File fileList : configFile.listFiles()) {
					UUID islandOwnerUUID = UUID.fromString(fileList.getName().replaceFirst("[.][^.]+$", ""));
					createIsland(islandOwnerUUID);
				}	
			}
		}
	}
	
	public void transfer(UUID uuid, UUID islandOwnerUUID) {
		Ban ban = getIsland(islandOwnerUUID);
		ban.save();
		
		File oldBanDataFile = new File(new File(Main.getInstance().getDataFolder().toString() + "/ban-data"), islandOwnerUUID.toString() + ".yml");
		File newBanDataFile = new File(new File(Main.getInstance().getDataFolder().toString() + "/ban-data"), uuid.toString() + ".yml");
		
		((FileManager) Main.getInstance(Main.Instance.FileManager)).unloadConfig(oldBanDataFile);
		((FileManager) Main.getInstance(Main.Instance.FileManager)).unloadConfig(newBanDataFile);
		
		oldBanDataFile.renameTo(newBanDataFile);
		
		removeIsland(islandOwnerUUID);
		addIsland(uuid, ban);
	}
	
	public void removeVisitor(Island island) {
		FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
		Config config = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		for (UUID visitorList : island.getVisitors()) {
			Player targetPlayer = Bukkit.getServer().getPlayer(visitorList);
			
			LocationUtil.getInstance().teleportPlayerToSpawn(targetPlayer);
			
			targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Visit.Banned.Message")));
			targetPlayer.playSound(targetPlayer.getLocation(), Sounds.ENDERMAN_TELEPORT.bukkitSound(), 1.0F, 1.0F);
		}
	}
	
	public boolean hasIsland(UUID islandOwnerUUID) {
		return banStorage.containsKey(islandOwnerUUID);
	}
	
	public Ban getIsland(UUID islandOwnerUUID) {
		if (hasIsland(islandOwnerUUID)) {
			return banStorage.get(islandOwnerUUID);
		}
		
		return null;
	}
	
	public HashMap<UUID, Ban> getIslands() {
		return banStorage;
	}
	
	public void createIsland(UUID islandOwnerUUID) {
		banStorage.put(islandOwnerUUID, new Ban(islandOwnerUUID));
	}
	
	public void addIsland(UUID islandOwnerUUID, Ban ban) {
		banStorage.put(islandOwnerUUID, ban);
	}
	
	public void removeIsland(UUID islandOwnerUUID) {
		if (hasIsland(islandOwnerUUID)) {
			banStorage.remove(islandOwnerUUID);
		}
	}
	
	public void unloadIsland(UUID islandOwnerUUID) {
		if (hasIsland(islandOwnerUUID)) {
			((FileManager) Main.getInstance(Main.Instance.FileManager)).unloadConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/ban-data"), islandOwnerUUID.toString() + ".yml"));
			banStorage.remove(islandOwnerUUID);
		}
	}
	
	public void deleteIsland(UUID islandOwnerUUID) {
		if (hasIsland(islandOwnerUUID)) {
			((FileManager) Main.getInstance(Main.Instance.FileManager)).deleteConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/ban-data"), islandOwnerUUID.toString() + ".yml"));
			banStorage.remove(islandOwnerUUID);
		}
	}
}
