package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandSettings;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class Entity implements Listener {
	
	private final Main plugin;
	
 	public Entity(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			
			if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
				if (event.getEntity() instanceof Player) {
					if (!plugin.getIslandManager().hasPermission(player, "PvP")) {
						event.setCancelled(true);
					}
				} else {
					if (!plugin.getIslandManager().hasPermission(player, "MobHurting")) {
						event.setCancelled(true);
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);	
						
						return;
					}
				}
			}
		}
		
		if (event.getEntity() instanceof ArmorStand) {
			if (event.getDamager() instanceof Player) {
				Player player = (Player) event.getDamager();
				
				if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
					if (!plugin.getIslandManager().hasPermission(player, "Destroy")) {
						event.setCancelled(true);
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					}
				}
			} else if (event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();
				
				if (projectile.getShooter() instanceof Player) {
					Player player = (Player) projectile.getShooter();
					
					if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
						if (!plugin.getIslandManager().hasPermission(player, "Destroy")) {
							event.setCancelled(true);
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
							player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerShearEntity(PlayerShearEntityEvent event) {
		Player player = event.getPlayer();
		
		if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
			if (!plugin.getIslandManager().hasPermission(player, "Shearing")) {
				event.setCancelled(true);
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
				player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
			}
		}
	}
	
	@EventHandler
	public void onEntityTaming(EntityTameEvent event) {
		if (Bukkit.getServer().getPlayer(event.getOwner().getUniqueId()) != null) {
			Player player = Bukkit.getServer().getPlayer(event.getOwner().getUniqueId());
			
			if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
				if (!plugin.getIslandManager().hasPermission(player, "MobTaming")) {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		org.bukkit.entity.Entity entity = event.getEntity();
		
		if (!(entity instanceof Player)) {
			if (entity.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || entity.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
				IslandManager islandManager = plugin.getIslandManager();
				
				for (UUID islandList : islandManager.getIslands().keySet()) {
					Island island = islandManager.getIslands().get(islandList);
					
					for (IslandLocation.World worldList : IslandLocation.World.values()) {
						if (LocationUtil.isLocationAtLocationRadius(entity.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
							if (!island.getSetting(IslandSettings.Role.Owner, "MobGriefing").getStatus()) {
								event.setCancelled(true);
							}
							
							return;
						}
					}
				}
				
				event.setCancelled(true);
			}	
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		org.bukkit.entity.Entity entity = event.getEntity();
		
		if (entity.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || entity.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
			IslandManager islandManager = plugin.getIslandManager();
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.isLocationAtLocationRadius(entity.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						if (!island.getSetting(IslandSettings.Role.Owner, "Explosions").getStatus()) {
							event.setCancelled(true);
						}
						
						return;
					}
				}
			}
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.CUSTOM) {
			LivingEntity livingEntity = event.getEntity();
			
			if (livingEntity.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || livingEntity.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
				IslandManager islandManager = plugin.getIslandManager();
				
				for (UUID islandList : islandManager.getIslands().keySet()) {
					Island island = islandManager.getIslands().get(islandList);
					
					for (IslandLocation.World worldList : IslandLocation.World.values()) {
						if (LocationUtil.isLocationAtLocationRadius(livingEntity.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
							if (!island.getSetting(IslandSettings.Role.Owner, "NaturalMobSpawning").getStatus()) {
								livingEntity.remove();
							}
							
							return;
						}
					}
				}
				
				livingEntity.remove();
			}
		}
	}
}
