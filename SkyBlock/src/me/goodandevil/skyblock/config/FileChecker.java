package me.goodandevil.skyblock.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.goodandevil.skyblock.Main;

public class FileChecker {
	
	private HashMap<File.Type, File> loadedFiles;
	
	public FileChecker(String configurationFileName) {
		loadedFiles = new HashMap<File.Type, File>();
		
		java.io.File configFile = new java.io.File(Main.getInstance().getDataFolder(), configurationFileName);
		loadedFiles.put(File.Type.Created, new File(configFile, YamlConfiguration.loadConfiguration(configFile)));
		loadedFiles.put(File.Type.Resource, new File(null, YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource(configurationFileName)))));
	}
	
	public void loadSections() {
		for (File.Type fileType : File.Type.values()) {
			File file = loadedFiles.get(fileType);
			FileConfiguration configLoad = file.getFileConfiguration();
			
			Set<String> configKeys = configLoad.getKeys(true);
			
			for (String configKeysList : configKeys) {
				file.addKey(configKeysList, configLoad.get(configKeysList));
			}
		}
	}
	
	public void compareFiles() {
		for (File.Type fileType : File.Type.values()) {
			File file = loadedFiles.get(fileType);
			FileConfiguration configLoad = file.getFileConfiguration();
			
			if (fileType == File.Type.Created) {
				File resourceFile = loadedFiles.get(File.Type.Resource);
				
				for (String configKeysList : file.getKeys().keySet()) {
					if (!resourceFile.getKeys().containsKey(configKeysList)) {
						configLoad.set(configKeysList, null);
					}
				}
			} else if (fileType == File.Type.Resource) {
				File createdFile = loadedFiles.get(File.Type.Created);
				FileConfiguration createdConfigLoad = createdFile.getFileConfiguration();
				
				for (String configKeysList : file.getKeys().keySet()) {
					if (createdConfigLoad.getString(configKeysList) == null) {
						createdConfigLoad.set(configKeysList, file.getKeys().get(configKeysList));
					}
				}
			}
		}
	}
	
	public void saveChanges() {
		File file = loadedFiles.get(File.Type.Created);
		
		try {
			file.getFileConfiguration().save(file.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class File {
		
		private java.io.File configFile;
		private FileConfiguration configLoad;
		
		private HashMap<String, Object> configKeys;
		
		public File(java.io.File configFile, FileConfiguration configLoad) {
			this.configFile = configFile;
			this.configLoad = configLoad;
			configKeys = new HashMap<String, Object>();
		}
		
		public java.io.File getFile() {
			return configFile;
		}
		
		public FileConfiguration getFileConfiguration() {
			return configLoad;
		}
		
		public HashMap<String, Object> getKeys() {
			return configKeys;
		}
		
		public void addKey(String key, Object object) {
			configKeys.put(key, object);
		}
		
		public enum Type {
			Created,
			Resource;
		}
	}
}
