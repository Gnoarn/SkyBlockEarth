package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandSettings;
import me.goodandevil.skyblock.utils.world.LocationUtil;
import me.goodandevil.skyblock.world.WorldManager;

public class Death implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		for (IslandLocation.World worldList : IslandLocation.World.values()) {
			if (player.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(worldList).getName())) {
				Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml"));
				FileConfiguration configLoad = config.getFileConfiguration();
				
				IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
				
				boolean keepInventory = false;
				
				for (UUID islandList : islandManager.getIslands().keySet()) {
					Island island = islandManager.getIslands().get(islandList);
					
					if (LocationUtil.getInstance().isLocationAtLocationRadius(player.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						if (island.getSetting(IslandSettings.Role.Owner, "KeepItemsOnDeath").getStatus()) {
							keepInventory = true;
						}
						
						break;
					}
				}
				
				if (keepInventory) {
					event.setKeepInventory(true);
					event.getDrops().clear();
					event.setKeepLevel(true);
					event.setDroppedExp(0);
				}
				
				if (configLoad.getBoolean("Island.Death.AutoRespawn")) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				        public void run() {
				        	player.spigot().respawn();
				        	player.setFallDistance(0.0F);
				        	player.setFireTicks(0);
				        }
				   	}, 1L);	
				}
				
				break;
			}
		}
	}
}
