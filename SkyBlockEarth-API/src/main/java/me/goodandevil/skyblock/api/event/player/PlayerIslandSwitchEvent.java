package me.goodandevil.skyblock.api.event.player;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerIslandSwitchEvent extends PlayerEvent {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private final Island oldIsland, newIsland;
	
	public PlayerIslandSwitchEvent(Player player, Island oldIsland, Island newIsland) {
		super(player);
		this.oldIsland = oldIsland;
		this.newIsland = newIsland;
	}
	
	public Island getOldIsland() {
		return oldIsland;
	}
	
	public Island getNewIsland() {
		return newIsland;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}