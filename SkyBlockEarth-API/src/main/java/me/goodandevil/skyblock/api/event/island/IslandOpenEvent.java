package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.event.HandlerList;

public class IslandOpenEvent extends IslandEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancelled = false;
	private final boolean open;
	
	public IslandOpenEvent(Island island, boolean open) {
		super(island);
		this.open = open;
	}
	
	public boolean isOpen() {
		return open;
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