package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandLocationContext;

import org.bukkit.event.HandlerList;

public class IslandLocationChangeEvent extends IslandEvent {
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private final IslandLocationContext oldLocation, newLocation;
	
	public IslandLocationChangeEvent(Island island, IslandLocationContext oldLocation, IslandLocationContext newLocation) {
		super(island);
		this.oldLocation = oldLocation;
		this.newLocation = newLocation;
	}
	
	public IslandLocationContext getOldLocation() {
		return oldLocation;
	}
	
	public IslandLocationContext getNewLocation() {
		return newLocation;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}