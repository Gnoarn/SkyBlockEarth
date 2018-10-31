package me.goodandevil.skyblock.island;

import org.bukkit.Location;

public class IslandLocation {

	private World world;
	private Environment environment;
	private Location location;
	
	public IslandLocation(World world, Environment environment, Location location) {
		this.world = world;
		this.environment = environment;
		this.location = location;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public enum Environment {
		Island,
		Visitor,
		Main;
	}
	
	public enum World {
		Normal,
		Nether;
	}
}
