package me.goodandevil.skyblock.cooldown;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;

public class CooldownManager {

	private final SkyBlock skyblock;

	private Map<CooldownType, List<CooldownPlayer>> cooldownStorage = new HashMap<>();

	public CooldownManager(SkyBlock skyblock) {
		this.skyblock = skyblock;

		IslandManager islandManager = skyblock.getIslandManager();

		for (CooldownType cooldownTypeList : CooldownType.values()) {
			List<CooldownPlayer> cooldownPlayers = new ArrayList<>();

			for (Player all : Bukkit.getOnlinePlayers()) {
				CooldownPlayer cooldownPlayer = null;

				if (cooldownTypeList == CooldownType.Biome || cooldownTypeList == CooldownType.Creation) {
					cooldownPlayer = loadCooldownPlayer(cooldownTypeList, all);
				} else if (cooldownTypeList == CooldownType.Levelling || cooldownTypeList == CooldownType.Ownership) {
					Island island = islandManager.getIsland(all);

					if (island != null) {
						OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(island.getOwnerUUID());

						if (!hasPlayer(cooldownTypeList, offlinePlayer)) {
							cooldownPlayer = loadCooldownPlayer(cooldownTypeList, offlinePlayer);
						}
					}
				}

				if (cooldownPlayer != null) {
					cooldownPlayers.add(cooldownPlayer);
				}
			}

			cooldownStorage.put(cooldownTypeList, cooldownPlayers);
		}

		new CooldownTask(this).runTaskTimerAsynchronously(skyblock, 0L, 20L);
	}

	public void onDisable() {
		for (CooldownType cooldownTypeList : CooldownType.values()) {
			setCooldownPlayer(cooldownTypeList);
			saveCooldownPlayer(cooldownTypeList);
		}
	}

	public CooldownPlayer loadCooldownPlayer(CooldownType cooldownType, OfflinePlayer player) {
		if (cooldownType == CooldownType.Biome || cooldownType == CooldownType.Creation) {
			Config config = skyblock.getFileManager()
					.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/player-data"),
							player.getUniqueId().toString() + ".yml"));
			FileConfiguration configLoad = config.getFileConfiguration();

