package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.invite.IslandInvitation;
import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IslandInviteEvent extends Event {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private final Player invited, inviter;
	private final Island island;
	private final IslandInvitation invite;
	
	public IslandInviteEvent(Player invited, Player inviter, Island island, IslandInvitation invite) {
		this.invited = invited;
		this.inviter = inviter;
		this.island = island;
		this.invite = invite;
	}
	
	public Player getInvited() {
		return invited;
	}
	
	public Player getInviter() {
		return inviter;
	}
	
	public Island getIsland() {
		return island;
	}
	
	public IslandInvitation getInvite() {
		return invite;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}