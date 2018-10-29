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
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandSettings;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;
import me.goodandevil.skyblock.world.WorldManager;

public class Block implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		for (IslandLocation.World worldList : IslandLocation.World.values()) {
			if (player.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(worldList).getName())) {
				IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
				
				if (islandManager.hasPermission(player, "Destroy")) {
					for (UUID islandList : islandManager.getIslands().keySet()) {
						Island island = islandManager.getIslands().get(islandList);
						
						if (LocationUtil.getInstance().isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
							if (LocationUtil.getInstance().isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Main)) || LocationUtil.getInstance().isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Main).clone().add(0.0D, 1.0D, 0.0D)) || LocationUtil.getInstance().isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Main).clone().subtract(0.0D, 1.0D, 0.0D))) {
								if (((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Spawn.Protection")) {
									event.setCancelled(true);	
								}
							}
							
							return;
						}
					}
					
					event.setCancelled(true);
				} else {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
				
				break;
			}	
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		for (IslandLocation.World worldList : IslandLocation.World.values()) {
			if (player.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(worldList).getName())) {
				IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
				
				if (islandManager.hasPermission(player, "Place")) {
					for (UUID islandList : islandManager.getIslands().keySet()) {
						Island island = islandManager.getIslands().get(islandList);
						
						if (LocationUtil.getInstance().isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
							Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml"));
							FileConfiguration configLoad = config.getFileConfiguration();
							
							if (configLoad.getBoolean("Island.WorldBorder.Block")) {
								if (event.getBlock().getType() == Materials.PISTON.parseMaterial() || event.getBlock().getType() == Materials.STICKY_PISTON.parseMaterial()) {
									if (!LocationUtil.getInstance().isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 73)) {
										event.setCancelled(true);
									}
								} else if (event.getBlock().getType() == Material.DISPENSER) {
									if (!LocationUtil.getInstance().isLocationAtLocationRadius(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 83)) {
										event.setCancelled(true);
									}
								}
							}
							
							if (LocationUtil.getInstance().isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Main)) || LocationUtil.getInstance().isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Main).clone().add(0.0D, 1.0D, 0.0D)) || LocationUtil.getInstance().isLocationLocation(event.getBlock().getLocation(), island.getLocation(worldList, IslandLocation.Environment.Main).clone().subtract(0.0D, 1.0D, 0.0D))) {
								if (((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Spawn.Protection")) {
									event.setCancelled(true);	
								}
							}
							
							return;
						}
					}
					
					event.setCancelled(true);
				} else {
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml")).getFileConfiguration().getString("Island.Settings.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
				
				break;
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		if (!((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Piston.Connected.Extend")) {
			for (IslandLocation.World worldList : IslandLocation.World.values()) {
				if (event.getBlock().getLocation().getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(worldList).getName())) {
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
		if (!((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Piston.Connected.Retract")) {
			for (IslandLocation.World worldList : IslandLocation.World.values()) {
				if (event.getBlock().getLocation().getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(worldList).getName())) {
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
			if (!((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Weather.IceAndSnow")) {
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (event.getBlock().getLocation().getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(worldList).getName())) {
						event.setCancelled(true);
					}
				}	
			}	
		}
	}
	
	@EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
		org.bukkit.block.Block block = event.getBlock();
		
		if (block.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Normal).getName()) || block.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Nether).getName())) {
			IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.getInstance().isLocationAtLocationRadius(block.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						if (!island.getSetting(IslandSettings.Role.Owner, "FireSpread").getStatus()) {
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
		
		if (block.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Normal).getName()) || block.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Nether).getName())) {
			IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.getInstance().isLocationAtLocationRadius(block.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						if (!island.getSetting(IslandSettings.Role.Owner, "FireSpread").getStatus()) {
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
		
		if (block.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Normal).getName()) || block.getWorld().getName().equals(((WorldManager) Main.getInstance(Main.Instance.WorldManager)).getWorld(IslandLocation.World.Nether).getName())) {
			IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
			
			for (UUID islandList : islandManager.getIslands().keySet()) {
				Island island = islandManager.getIslands().get(islandList);
				
				for (IslandLocation.World worldList : IslandLocation.World.values()) {
					if (LocationUtil.getInstance().isLocationAtLocationRadius(block.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
						if (!island.getSetting(IslandSettings.Role.Owner, "LeafDecay").getStatus()) {
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
