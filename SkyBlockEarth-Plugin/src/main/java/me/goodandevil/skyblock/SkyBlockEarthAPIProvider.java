package me.goodandevil.skyblock;

import java.util.UUID;

import me.goodandevil.skyblock.api.SkyBlockEarthAPI;
import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandEnvironment;
import me.goodandevil.skyblock.api.island.IslandWorld;
import me.goodandevil.skyblock.api.player.SkyBlockPlayer;

import org.bukkit.OfflinePlayer;

final class SkyBlockEarthAPIProvider implements SkyBlockEarthAPI {
	
	private static SkyBlockEarthAPIProvider instance;
	
	private SkyBlockEarthAPIProvider() { }
	
	@Override
	public SkyBlockPlayer getSkyBlockPlayer(OfflinePlayer player) {
		return null;
	}
	
	@Override
	public SkyBlockPlayer getSkyBlockPlayer(UUID playerUUID) {
		return null;
	}
	
	@Override
	public Island getIsland(UUID islandUUID) {
		return null;
	}
	
	@Override
	public Island getIslandAt(IslandWorld world, IslandEnvironment environment, int x, int y, int z) {
		return null;
	}
	
	protected static SkyBlockEarthAPIProvider get() {
		return (instance == null) ? instance = new SkyBlockEarthAPIProvider() : instance;
	}
	
}