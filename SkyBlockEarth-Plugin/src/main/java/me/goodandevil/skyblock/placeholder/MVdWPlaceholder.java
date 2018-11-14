package me.goodandevil.skyblock.placeholder;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Role;

public class MVdWPlaceholder {

	private final SkyBlockEarth plugin;
	
	public MVdWPlaceholder(SkyBlockEarth plugin) {
		this.plugin = plugin;
	}
	
	public void register() {
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_level", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getLevel().getLevel();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_points", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getLevel().getPoints();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_role", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		Island island = islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
					
	    			for (Role roleList : Role.values()) {
	    				if (island.isRole(roleList, player.getUniqueId())) {
	    					return roleList.name();
	    				}
	    			}
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_owner", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
	    			UUID islandOwnerUUID = islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getOwnerUUID();
	    			Player targetPlayer = Bukkit.getServer().getPlayer(islandOwnerUUID);
	    			
	    			if (targetPlayer == null) {
	    				return Bukkit.getServer().getOfflinePlayer(islandOwnerUUID).getName();
	    			} else {
	    				return targetPlayer.getName();
	    			}
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_biome", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getBiomeName();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_time", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getTime();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_weather", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getWeatherName();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_bans", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getBan().getBans().size();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_members_total", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		Island island = islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
		    		return "" + (island.getRole(Role.Member).size() + island.getRole(Role.Operator).size() + 1);
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_members", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getRole(Role.Member).size();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_operators", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getRole(Role.Operator).size();
				}
		    	
				return null;
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "skyblock_island_visitors", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				
				if (player == null) {
					return null;
				}
				
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasIsland(player)) {
		    		return "" + islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner()).getVisitors().size();
				}
		    	
				return null;
			}
		});
	}
}
