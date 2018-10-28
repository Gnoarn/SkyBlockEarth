package me.goodandevil.skyblock.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.world.WorldManager;

public class Projectile implements Listener {

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			
			if (player.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Nether).getName())) {
				if (event.getEntity() instanceof FishHook) {
					if (!((IslandManager) Main.getInstance(Main.Instance.IslandManager)).hasPermission(player, "Fishing")) {
						event.setCancelled(true);
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					}
					
					return;
				}
				
				if (!((IslandManager) Main.getInstance(Main.Instance.IslandManager)).hasPermission(player, "Projectile")) {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			}
		}
	}
}