			if (configLoad.getString("Island." + cooldownType.name() + ".Cooldown") != null) {
				return new CooldownPlayer(player.getUniqueId(),
						new Cooldown(configLoad.getInt("Island." + cooldownType.name() + ".Cooldown")));
			}
		} else if (cooldownType == CooldownType.Levelling || cooldownType == CooldownType.Ownership) {
			Config config = skyblock.getFileManager()
					.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/island-data"),
							player.getUniqueId().toString() + ".yml"));
			FileConfiguration configLoad = config.getFileConfiguration();

			if (configLoad.getString(cooldownType.name() + ".Cooldown") != null) {
				return new CooldownPlayer(player.getUniqueId(),
						new Cooldown(configLoad.getInt(cooldownType.name() + ".Cooldown")));
			}
		}

		return null;
	}

	public void createPlayer(CooldownType cooldownType, OfflinePlayer player) {
		FileManager fileManager = skyblock.getFileManager();

		if (cooldownStorage.containsKey(cooldownType)) {
			int time = 0;

			if (cooldownType == CooldownType.Biome || cooldownType == CooldownType.Creation) {
				time = fileManager.getConfig(new File(skyblock.getDataFolder(), "config.yml")).getFileConfiguration()
						.getInt("Island." + cooldownType.name() + ".Cooldown.Time");

				Config config = fileManager
						.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/player-data"),
								player.getUniqueId().toString() + ".yml"));
				File configFile = config.getFile();
				FileConfiguration configLoad = config.getFileConfiguration();

				configLoad.set("Island." + cooldownType.name() + ".Cooldown", time);

				try {
					configLoad.save(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (cooldownType == CooldownType.Levelling || cooldownType == CooldownType.Ownership) {
				time = fileManager.getConfig(new File(skyblock.getDataFolder(), "config.yml")).getFileConfiguration()
						.getInt("Island." + cooldownType.name() + ".Cooldown.Time");

				Config config = skyblock.getFileManager()
						.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/island-data"),
								player.getUniqueId().toString() + ".yml"));
				File configFile = config.getFile();
				FileConfiguration configLoad = config.getFileConfiguration();

				configLoad.set(cooldownType.name() + ".Cooldown", time);

				try {
					configLoad.save(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			cooldownStorage.get(cooldownType).add(new CooldownPlayer(player.getUniqueId(), new Cooldown(time)));
		}
	}

	public void deletePlayer(CooldownType cooldownType, OfflinePlayer player) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				if (cooldownPlayerList.getUUID().equals(player.getUniqueId())) {
					if (cooldownType == CooldownType.Biome || cooldownType == CooldownType.Creation) {
						skyblock.getFileManager()
								.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/player-data"),
										player.getUniqueId().toString() + ".yml"))
								.getFileConfiguration().set("Island." + cooldownType.name() + ".Cooldown", null);
					} else if (cooldownType == CooldownType.Levelling || cooldownType == CooldownType.Ownership) {
						skyblock.getFileManager()
								.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/island-data"),
										player.getUniqueId().toString().toString() + ".yml"))
								.getFileConfiguration().set(cooldownType.name() + ".Cooldown", null);
					}

					cooldownStorage.get(cooldownType).remove(cooldownPlayerList);

					break;
				}
			}
		}
	}

	public boolean hasPlayer(CooldownType cooldownType, OfflinePlayer player) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				if (cooldownPlayerList.getUUID().equals(player.getUniqueId())) {
					return true;
				}
			}
		}

		return false;
	}

	public void transferPlayer(CooldownType cooldownType, OfflinePlayer player1, OfflinePlayer player2) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				if (cooldownPlayerList.getUUID().equals(player1.getUniqueId())) {
					cooldownPlayerList.setUUID(player2.getUniqueId());

					break;
				}
			}
		}
	}

	public void removeCooldownPlayer(CooldownType cooldownType, CooldownPlayer cooldownPlayer) {
		if (cooldownStorage.containsKey(cooldownType)) {
			cooldownStorage.get(cooldownType).remove(cooldownPlayer);
		}
	}

	public void removeCooldownPlayer(CooldownType cooldownType, OfflinePlayer player) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				if (cooldownPlayerList.getUUID().equals(player.getUniqueId())) {
					cooldownStorage.get(cooldownType).remove(cooldownPlayerList);

					break;
				}
			}
		}
	}

	public void setCooldownPlayer(CooldownType cooldownType) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				setCooldownPlayer(cooldownType, Bukkit.getServer().getOfflinePlayer(cooldownPlayerList.getUUID()));
			}
		}
	}

	public void setCooldownPlayer(CooldownType cooldownType, OfflinePlayer player) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				if (cooldownPlayerList.getUUID().equals(player.getUniqueId())) {
					if (cooldownType == CooldownType.Biome || cooldownType == CooldownType.Creation) {
						skyblock.getFileManager()
								.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/player-data"),
										player.getUniqueId().toString() + ".yml"))
								.getFileConfiguration().set("Island." + cooldownType + ".Cooldown",
										cooldownPlayerList.getCooldown().getTime());
					} else if (cooldownType == CooldownType.Levelling || cooldownType == CooldownType.Ownership) {
						skyblock.getFileManager()
								.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/island-data"),
										player.getUniqueId().toString() + ".yml"))
								.getFileConfiguration()
								.set(cooldownType.name() + ".Cooldown", cooldownPlayerList.getCooldown().getTime());
					}

					break;
				}
			}
		}
	}

	public void saveCooldownPlayer(CooldownType cooldownType) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				saveCooldownPlayer(cooldownType, Bukkit.getServer().getOfflinePlayer(cooldownPlayerList.getUUID()));
			}
		}
	}

	public void saveCooldownPlayer(CooldownType cooldownType, OfflinePlayer player) {
		Config config = null;

		if (cooldownType == CooldownType.Biome || cooldownType == CooldownType.Creation) {
			config = skyblock.getFileManager()
					.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/player-data"),
							player.getUniqueId().toString() + ".yml"));
		} else if (cooldownType == CooldownType.Levelling || cooldownType == CooldownType.Ownership) {
			config = skyblock.getFileManager()
					.getConfig(new File(new File(skyblock.getDataFolder().toString() + "/island-data"),
							player.getUniqueId().toString().toString() + ".yml"));
		}

		if (config != null) {
			try {
				config.getFileConfiguration().save(config.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addCooldownPlayer(CooldownType cooldownType, CooldownPlayer cooldownPlayer) {
		if (cooldownType != null && cooldownPlayer != null) {
			if (cooldownStorage.containsKey(cooldownType)) {
				cooldownStorage.get(cooldownType).add(cooldownPlayer);
			}
		}
	}

	public CooldownPlayer getCooldownPlayer(CooldownType cooldownType, OfflinePlayer player) {
		if (cooldownStorage.containsKey(cooldownType)) {
			for (CooldownPlayer cooldownPlayerList : cooldownStorage.get(cooldownType)) {
				if (cooldownPlayerList.getUUID().equals(player.getUniqueId())) {
					return cooldownPlayerList;
				}
			}
		}

		return null;
	}

	public List<CooldownPlayer> getCooldownPlayers(CooldownType cooldownType) {
		if (cooldownStorage.containsKey(cooldownType)) {
			return cooldownStorage.get(cooldownType);
		}

		return null;
	}

	public boolean hasCooldownType(CooldownType cooldownType) {
		return cooldownStorage.containsKey(cooldownType);
	}
}
