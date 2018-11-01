package me.goodandevil.skyblock.api.event.island;

import java.util.Map;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.Material;
import org.bukkit.event.HandlerList;

public class IslandLevelChangeEvent extends IslandEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private final int oldLevel, newLevel, oldPoints, newPoints;
	private final Map<Material, Integer> materials;
	
	public IslandLevelChangeEvent(Island island, int oldLevel, int newLevel, int oldPoints, int newPoints, Map<Material, Integer> materials) {
		super(island);
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
		this.oldPoints = oldPoints;
		this.newPoints = newPoints;
		this.materials = materials;
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
	
	public Map<Material, Integer> getMaterials() {
		return materials;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}