package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

public class IslandOwnershipTransferEvent extends IslandEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private final OfflinePlayer oldOwner, newOwner;
	
	public IslandOwnershipTransferEvent(Island island, OfflinePlayer oldOwner, OfflinePlayer newOwner) {
		super(island);
		this.oldOwner = oldOwner;
		this.newOwner = newOwner;
	}
	
	public OfflinePlayer getOldOwner() {
		return oldOwner;
	}
	
	public OfflinePlayer getNewOwner() {
		return newOwner;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}