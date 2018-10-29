package me.goodandevil.skyblock.utils.structure;

import java.util.HashMap;

import org.bukkit.Location;

public class StructureArea {

	private HashMap<Integer, Location> positions;
	
	public StructureArea() {
		positions = new HashMap<Integer, Location>();
	}
	
	public Location getPosition(int position) {
		if (positions.containsKey(position)) {
			return positions.get(position);
		}
		
		return null;
	}
	
	public void setPosition(int position, Location location) {
		positions.put(position, location);
	}
}
