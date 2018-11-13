package me.goodandevil.skyblock.placeholder;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;

public class EZPlaceholder extends PlaceholderExpansion {
	
	private final Main plugin;
	
	public EZPlaceholder(Main plugin) {
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
    			
    		}
    	}
        
        return null;
    }
}
