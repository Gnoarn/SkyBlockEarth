package me.goodandevil.skyblock.api.island;

import org.bukkit.Location;
import org.bukkit.World;

public interface IslandLocationContext {
	
	public IslandEnvironment getEnvironment();
	
	public IslandWorld getWorld();
	
	public Location getLocation();
	
	public World getBukkitWorld();
	
	public int getX();
	
	public int getY();
	
	public int getZ();
	
}