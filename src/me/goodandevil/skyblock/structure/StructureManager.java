package me.goodandevil.skyblock.structure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.utils.NMSUtil;
import me.goodandevil.skyblock.utils.version.Materials;

public class StructureManager {

	private List<Structure> structureStorage = new ArrayList<>();
	
	public StructureManager(Main plugin) {
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "structures.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		int NMSVersion = NMSUtil.getVersionNumber();
		
		for (String structureList : configLoad.getConfigurationSection("").getKeys(false)) {
			String materialName = configLoad.getString(structureList + ".Item.Material");
			Material material;
			
			try {
				if (NMSVersion < 13) {
					material = Material.valueOf(materialName);
				} else {
					material = Materials.fromString(materialName).parseMaterial();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getServer().getLogger().log(Level.WARNING, "SkyBlock | Error: The material '" + materialName + "' is not a Material type. Please correct this in the 'structures.yml' file.");
				
				continue;
			}
			
			structureStorage.add(new Structure(material, configLoad.getInt(structureList + ".Item.Data"), configLoad.getString(structureList + ".File"), configLoad.getString(structureList + ".Name"), configLoad.getString(structureList + ".Displayname"), configLoad.getString(structureList + ".Permission"), configLoad.getStringList(structureList + ".Description")));
		}
	}
	
	public List<Structure> getStructures() {
		return structureStorage;
	}
}
