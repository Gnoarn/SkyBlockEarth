package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandEnterEvent;
import me.goodandevil.skyblock.events.IslandExitEvent;
import me.goodandevil.skyblock.events.IslandSwitchEvent;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.Location;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;
import me.goodandevil.skyblock.visit.Visit;

public class Teleport implements Listener {

	private final Main plugin;
	
 	public Teleport(Main plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	Player player = event.getPlayer();
    	
    	IslandManager islandManager = plugin.getIslandManager();
    	FileManager fileManager = plugin.getFileManager();
    	
    	Config config = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
    	FileConfiguration configLoad = config.getFileConfiguration();
    	
    	islandManager.loadPlayer(player);
    	
    	if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Nether).getName())) {
    		if(event.getCause() == TeleportCause.ENDER_PEARL || event.getCause() == TeleportCause.NETHER_PORTAL) {
				if (!islandManager.hasPermission(player, "Portal")) {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					
					return;
				}
    		}
    	}
    	
    	PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
    	
		if (playerDataManager.hasPlayerData(player)) {
			PlayerData playerData = playerDataManager.getPlayerData(player);
			
			UUID islandOwnerUUID = playerData.getIsland();
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (Location.World worldList : Location.World.values()) {
					if (LocationUtil.isLocationAtLocationRadius(event.getTo(), island.getLocation(worldList, Location.Environment.Island), 85)) {
						if (!island.getOwnerUUID().equals(playerData.getOwner())) {
							if (!island.isOpen()) {
								event.setCancelled(true);
								
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Visit.Closed.Plugin.Message")));
								player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
								
								return;
							} else if (fileManager.getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Visitor.Banning") && island.getBan().isBanned(player.getUniqueId())) {
								event.setCancelled(true);
								
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Visit.Banned.Teleport.Message")));
								player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
								
								return;
							}
						}
						
						if (playerData.getIsland() != null && !playerData.getIsland().equals(island.getOwnerUUID())) {
							Bukkit.getServer().getPluginManager().callEvent(new IslandExitEvent(player, islandManager.getIsland(islandOwnerUUID)));
							Bukkit.getServer().getPluginManager().callEvent(new IslandSwitchEvent(player, islandManager.getIsland(islandOwnerUUID), island));
							
							playerData.setVisitTime(0);
						}
						
						if (worldList == Location.World.Normal) {
							if (!island.isWeatherSynchronised()) {
			    				player.setPlayerTime(island.getTime(), false);
			    				player.setPlayerWeather(island.getWeather());
							}
						}
						
						playerData.setIsland(island.getOwnerUUID());
						
						if (islandOwnerUUID != null && islandManager.containsIsland(islandOwnerUUID) && (playerData.getOwner() == null || !playerData.getOwner().equals(islandOwnerUUID))) {
							islandManager.unloadIsland(islandOwnerUUID);
						}
						
						Visit visit = island.getVisit();
						
						if (!visit.isVisitor(player.getUniqueId())) {
							Bukkit.getServer().getPluginManager().callEvent(new IslandEnterEvent(player, island));
							
							visit.addVisitor(player.getUniqueId());
							visit.save();
						}
						
						return;
					}
				}
			}
			
			player.resetPlayerTime();
			player.resetPlayerWeather();
			
			if (islandOwnerUUID != null) {
				Bukkit.getServer().getPluginManager().callEvent(new IslandExitEvent(player, islandManager.getIsland(islandOwnerUUID)));
			}
			
			playerData.setIsland(null);
			
			if (islandOwnerUUID != null && islandManager.containsIsland(islandOwnerUUID) && (playerData.getOwner() == null || !playerData.getOwner().equals(islandOwnerUUID))) {
				islandManager.unloadIsland(islandOwnerUUID);
			}
		}
    }
}
