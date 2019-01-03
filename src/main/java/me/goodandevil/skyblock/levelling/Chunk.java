package me.goodandevil.skyblock.levelling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandEnvironment;
import me.goodandevil.skyblock.island.IslandWorld;

public class Chunk {

	private final SkyBlock skyblock;
	private Island island;

	private List<ChunkSnapshot> chunkSnapshots = new ArrayList<>();
	private boolean complete;

	public Chunk(SkyBlock skyblock, Island island) {
		this.skyblock = skyblock;
		this.island = island;

		complete = false;
	}

	public void prepare() {
		new BukkitRunnable() {
			@Override
			public void run() {
				prepareChunkSnapshots();
			}
		}.runTask(skyblock);
	}

	public List<ChunkSnapshot> getChunkSnapshots() {
		return chunkSnapshots;
	}

	public boolean isComplete() {
		return complete;
	}

	private void prepareChunkSnapshots() {
		for (IslandWorld worldList : IslandWorld.values()) {
			if (worldList == IslandWorld.Normal
					|| (worldList == IslandWorld.Nether
							&& skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"))
									.getFileConfiguration().getBoolean("Island.World.Nether.Enable"))
					|| (worldList == IslandWorld.End
							&& skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"))
									.getFileConfiguration().getBoolean("Island.World.End.Enable"))) {
				Location islandLocation = island.getLocation(worldList, IslandEnvironment.Island);

				Location minLocation = new Location(islandLocation.getWorld(),
						islandLocation.getBlockX() - island.getRadius(), 0,
						islandLocation.getBlockZ() - island.getRadius());
				Location maxLocation = new Location(islandLocation.getWorld(),
						islandLocation.getBlockX() + island.getRadius(), islandLocation.getWorld().getMaxHeight(),
						islandLocation.getBlockZ() + island.getRadius());

				int MinX = Math.min(maxLocation.getBlockX(), minLocation.getBlockX());
				int MinZ = Math.min(maxLocation.getBlockZ(), minLocation.getBlockZ());

				int MaxX = Math.max(maxLocation.getBlockX(), minLocation.getBlockX());
				int MaxZ = Math.max(maxLocation.getBlockZ(), minLocation.getBlockZ());

				for (int x = MinX - 16; x <= MaxX + 16; x += 16) {
					for (int z = MinZ - 16; z <= MaxZ + 16; z += 16) {
						org.bukkit.Chunk chunk = islandLocation.getWorld().getBlockAt(x, 0, z).getChunk();
						chunkSnapshots.add(chunk.getChunkSnapshot());
					}
				}
			}
		}

		complete = true;
	}
}
