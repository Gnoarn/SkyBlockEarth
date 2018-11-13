package me.goodandevil.skyblock.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.Location;
import me.goodandevil.skyblock.utils.version.Sounds;

public class Projectile implements Listener {

	private final Main plugin;
	
 	public Projectile(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			
			if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Nether).getName())) {
				if (event.getEntity() instanceof FishHook) {
					if (!plugin.getIslandManager().hasPermission(player, "Fishing")) {
						event.setCancelled(true);
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					}
					
					return;
				}
				
				if (!plugin.getIslandManager().hasPermission(player, "Projectile")) {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			}
		}
	}
}
