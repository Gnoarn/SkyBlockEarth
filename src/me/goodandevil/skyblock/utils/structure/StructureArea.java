package me.goodandevil.skyblock.utils.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class StructureArea {

	private Map<Integer, Location> positions;
	
	public StructureArea() {
		positions = new HashMap<>();
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
