package me.goodandevil.skyblock.events;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandMessage;

public class IslandMessageChangeEvent extends Event {
	
	private Island island;
	private IslandMessage message;
	private List<String> lines;
	private String author;
	private boolean cancel = false;
	
	public IslandMessageChangeEvent(Island island, IslandMessage message, List<String> lines, String author) {
		this.island = island;
		this.message = message;
		this.lines = lines;
		this.author = author;
	}
	
	public Island getIsland() {
		return island;
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
		return cancel;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
    private static final HandlerList handlers = new HandlerList();
    
	public HandlerList getHandlers() {
		return handlers;
	}
}
