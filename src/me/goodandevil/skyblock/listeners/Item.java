package me.goodandevil.skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.IslandLocation;

@SuppressWarnings("deprecation")
public class Item implements Listener {

	private final Main plugin;
	
 	public Item(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		
		if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
			if (!plugin.getIslandManager().hasPermission(player, "ItemDrop")) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		
		if (player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Normal).getName()) || player.getWorld().getName().equals(plugin.getWorldManager().getWorld(IslandLocation.World.Nether).getName())) {
			if (!plugin.getIslandManager().hasPermission(player, "ItemPickup")) {
				event.setCancelled(true);
			}
		}
	}
}
