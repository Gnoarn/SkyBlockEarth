package me.goodandevil.skyblock.api.event.player;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerIslandChatEvent extends PlayerEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancelled = false;
	private String message, format;
	private final Island island;
	
	public PlayerIslandChatEvent(Player player, Island island, String message, String format) {
		super(player);
		this.island = island;
		this.message = message;
		this.format = format;
	}
	
	public Island getIsland() {
		return island;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
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
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}