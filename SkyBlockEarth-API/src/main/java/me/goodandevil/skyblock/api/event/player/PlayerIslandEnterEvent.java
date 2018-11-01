package me.goodandevil.skyblock.api.event.player;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerIslandEnterEvent extends PlayerEvent {
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private final Island island;
	
	public PlayerIslandEnterEvent(Player player, Island island) {
		super(player);
		this.island = island;
	}
	
	public Island getIsland() {
		return island;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}