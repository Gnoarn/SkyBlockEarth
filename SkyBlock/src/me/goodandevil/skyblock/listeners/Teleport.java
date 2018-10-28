package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.events.IslandEnterEvent;
import me.goodandevil.skyblock.events.IslandExitEvent;
import me.goodandevil.skyblock.events.IslandSwitchEvent;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;
import me.goodandevil.skyblock.visit.Visit;
import me.goodandevil.skyblock.world.WorldManager;

public class Teleport implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	Player player = event.getPlayer();
    	
    	IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
    	FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
    	
    	islandManager.loadPlayer(player);
    	
    	if (player.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Nether).getName())) {
    		if(event.getCause() == TeleportCause.ENDER_PEARL || event.getCause() == TeleportCause.NETHER_PORTAL) {
				if (!islandManager.hasPermission(player, "Portal")) {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					
					return;
				}
    		}
    	}
    	
    	PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
    	
    	// TODO Check if player is banned from Island.
    	
		if (playerDataManager.hasPlayerData(player)) {
			PlayerData playerData = playerDataManager.getPlayerData(player);
			
			UUID islandOwnerUUID = playerData.getIsland();
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.getInstance().isLocationAtLocationRadius(event.getTo(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						if (!island.getOwnerUUID().equals(playerData.getOwner()) && !island.isOpen()) {
							event.setCancelled(true);
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Visit.Closed.Plugin.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						
							return;
						}
						
						if (playerData.getIsland() != null && !playerData.getIsland().equals(island.getOwnerUUID())) {
							Bukkit.getServer().getPluginManager().callEvent(new IslandExitEvent(player, islandManager.getIsland(islandOwnerUUID)));
							Bukkit.getServer().getPluginManager().callEvent(new IslandSwitchEvent(player, islandManager.getIsland(islandOwnerUUID), island));
							
							playerData.setVisitTime(0);
						}
						
						if (worldList == IslandLocation.World.Normal) {
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
