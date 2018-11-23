package me.goodandevil.skyblock;

import java.util.UUID;

import me.goodandevil.skyblock.api.SkyBlockEarthAPI;
import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandEnvironment;
import me.goodandevil.skyblock.api.island.IslandWorld;
import me.goodandevil.skyblock.api.player.SkyBlockPlayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

final class SkyBlockEarthAPIProvider implements SkyBlockEarthAPI {
	
	private static SkyBlockEarthAPIProvider instance;
	
	private final SkyBlockEarth plugin;
	
	private SkyBlockEarthAPIProvider(SkyBlockEarth plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public SkyBlockPlayer getSkyBlockPlayer(OfflinePlayer player) {
		if (player == null || player.isOnline()) {
			return null;
		}
		
		return plugin.getPlayerDataManager().getPlayerData(player.getPlayer()).apiWrapper;
	}
	
	@Override
	public SkyBlockPlayer getSkyBlockPlayer(UUID playerUUID) {
		Player player = Bukkit.getPlayer(playerUUID);
		if (player == null) {
			return null;
		}
		
		return plugin.getPlayerDataManager().getPlayerData(player).apiWrapper;
	}
	
	@Override
	public Island getIsland(UUID islandUUID) {
		return plugin.getIslandManager().getIsland(islandUUID).apiWrapper;
	}
	
	@Override
	public Island getIslandAt(IslandWorld world, IslandEnvironment environment, int x, int y, int z) {
		return null; // TODO
	}
	
	protected static SkyBlockEarthAPIProvider get(SkyBlockEarth plugin) {
		if (instance != null) {
			return instance;
		}
		
		return (instance = new SkyBlockEarthAPIProvider(plugin));
	}
	
}