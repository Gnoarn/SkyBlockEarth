package me.goodandevil.skyblock.levelling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;

public class LevellingChunk {

	private final Main plugin;
	private Island island;
	
	private List<ChunkSnapshot> chunkSnapshots = new ArrayList<>();
	private boolean complete;
	
	public LevellingChunk(Main plugin, Island island) {
		this.plugin = plugin;
		this.island = island;
		
		complete = false;
	}
	
	public void prepare() {
		new BukkitRunnable() {
			@Override
			public void run() {
				prepareChunkSnapshots();
			}
		}.runTask(plugin);
	}
	
	public List<ChunkSnapshot> getChunkSnapshots() {
		return chunkSnapshots;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	private void prepareChunkSnapshots() {
		for (IslandLocation.World worldList : IslandLocation.World.values()) {
			if (worldList == IslandLocation.World.Normal || (worldList == IslandLocation.World.Nether && plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.World.Nether.Enable"))) {
				Location islandLocation = island.getLocation(worldList, IslandLocation.Environment.Island);
				
				Location minLocation = new Location(islandLocation.getWorld(), islandLocation.getBlockX() - 85, 0, islandLocation.getBlockZ() - 85);
				Location maxLocation = new Location(islandLocation.getWorld(), islandLocation.getBlockX() + 85, 256, islandLocation.getBlockZ() + 85);
				
			    int MinX = Math.min(maxLocation.getBlockX(), minLocation.getBlockX());
			    int MinZ = Math.min(maxLocation.getBlockZ(), minLocation.getBlockZ());
			    
			    int MaxX = Math.max(maxLocation.getBlockX(), minLocation.getBlockX());
			    int MaxZ = Math.max(maxLocation.getBlockZ(), minLocation.getBlockZ());
			    
			    for (int x = MinX - 16; x <= MaxX + 16; x+=16) {
			    	for (int z = MinZ - 16; z <= MaxZ + 16; z+=16) {
			    		org.bukkit.Chunk chunk = islandLocation.getWorld().getBlockAt(x, 0, z).getChunk();
			    		chunkSnapshots.add(chunk.getChunkSnapshot());
			    	}
			    }
			}
		}
		
		complete = true;
	}
}
