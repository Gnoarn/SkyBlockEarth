package me.goodandevil.skyblock.utils.structure;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

public final class SelectionLocation {
	
    public static LinkedHashMap<Entity, StructureLocation> getEntities(Location originLocation, Location location1, Location location2) throws Exception {
        LinkedHashMap<Location, StructureLocation> locations = getLocations(originLocation, location1, location2);
        LinkedHashMap<Entity, StructureLocation> entities = new LinkedHashMap<>();
        
        for (Entity entityList : location2.getWorld().getEntities()) {
            for (Location locationList : locations.keySet()){
                if (locationList.getBlockX() == entityList.getLocation().getBlockX() && locationList.getBlockY() == entityList.getLocation().getBlockY() && locationList.getBlockZ() == entityList.getLocation().getBlockZ()) {
                    if (entityList instanceof Player || !(entityList instanceof LivingEntity || entityList instanceof Vehicle || entityList instanceof Hanging)){
                        continue;
                    }
                    
                    entities.put(entityList, locations.get(locationList));
                }
            }
        }
        
        return entities;
    }

    public static LinkedHashMap<Block, StructureLocation> getBlocks(Location originLocation, Location location1, Location location2) throws Exception {
        LinkedHashMap<Location, StructureLocation> locations = getLocations(originLocation, location1, location2);
        LinkedHashMap<Block, StructureLocation> blocks = new LinkedHashMap<>();
        
        for (Location locationList : locations.keySet()){
        	blocks.put(locationList.getBlock(), locations.get(locationList));
        }
        
        return blocks;
    }

    private static LinkedHashMap<Location, StructureLocation> getLocations(Location originLocation, Location location1, Location location2) throws Exception {
        LinkedHashMap<Location, StructureLocation> locations = new LinkedHashMap<>();
        
	    int MinX = Math.min(location2.getBlockX(), location1.getBlockX());
	    int MinY = Math.min(location2.getBlockY(), location1.getBlockY());
	    int MinZ = Math.min(location2.getBlockZ(), location1.getBlockZ());
	    
	    int MaxX = Math.max(location2.getBlockX(), location1.getBlockX());
	    int MaxY = Math.max(location2.getBlockY(), location1.getBlockY());
	    int MaxZ = Math.max(location2.getBlockZ(), location1.getBlockZ());
	    
	    for (int x = MinX; x <= MaxX; x++) {
	        for (int y = MinY; y <= MaxY; y++) {
	            for (int z = MinZ; z <= MaxZ; z++) {
                    Block block = location1.getWorld().getBlockAt(x, y, z);
                    
                    int offsetX = x - (int) location1.getX();
                    int offsetY = y - (int) location1.getY();
                    int offsetZ = z - (int) location1.getZ();
                    
                    boolean isOriginLocation = false;
                    
                    if (block.getX() == originLocation.getBlockX() && block.getY() == originLocation.getBlockY() && block.getZ() == originLocation.getBlockZ()) {
                    	isOriginLocation = true;
                    }
                    
                    locations.put(block.getLocation(), new StructureLocation(offsetX, offsetY, offsetZ, isOriginLocation));
	            }
	        }
	    }
        
        return locations;
    }
}
