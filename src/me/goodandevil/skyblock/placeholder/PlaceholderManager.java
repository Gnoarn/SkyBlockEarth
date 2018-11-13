package me.goodandevil.skyblock.placeholder;

import org.bukkit.plugin.PluginManager;

import me.goodandevil.skyblock.Main;

public class PlaceholderManager {

	private final Main plugin;
	
	public PlaceholderManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public void registerPlaceholders(PluginManager pluginManager) {
		if (pluginManager.getPlugin("PlaceholderAPI") != null) {
			new EZPlaceholder(plugin).register();
		}
	}
}
