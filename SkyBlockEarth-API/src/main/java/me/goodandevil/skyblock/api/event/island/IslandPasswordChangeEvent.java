package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.event.HandlerList;

public class IslandPasswordChangeEvent extends IslandEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancelled = false;
	private final String oldPassword, newPassword;
	
	public IslandPasswordChangeEvent(Island island, String oldPassword, String newPassword) {
		super(island);
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
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