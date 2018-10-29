package me.goodandevil.skyblock.biome;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.NMSManager;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class BiomeManager {
	
	private HashMap<UUID, me.goodandevil.skyblock.biome.Biome> playerBiomeStorage = new HashMap<UUID, me.goodandevil.skyblock.biome.Biome>();

	public BiomeManager() {
		new BiomeTask().runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			loadBiome(all);
		}
	}
	
	public void onDisable() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			saveBiome(all);
		}
	}
	
	public void setBiome(Player player, Location location, Biome biome) {
	    new BukkitRunnable() {
	    	public void run() {
	    		List<Chunk> chunks = new ArrayList<Chunk>();
	    		
	    		for (Location locationList : LocationUtil.getInstance().getLocations(new Location(location.getWorld(), location.getBlockX() - 85, 0, location.getBlockZ() - 85), new Location(location.getWorld(), location.getBlockX() + 85, 256, location.getBlockZ() + 85))) {
	            	try {
    	            	Block block = locationList.getBlock();
    	            	
    	            	if (block != null) {
    	            		block.setBiome(biome);
    	            		
    	            		boolean containsChunk = false;
    	            		
    	            		for (Chunk chunkList : chunks) {
    	            			if (chunkList.getX() == block.getChunk().getX() && chunkList.getZ() == block.getChunk().getZ()) {
    	            				containsChunk = true;
    	            				break;
    	            			}
    	            		}
    	            		
    	            		if (!containsChunk) {
    	            			chunks.add(block.getChunk());
    	            		}
    	            	}
	            	} catch (Exception e) {}
	    		}
	    	    
	    	    if (player != null) {
	    	    	IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
	    	    	Island island = islandManager.getIsland(((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager)).getPlayerData(player).getOwner());
	    	    	updateBiome(island, chunks);
	    	    }
	    	}
	    }.runTaskAsynchronously(Main.getInstance());
	}
	
	public void updateBiome(Island island, List<Chunk> chunks) {
		NMSManager nmsmanager = NMSManager.getInstance();
		
		Class<?> packetPlayOutMapChunkClass = NMSManager.getInstance().getNMSClass("PacketPlayOutMapChunk");
		Class<?> chunkClass = NMSManager.getInstance().getNMSClass("Chunk");
		
    	for (Player all : ((IslandManager) Main.getInstance(Main.Instance.IslandManager)).getPlayersAtIsland(island, IslandLocation.World.Normal)) {
    	    for (Chunk chunkList : chunks) {
    			try {
    				if (nmsmanager.getVersionNumber() < 10) {
    					nmsmanager.sendPacket(all, packetPlayOutMapChunkClass.getConstructor(chunkClass, boolean.class, int.class).newInstance(all.getLocation().getChunk().getClass().getMethod("getHandle").invoke(chunkList), true, 20));
    				} else {
    					nmsmanager.sendPacket(all, packetPlayOutMapChunkClass.getConstructor(chunkClass, int.class).newInstance(all.getLocation().getChunk().getClass().getMethod("getHandle").invoke(chunkList), 65535));
    				}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
    	    }
    	}
	}
	
	public void createBiome(Player player, int time) {
		Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		configLoad.set("Island.Biome.Cooldown", time);
		
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		me.goodandevil.skyblock.biome.Biome biome = new me.goodandevil.skyblock.biome.Biome(time);
		playerBiomeStorage.put(player.getUniqueId(), biome);
	}
	
	public void loadBiome(Player player) {
		Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (configLoad.getString("Island.Biome.Cooldown") != null) {
			me.goodandevil.skyblock.biome.Biome biome = new me.goodandevil.skyblock.biome.Biome(configLoad.getInt("Island.Biome.Cooldown"));
			playerBiomeStorage.put(player.getUniqueId(), biome);	
		}
	}
	
	public void removeBiome(Player player) {
		if (hasBiome(player)) {
			Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
			File configFile = config.getFile();
			FileConfiguration configLoad = config.getFileConfiguration();
			
			configLoad.set("Island.Biome.Cooldown", null);
			
			try {
				configLoad.save(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			playerBiomeStorage.remove(player.getUniqueId());
		}
	}
	
	public void saveBiome(Player player) {
		if (hasBiome(player)) {
			me.goodandevil.skyblock.biome.Biome biome = getBiome(player);
			
			Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), player.getUniqueId().toString() + ".yml"));
			File configFile = config.getFile();
			FileConfiguration configLoad = config.getFileConfiguration();
			
			configLoad.set("Island.Biome.Cooldown", biome.getTime());
			
			try {
				configLoad.save(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void unloadBiome(Player player) {
		if (hasBiome(player)) {
			playerBiomeStorage.remove(player.getUniqueId());
		}
	}

	public me.goodandevil.skyblock.biome.Biome getBiome(Player player) {
		if (hasBiome(player)) {
			return playerBiomeStorage.get(player.getUniqueId());
		}
		
		return null;
	}
	
	public boolean hasBiome(Player player) {
		return playerBiomeStorage.containsKey(player.getUniqueId());
	}
}
