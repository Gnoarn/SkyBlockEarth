package me.goodandevil.skyblock.api.event.player;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerIslandLeaveEvent extends PlayerEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
    private boolean cancelled = false;
    private final Island island;
	
	public PlayerIslandLeaveEvent(Player player, Island island) {
		super(player);
		this.island = island;
	}
	
	public Island getIsland() {
		return island;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public HandlerList getHandlerList() {
		return HANDLERS;
	}
}
