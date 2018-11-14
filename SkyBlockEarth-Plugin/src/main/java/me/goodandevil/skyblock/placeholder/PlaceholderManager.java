package me.goodandevil.skyblock.placeholder;

import org.bukkit.plugin.PluginManager;

import me.goodandevil.skyblock.SkyBlockEarth;

public class PlaceholderManager {

	private final SkyBlockEarth plugin;
	
	private boolean PlaceholderAPI = false;
	private boolean MVdWPlaceholderAPI = false;
	
	public PlaceholderManager(SkyBlockEarth plugin) {
		this.plugin = plugin;
		
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		
		if (pluginManager.getPlugin("PlaceholderAPI") != null) {
			PlaceholderAPI = true;
		}
		
		if (pluginManager.getPlugin("MVdWPlaceholderAPI") != null) {
			MVdWPlaceholderAPI = true;
		}
	}
	
	public void registerPlaceholders() {
		if (PlaceholderAPI) {
			new EZPlaceholder(plugin).register();
		}
		
		if (MVdWPlaceholderAPI) {
			new MVdWPlaceholder(plugin).register();
		}
	}
	
	public boolean isPlaceholderAPIEnabled() {
		return PlaceholderAPI;
	}
	
	public boolean isMVdWPlaceholderAPIEnabled() {
		return MVdWPlaceholderAPI;
	}
}
