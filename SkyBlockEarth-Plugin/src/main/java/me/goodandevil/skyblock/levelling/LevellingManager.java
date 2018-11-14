package me.goodandevil.skyblock.levelling;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.ChunkSnapshot;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.NMSUtil;
import me.goodandevil.skyblock.utils.version.Sounds;

public class LevellingManager {
	
	private final SkyBlockEarth plugin;
	
	private List<Material> materialStorage = new ArrayList<>();
	private Map<UUID, Levelling> islandLevellingStorage = new HashMap<>();
	
	public LevellingManager(SkyBlockEarth plugin) {
		this.plugin = plugin;
		
		new LevellingTask(this, plugin).runTaskTimerAsynchronously(plugin, 0L, 20L);
		
		registerMaterials();
		
		IslandManager islandManager = plugin.getIslandManager();
		PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (islandManager.hasIsland(all)) {
				Island island = islandManager.getIsland(playerDataManager.getPlayerData(all).getOwner());
				
				if (!hasLevelling(island.getOwnerUUID())) {
					loadLevelling(island.getOwnerUUID());
				}
			}
		}
	}
	
	public void onDisable() {
		IslandManager islandManager = plugin.getIslandManager();
		
		for (UUID islandList : islandManager.getIslands().keySet()) {
			Island island = islandManager.getIslands().get(islandList);
			
			if (hasLevelling(island.getOwnerUUID())) {
				saveLevelling(island.getOwnerUUID());
				unloadLevelling(island.getOwnerUUID());
			}
		}
	}
	
	public void calculatePoints(Player player, Island island) {
		Chunk chunk = new Chunk(plugin, island);
		chunk.prepare();
		
		int NMSVersion = NMSUtil.getVersionNumber();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (chunk.isComplete()) {
					cancel();
					
		    		HashMap<org.bukkit.Material, Integer> materials = new HashMap<>();
		    		
		    		Method getBlockTypeMethod = null;
		    		Method getBlockTypeIdMethod = null;
		    		Method getBlockTypeDataMethod = null;
		    		Method getMaterialMethod = null;
		    		
	    			for (ChunkSnapshot chunkSnapshotList : chunk.getChunkSnapshots()) {
	    				for (int x = 0; x < 16; x++) {
	    					for (int z = 0; z < 16; z++) {
	    						for (int y = 0; y < 256; y++) {
									try {
										org.bukkit.Material blockMaterial = org.bukkit.Material.AIR;
										int blockData = 0;
										
		    							if (NMSVersion > 12) {
		    								if (getBlockTypeMethod == null) {
		    									getBlockTypeMethod = chunkSnapshotList.getClass().getMethod("getBlockType", int.class, int.class, int.class);
		    								}
		    								
		    								blockMaterial = (org.bukkit.Material) getBlockTypeMethod.invoke(chunkSnapshotList, x, y, z);
		    							} else {
		    								if (getBlockTypeIdMethod == null) {
		    									getBlockTypeIdMethod = chunkSnapshotList.getClass().getMethod("getBlockTypeId", int.class, int.class, int.class);
		    								}
		    								
		    								if (getBlockTypeDataMethod == null) {
		    									getBlockTypeDataMethod = chunkSnapshotList.getClass().getMethod("getBlockData", int.class, int.class, int.class);
		    								}
		    								
		    								if (getMaterialMethod == null) {
		    									getMaterialMethod = blockMaterial.getClass().getMethod("getMaterial", int.class);
		    								}
		    								
		    								blockMaterial = (org.bukkit.Material) getMaterialMethod.invoke(blockMaterial, (int) getBlockTypeIdMethod.invoke(chunkSnapshotList, x, y, z));
											blockData = (int) getBlockTypeDataMethod.invoke(chunkSnapshotList, x, y, z);
		    							}
		    							
		    							if (blockMaterial != org.bukkit.Material.AIR) {
				    	            		for (Material materialList : materialStorage) {
				    	            			if (blockMaterial == materialList.getMaterial()) {
				    	            				if (NMSVersion < 13) {
					    	            				if (materialList.getData() != -1) {
					    	            					if (!(blockData == materialList.getData())) {
					    	            						continue;
					    	            					}
					    	            				}
				    	            				}
				    	            				
				    	            				if (materials.containsKey(materialList.getMaterial())) {
				    	            					materials.put(materialList.getMaterial(), materials.get(materialList.getMaterial()) + materialList.getPoints());
				    	            				} else {
				    	            					materials.put(materialList.getMaterial(), materialList.getPoints());
				    	            				}
				    	            			}
				    	            		}
		    							}
									} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
										e.printStackTrace();
									}
	    						}
	    					}
	    				}
	    			}
	    			
		    	    int totalPointsEarned = 0;
		    	    
		    	    for (org.bukkit.Material pointsEarnedList : materials.keySet()) {
		    	    	totalPointsEarned = totalPointsEarned + materials.get(pointsEarnedList);
		    	    }
		    	    
		    	    if (totalPointsEarned == 0) {
		    	    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Command.Island.Level.Materials.Message")));
		    	    	player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
		    	    } else {
		    	    	me.goodandevil.skyblock.island.Level level = island.getLevel();
			    	    level.setPoints(totalPointsEarned);
			    	    level.setMaterials(materials);
			    	    
			    	    if (player != null) {
			    	    	me.goodandevil.skyblock.menus.Levelling.getInstance().open(player);
			    	    }
		    	    }
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 1L);
	}
	
	public void registerMaterials() {
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "levelling.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		int NMSVersion = NMSUtil.getVersionNumber();
		
		for (String materialList : configLoad.getConfigurationSection("Materials").getKeys(false)) {
			try {
				org.bukkit.Material material;
				int data = 0;
				
				if (NMSVersion < 13) {
					material = org.bukkit.Material.valueOf(materialList);
					data = configLoad.getInt("Materials." + materialList + ".Data");
				} else {
					material = Materials.fromString(materialList).parseMaterial();
				}
				
				int points = configLoad.getInt("Materials." + materialList + ".Points");
				
				addMaterial(material, data, points);
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getServer().getLogger().log(Level.WARNING, "SkyBlock | Error: The material '" + materialList + "' is not a Material type. Please correct this in the 'levelling.yml' file.");
			}
		}
	}
	
	public void addMaterial(org.bukkit.Material material, int data, int points) {
		materialStorage.add(new Material(material, data, points));
	}
	
	public void removeMaterial(Material material) {
		materialStorage.remove(material);
	}
	
	public void createLevelling(UUID playerUUID) {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), playerUUID.toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		configLoad.set("Levelling.Cooldown", plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getInt("Island.Levelling.Cooldown"));
	
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeLevelling(UUID playerUUID) {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), playerUUID.toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		configLoad.set("Levelling.Cooldown", null);
	
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveLevelling(UUID playerUUID) {
		if (hasLevelling(playerUUID)) {
			Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), playerUUID.toString() + ".yml"));
			File configFile = config.getFile();
			FileConfiguration configLoad = config.getFileConfiguration();
			
			configLoad.set("Levelling.Cooldown", getLevelling(playerUUID).getTime());
		
			try {
				configLoad.save(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadLevelling(UUID playerUUID) {
		if (!hasLevelling(playerUUID)) {
			Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), playerUUID.toString() + ".yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			if (configLoad.getString("Levelling.Cooldown") != null) {
				islandLevellingStorage.put(playerUUID, new Levelling(configLoad.getInt("Levelling.Cooldown")));
			}
		}
	}
	
	public void unloadLevelling(UUID playerUUID) {
		if (hasLevelling(playerUUID)) {
			islandLevellingStorage.remove(playerUUID);
		}
	}

	public Levelling getLevelling(UUID playerUUID) {
		if (hasLevelling(playerUUID)) {
			return islandLevellingStorage.get(playerUUID);
		}
		
		return null;
	}
	
	public boolean hasLevelling(UUID playerUUID) {
		return islandLevellingStorage.containsKey(playerUUID);
	}
}
