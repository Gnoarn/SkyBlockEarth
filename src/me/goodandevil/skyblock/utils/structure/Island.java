package me.goodandevil.skyblock.utils.structure;

import org.bukkit.Location;

public class Island {

	private Location centerLocation;
	private Location originLocation;
	
	public Island(Location centerLocation, Location originLocation) {
		this.centerLocation = centerLocation;
		this.originLocation = originLocation;
	}
	
	public Location getCenterLocation() {
		return centerLocation;
	}
	
	public Location getOriginLocation() {
		return originLocation;
	}
}
