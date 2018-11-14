package me.goodandevil.skyblock.api.level;

import java.util.Map;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.Material;

public interface Level {
	
	public Island getIsland();
	
	public void setPoints(int points);
	
	public int getPoints();
	
	public int getLevel();
	
	public int getLastCalculatedLevel();
	
	public void setMaterialPointValue(Material material, int points);
	
	public int getMaterialPointValue(Material material);
	
	public Map<Material, Integer> getPointValues();
	
}