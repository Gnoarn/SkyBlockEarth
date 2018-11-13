package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.Location;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Settings;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class Block implements Listener {

	private final Main plugin;
	
 	public Block(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		for (Location.World worldList : Location.World.values()) {
			if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(worldList).getName())) {
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasPermission(player, "Destroy")) {
					for (UUID islandList : islandManager.getIslands().keySet()) {
						Island island = islandManager.getIslands().get(islandList);
						
						if (LocationUtil.isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Island), 85)) {
							if (LocationUtil.isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Main)) || LocationUtil.isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Main).clone().add(0.0D, 1.0D, 0.0D)) || LocationUtil.isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Main).clone().subtract(0.0D, 1.0D, 0.0D))) {
								if (plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Spawn.Protection")) {
									event.setCancelled(true);	
								}
							}
							
							return;
						}
					}
					
					event.setCancelled(true);
				} else {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
				
				break;
			}	
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		for (Location.World worldList : Location.World.values()) {
			if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(worldList).getName())) {
				IslandManager islandManager = plugin.getIslandManager();
				
				if (islandManager.hasPermission(player, "Place")) {
					for (UUID islandList : islandManager.getIslands().keySet()) {
						Island island = islandManager.getIslands().get(islandList);
						
						if (LocationUtil.isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Island), 85)) {
							Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml"));
							FileConfiguration configLoad = config.getFileConfiguration();
							
							if (configLoad.getBoolean("Island.WorldBorder.Block")) {
								if (event.getBlock().getType() == Materials.PISTON.parseMaterial() || event.getBlock().getType() == Materials.STICKY_PISTON.parseMaterial()) {
									if (!LocationUtil.isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Island), 73)) {
										event.setCancelled(true);
									}
								} else if (event.getBlock().getType() == Material.DISPENSER) {
									if (!LocationUtil.isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Island), 83)) {
										event.setCancelled(true);
									}
								}
							}
							
							if (LocationUtil.isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Main)) || LocationUtil.isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Main).clone().add(0.0D, 1.0D, 0.0D)) || LocationUtil.isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, Location.Environment.Main).clone().subtract(0.0D, 1.0D, 0.0D))) {
								if (plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Spawn.Protection")) {
									event.setCancelled(true);	
								}
							}
							
							return;
						}
					}
					
					event.setCancelled(true);
				} else {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
				
				break;
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		if (!plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Piston.Connected.Extend")) {
			for (Location.World worldList : Location.World.values()) {
				if (event.getBlock().getLocation().getWorld().getName().equals(plugin.getWorldManager().getWorld(worldList).getName())) {
					for (org.bukkit.block.Block blockList : event.getBlocks()) {
						if (blockList.getType() == Materials.PISTON.parseMaterial() || blockList.getType() == Materials.STICKY_PISTON.parseMaterial()) {
							event.setCancelled(true);
							
							break;
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonRetract(BlockPistonRetractEvent event) {
		if (!plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Piston.Connected.Retract")) {
			for (Location.World worldList : Location.World.values()) {
				if (event.getBlock().getLocation().getWorld().getName().equals(plugin.getWorldManager().getWorld(worldList).getName())) {
					for (org.bukkit.block.Block blockList : event.getBlocks()) {
						if (blockList.getType() == Materials.PISTON.parseMaterial() || blockList.getType() == Materials.STICKY_PISTON.parseMaterial()) {
							event.setCancelled(true);
							
							break;
						}
					}
				}
			}	
		}
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		if (event.getBlock().getType() == Material.ICE || event.getBlock().getType() == Material.SNOW) {
			if (!plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Weather.IceAndSnow")) {
				for (Location.World worldList : Location.World.values()) {
					if (event.getBlock().getLocation().getWorld().getName().equals(plugin.getWorldManager().getWorld(worldList).getName())) {
						event.setCancelled(true);
					}
				}	
			}	
		}
	}
	
	@EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
		org.bukkit.block.Block block = event.getBlock();
		
		if (block.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Normal).getName()) || block.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Nether).getName())) {
			IslandManager islandManager = plugin.getIslandManager();
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (Location.World worldList : Location.World.values()) {
					if (LocationUtil.isLocationAtLocationRadius(block.getLocation(), island.getLocation(worldList, Location.Environment.Island), 85)) {
						if (!island.getSetting(Settings.Role.Owner, "FireSpread").getStatus()) {
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
    public void onBlockSpread(BlockSpreadEvent event) {
		org.bukkit.block.Block block = event.getBlock();
		
		if (block.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Normal).getName()) || block.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Nether).getName())) {
			IslandManager islandManager = plugin.getIslandManager();
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (Location.World worldList : Location.World.values()) {
					if (LocationUtil.isLocationAtLocationRadius(block.getLocation(), island.getLocation(worldList, Location.Environment.Island), 85)) {
						if (!island.getSetting(Settings.Role.Owner, "FireSpread").getStatus()) {
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
	public void onLeavesDecay(LeavesDecayEvent event) {
		org.bukkit.block.Block block = event.getBlock();
		
		if (block.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Normal).getName()) || block.getWorld().getName().equals(plugin.getWorldManager().getWorld(Location.World.Nether).getName())) {
			IslandManager islandManager = plugin.getIslandManager();
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (Location.World worldList : Location.World.values()) {
					if (LocationUtil.isLocationAtLocationRadius(block.getLocation(), island.getLocation(worldList, Location.Environment.Island), 85)) {
						if (!island.getSetting(Settings.Role.Owner, "LeafDecay").getStatus()) {
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
