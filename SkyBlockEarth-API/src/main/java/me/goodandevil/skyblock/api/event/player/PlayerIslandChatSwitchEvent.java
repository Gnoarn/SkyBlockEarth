package me.goodandevil.skyblock.api.event.player;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerIslandChatSwitchEvent extends PlayerEvent {
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private Island island;
	private boolean chat;
	
	public PlayerIslandChatSwitchEvent(Player player, Island island, boolean chat) {
		super(player);
		this.island = island;
		this.chat = chat;
	}
	
	public Island getIsland() {
		return island;
	}
	
	public boolean isChat() {
		return chat;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}