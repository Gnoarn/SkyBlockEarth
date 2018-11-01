package me.goodandevil.skyblock.api.event.island;

import java.util.List;

import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandMessage;

import org.bukkit.event.HandlerList;

public class IslandMessageChangeEvent extends IslandEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
    private boolean cancelled = false;
    
	private final IslandMessage message;
	private final List<String> lines;
	private final String author;
	
	public IslandMessageChangeEvent(Island island, IslandMessage message, List<String> lines, String author) {
		super(island);
		this.message = message;
		this.lines = lines;
		this.author = author;
	}
	
	public IslandMessage getMessage() {
		return message;
	}
	
	public List<String> getLines() {
		return lines;
	}
	
	public String getAuthor() {
		return author;
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