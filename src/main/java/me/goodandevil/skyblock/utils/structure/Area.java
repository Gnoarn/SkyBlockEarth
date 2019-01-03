package me.goodandevil.skyblock.utils.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class Area {

	private Map<Integer, Location> positions;

	public Area() {
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
