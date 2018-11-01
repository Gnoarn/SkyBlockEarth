 package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandRole;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

public class IslandRoleChangeEvent extends IslandEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();

	private final OfflinePlayer player;
	private final IslandRole oldRole, newRole;
	
	public IslandRoleChangeEvent(Island island, OfflinePlayer player, IslandRole oldRole, IslandRole newRole) {
		super(island);
		this.player = player;
		this.oldRole = oldRole;
		this.newRole = newRole;
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	public IslandRole getOldRole() {
		return oldRole;
	}
	
	public IslandRole getNewRole() {
		return newRole;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}