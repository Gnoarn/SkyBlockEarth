package me.goodandevil.skyblock.ban;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.config.FileManager;

public class BanManager {

	private final SkyBlock skyblock;
	private Map<UUID, Ban> banStorage = new HashMap<>();

	public BanManager(SkyBlock skyblock) {
		this.skyblock = skyblock;

		loadIslands();
	}

	public void onDisable() {
		Map<UUID, Ban> banIslands = getIslands();

		for (UUID banIslandList : banIslands.keySet()) {
			Ban ban = banIslands.get(banIslandList);
			ban.save();
		}
	}

	public void loadIslands() {
		FileManager fileManager = skyblock.getFileManager();

		if (!fileManager.getConfig(new File(skyblock.getDataFolder(), "config.yml")).getFileConfiguration()
				.getBoolean("Island.Visitor.Unload")) {
			File configFile = new File(skyblock.getDataFolder().toString() + "/island-data");

			if (configFile.exists()) {
				for (File fileList : configFile.listFiles()) {
					UUID islandOwnerUUID = UUID.fromString(fileList.getName().replaceFirst("[.][^.]+$", ""));
					createIsland(islandOwnerUUID);
				}
			}
		}
	}

	public void transfer(UUID uuid1, UUID uuid2) {
		FileManager fileManager = skyblock.getFileManager();

		Ban ban = getIsland(uuid1);
		ban.save();

		File oldBanDataFile = new File(new File(skyblock.getDataFolder().toString() + "/ban-data"),
				uuid1.toString() + ".yml");
		File newBanDataFile = new File(new File(skyblock.getDataFolder().toString() + "/ban-data"),
				uuid2.toString() + ".yml");

		fileManager.unloadConfig(oldBanDataFile);
		fileManager.unloadConfig(newBanDataFile);

		oldBanDataFile.renameTo(newBanDataFile);

		removeIsland(uuid1);
		addIsland(uuid2, ban);
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

	public Map<UUID, Ban> getIslands() {
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
			skyblock.getFileManager().unloadConfig(new File(new File(skyblock.getDataFolder().toString() + "/ban-data"),
					islandOwnerUUID.toString() + ".yml"));
			banStorage.remove(islandOwnerUUID);
		}
	}

	public void deleteIsland(UUID islandOwnerUUID) {
		if (hasIsland(islandOwnerUUID)) {
			skyblock.getFileManager().deleteConfig(new File(new File(skyblock.getDataFolder().toString() + "/ban-data"),
					islandOwnerUUID.toString() + ".yml"));
			banStorage.remove(islandOwnerUUID);
		}
	}
}
