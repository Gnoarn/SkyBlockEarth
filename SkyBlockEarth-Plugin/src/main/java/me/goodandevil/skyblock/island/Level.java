package me.goodandevil.skyblock.island;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandLevelChangeEvent;

public class Level {
	
	private final SkyBlockEarth plugin;
	private final Island island;
	
	private int lastLevel = 0;
	private int lastPoints = 0;
	
	public Level(Island island, SkyBlockEarth plugin) {
		this.island = island;
		this.plugin = plugin;
	}
	
	public void setPoints(int points) {
		lastPoints = getPoints();
		lastLevel = getLevel();
		
		Bukkit.getServer().getPluginManager().callEvent(new IslandLevelChangeEvent(island, this));
		
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), island.getOwnerUUID().toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		configLoad.set("Levelling.Points", points);
		
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		island.getVisit().setLevel(getLevel());
	}
	
	public int getPoints() {
		return plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), island.getOwnerUUID().toString() + ".yml")).getFileConfiguration().getInt("Levelling.Points");
	}
	
	public int getLevel() {
		return getPoints() / plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getInt("Island.Levelling.Division");
	}
	
	public void setMaterials(HashMap<Material, Integer> materials) {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), island.getOwnerUUID().toString() + ".yml"));
		File configFile = config.getFile();
		FileConfiguration configLoad = config.getFileConfiguration();
		
		configLoad.set("Levelling.Materials", null);
		
		for (Material materialList : materials.keySet()) {
			configLoad.set("Levelling.Materials." + materialList.name() + ".Points", materials.get(materialList));
		}
		
		try {
			configLoad.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<Material, Integer> getMaterials() {
		Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), island.getOwnerUUID().toString() + ".yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		HashMap<Material, Integer> materials = new HashMap<>();
		
		if (configLoad.getString("Levelling.Materials") != null) {
			for (String materialList : configLoad.getConfigurationSection("Levelling.Materials").getKeys(false)) {
				materials.put(Material.valueOf(materialList), configLoad.getInt("Levelling.Materials." + materialList + ".Points"));
			}
		}
		
		return materials;
	}
	
	public int getLastPoints() {
		return lastPoints;
	}
	
	public int getLastLevel() {
		return lastLevel;
	}
}
