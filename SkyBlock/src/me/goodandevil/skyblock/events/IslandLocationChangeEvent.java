package me.goodandevil.skyblock.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;

public class IslandLocationChangeEvent extends Event {
	
	private IslandLocation oldLocation, newLocation;
	private Island island;
	
	public IslandLocationChangeEvent(Island island, IslandLocation oldLocation, IslandLocation newLocation) {
		this.island = island;
		this.oldLocation = oldLocation;
		this.newLocation = newLocation;
	}
	
	public IslandLocation getOldLocation() {
		return oldLocation;
	}
	
	public IslandLocation getNewLocation() {
		return newLocation;
	}
	
	public Island getIsland() {
		return island;
	}
	
    private static final HandlerList handlers = new HandlerList();
    
	public HandlerList getHandlers() {
		return handlers;
	}
}
