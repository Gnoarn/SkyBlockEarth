package me.goodandevil.skyblock.ban;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.api.event.island.IslandBanEvent;
import me.goodandevil.skyblock.api.event.island.IslandUnbanEvent;
import me.goodandevil.skyblock.config.FileManager.Config;

public class Ban {

	private UUID islandOwnerUUID;

	public Ban(UUID islandOwnerUUID) {
		this.islandOwnerUUID = islandOwnerUUID;
	}

	public UUID getOwnerUUID() {
		return islandOwnerUUID;
	}

	public void setOwnerUUID(UUID islandOwnerUUID) {
		this.islandOwnerUUID = islandOwnerUUID;
	}

	public boolean isBanned(UUID uuid) {
		return getBans().contains(uuid);
	}

	public Set<UUID> getBans() {
		SkyBlock skyblock = SkyBlock.getInstance();

		Set<UUID> islandBans = new HashSet<>();

		for (String islandBanList : skyblock.getFileManager()
				.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/ban-data"),
						islandOwnerUUID.toString() + ".yml"))
				.getFileConfiguration().getStringList("Bans")) {
			islandBans.add(UUID.fromString(islandBanList));
		}

		return islandBans;
	}

	public void addBan(UUID issuer, UUID banned) {
		SkyBlock skyblock = SkyBlock.getInstance();

		IslandBanEvent islandBanEvent = new IslandBanEvent(
				skyblock.getIslandManager().getIsland(Bukkit.getServer().getOfflinePlayer(islandOwnerUUID))
						.getAPIWrapper(),
				Bukkit.getServer().getOfflinePlayer(issuer), Bukkit.getServer().getOfflinePlayer(banned));
		Bukkit.getServer().getPluginManager().callEvent(islandBanEvent);

		if (!islandBanEvent.isCancelled()) {
			List<String> islandBans = new ArrayList<>();
			FileConfiguration configLoad = skyblock.getFileManager()
					.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/ban-data"),
							islandOwnerUUID.toString() + ".yml"))
					.getFileConfiguration();

			for (String islandBanList : configLoad.getStringList("Bans")) {
				islandBans.add(islandBanList);
			}

			islandBans.add(banned.toString());
			configLoad.set("Bans", islandBans);
		}
	}

	public void removeBan(UUID uuid) {
		SkyBlock skyblock = SkyBlock.getInstance();

		List<String> islandBans = new ArrayList<>();
		FileConfiguration configLoad = skyblock.getFileManager()
				.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/ban-data"),
						islandOwnerUUID.toString() + ".yml"))
				.getFileConfiguration();

		for (String islandBanList : configLoad.getStringList("Bans")) {
			if (!uuid.toString().equals(islandBanList)) {
				islandBans.add(islandBanList);
			}
		}

		configLoad.set("Bans", islandBans);

		Bukkit.getServer().getPluginManager()
				.callEvent(new IslandUnbanEvent(skyblock.getIslandManager()
						.getIsland(Bukkit.getServer().getOfflinePlayer(islandOwnerUUID)).getAPIWrapper(),
						Bukkit.getServer().getOfflinePlayer(uuid)));
	}

	public void save() {
		SkyBlock skyblock = SkyBlock.getInstance();

		Config config = skyblock.getFileManager().getConfig(new File(
				new File(skyblock.getDataFolder().toString() + "/ban-data"), islandOwnerUUID.toString() + ".yml"));

		try {
			config.getFileConfiguration().save(config.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
