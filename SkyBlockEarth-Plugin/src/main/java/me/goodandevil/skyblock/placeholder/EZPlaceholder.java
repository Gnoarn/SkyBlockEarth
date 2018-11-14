package me.goodandevil.skyblock.placeholder;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Role;

public class EZPlaceholder extends PlaceholderExpansion {
	
	private final SkyBlockEarth plugin;
	
	public EZPlaceholder(SkyBlockEarth plugin) {
		this.plugin = plugin;
	}
	
	public String getIdentifier() {
    	return "skyblock";
    }
	
    public String getPlugin() {
    	return null;
    }
        
    public String getAuthor() {
    	return plugin.getDescription().getAuthors().get(0);
    }
    
    public String getVersion() {
    	return plugin.getDescription().getVersion();
    }
    
    public String onPlaceholderRequest(Player player, String identifier) {
    	if(player == null){
    		return "";
        }
    	
    	IslandManager islandManager = plugin.getIslandManager();
    	
    	if (islandManager.hasIsland(player)) {
    		Island island = islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
    		
    		if (identifier.equalsIgnoreCase("island_level")) {
    			return "" + island.getLevel().getLevel();
    		} else if (identifier.equalsIgnoreCase("island_points")) {
    			return "" + island.getLevel().getPoints();
    		} else if (identifier.equalsIgnoreCase("island_role")) {
    			for (Role roleList : Role.values()) {
    				if (island.isRole(roleList, player.getUniqueId())) {
    					return roleList.name();
    				}
    			}
    		} else if (identifier.equalsIgnoreCase("island_owner")) {
    			UUID islandOwnerUUID = island.getOwnerUUID();
    			Player targetPlayer = Bukkit.getServer().getPlayer(islandOwnerUUID);
    			
    			if (targetPlayer == null) {
    				return Bukkit.getServer().getOfflinePlayer(islandOwnerUUID).getName();
    			} else {
    				return targetPlayer.getName();
    			}
    		} else if (identifier.equalsIgnoreCase("island_biome")) {
    			return island.getBiomeName();
    		} else if (identifier.equalsIgnoreCase("island_time")) {
    			return "" + island.getTime();
    		} else if (identifier.equalsIgnoreCase("island_weather")) {
    			return "" + island.getWeatherName();
    		} else if (identifier.equalsIgnoreCase("island_bans")) {
    			return "" + island.getBan().getBans().size();
    		} else if (identifier.equalsIgnoreCase("island_members_total")) {
    			return "" + (island.getRole(Role.Member).size() + island.getRole(Role.Operator).size() + 1);
    		} else if (identifier.equalsIgnoreCase("island_members")) {
    			return "" + island.getRole(Role.Member).size();
    		} else if (identifier.equalsIgnoreCase("island_operators")) {
    			return "" + island.getRole(Role.Operator).size();
    		} else if (identifier.equalsIgnoreCase("island_visitors")) {
    			return "" + island.getVisitors().size();
    		}
    	}
        
        return null;
    }
}
