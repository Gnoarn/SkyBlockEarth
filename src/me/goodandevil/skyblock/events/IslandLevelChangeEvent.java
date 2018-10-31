package me.goodandevil.skyblock.events;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.goodandevil.skyblock.island.Island;

public class IslandLevelChangeEvent extends Event {
	
	private Island island;
	private int oldLevel, newLevel, oldPoints, newPoints;
	private HashMap<Material, Integer> materials;
	
	public IslandLevelChangeEvent(Island island, int oldLevel, int newLevel, int oldPoints, int newPoints, HashMap<Material, Integer> materials) {
		this.island = island;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
		this.oldPoints = oldPoints;
		this.newPoints = newPoints;
		this.materials = materials;
	}
	
	public Island getIsland() {
		return island;
	}
	
	public int getOldLevel() {
		return oldLevel;
	}
	
	public int getNewLevel() {
		return newLevel;
	}
	
	public int getOldPoints() {
		return oldPoints;
	}
	
	public int getNewPoints() {
		return newPoints;
	}
	
	public HashMap<Material, Integer> getMaterials() {
		return materials;
	}
	
    private static final HandlerList handlers = new HandlerList();
    
	public HandlerList getHandlers() {
		return handlers;
	}
}
