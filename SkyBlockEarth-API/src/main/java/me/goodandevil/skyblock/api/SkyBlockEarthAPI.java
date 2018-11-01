package me.goodandevil.skyblock.api;

import java.util.UUID;

import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandEnvironment;
import me.goodandevil.skyblock.api.island.IslandWorld;
import me.goodandevil.skyblock.api.player.SkyBlockPlayer;

import org.bukkit.OfflinePlayer;

public interface SkyBlockEarthAPI {
	
	public SkyBlockPlayer getSkyBlockPlayer(OfflinePlayer player);
	
	public SkyBlockPlayer getSkyBlockPlayer(UUID playerUUID);
	
	public Island getIsland(UUID islandUUID);
	
	public Island getIslandAt(IslandWorld world, IslandEnvironment environment, int x, int y, int z);
	
}