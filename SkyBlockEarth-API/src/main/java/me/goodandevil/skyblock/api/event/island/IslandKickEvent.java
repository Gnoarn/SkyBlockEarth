package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandRole;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IslandKickEvent extends Event {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
    private boolean cancelled = false;
    
	private final Player kicker;
	private final OfflinePlayer kicked;
	private final Island island;
	private final IslandRole role;
	
	public IslandKickEvent(Island island, IslandRole role, OfflinePlayer kicked, Player kicker) {
		this.island = island;
		this.role = role;
		this.kicked = kicked;
		this.kicker = kicker;
	}
	
	public OfflinePlayer getKicked() {
		return kicked;
	}
	
	public Player getKicker() {
		return kicker;
	}
	
	public IslandRole getRole() {
		return role;
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
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}