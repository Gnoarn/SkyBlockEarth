package me.goodandevil.skyblock.world;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.generator.VoidWorld;
import me.goodandevil.skyblock.island.Location;

public class WorldManager {
	
	private final SkyBlockEarth plugin;

	private org.bukkit.World normalWorld;
	private org.bukkit.World netherWorld;
	
	public WorldManager(SkyBlockEarth plugin) {
		this.plugin = plugin;
		
		loadWorlds();
	}
	
	public void loadWorlds() {
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		String netherWorldName = configLoad.getString("Island.World.Nether.Name");
		String normalWorldName = configLoad.getString("Island.World.Normal.Name");
	
		if (Bukkit.getServer().getWorld(normalWorldName) == null) {
			Bukkit.getServer().getConsoleSender().sendMessage("SkyBlock | Info: Generating Normal world VoidWorld '" + normalWorldName + "'.");
			WorldCreator worldCreator = new WorldCreator(normalWorldName);
			worldCreator.generateStructures(false);
			worldCreator.generator(new VoidWorld());
			Bukkit.getServer().createWorld(worldCreator);
		}
		
		if (Bukkit.getServer().getWorld(netherWorldName) == null) {
			Bukkit.getServer().getConsoleSender().sendMessage("SkyBlock | Info: Generating World world VoidWorld '" + netherWorldName + "'.");
			WorldCreator worldCreator = new WorldCreator(netherWorldName);
			worldCreator.environment(Environment.NETHER);
			worldCreator.generateStructures(false);
			worldCreator.generator(new VoidWorld());
			Bukkit.getServer().createWorld(worldCreator);
		}
		
		normalWorld = Bukkit.getServer().getWorld(normalWorldName);
		normalWorld.setDifficulty(Difficulty.NORMAL);
		
		Bukkit.getServer().getConsoleSender().sendMessage("SkyBlock | Info: Loaded Normal world: '" + normalWorldName + "'.");
		
		netherWorld = Bukkit.getServer().getWorld(netherWorldName);
		netherWorld.setDifficulty(Difficulty.NORMAL);
		
		Bukkit.getServer().getConsoleSender().sendMessage("SkyBlock | Info: Loaded Nether world: '" + netherWorldName + "'.");
	}
	
	public org.bukkit.World getWorld(Location.World world) {
		if (world == Location.World.Normal) {
			return normalWorld;
		} else if (world == Location.World.Nether) {
			return netherWorld;
		}
		
		return null;
	}
}
