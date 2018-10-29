package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class Portal implements Listener {

	private final Main plugin;
	
 	public Portal(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityPortalEnter(EntityPortalEnterEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		Player player = (Player) event.getEntity();
		IslandManager islandManager = plugin.getIslandManager();
		
		if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName())) {
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island), 85)) {
					if (islandManager.hasPermission(player, "Portal")) {
						player.teleport(island.getLocation(IslandLocation.World.Nether, IslandLocation.Environment.Main));
						player.playSound(player.getLocation(), Sounds.ENDERMAN_TELEPORT.bukkitSound(), 1.0F, 1.0F);
					} else {
						player.teleport(island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Main));
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					}
					
					break;
				}
			}
		} else if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				if (LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(IslandLocation.World.Nether, IslandLocation.Environment.Island), 85)) {
					if (islandManager.hasPermission(player, "Portal")) {
						player.teleport(island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Main));
						player.playSound(player.getLocation(), Sounds.ENDERMAN_TELEPORT.bukkitSound(), 1.0F, 1.0F);
					} else {
						player.teleport(island.getLocation(IslandLocation.World.Nether, IslandLocation.Environment.Main));
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					}
					
					break;
				}
			}
		}
	}
}
