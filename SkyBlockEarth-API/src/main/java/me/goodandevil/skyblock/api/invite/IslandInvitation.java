package me.goodandevil.skyblock.api.invite;

import java.time.Instant;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.entity.Player;

public interface IslandInvitation {
	
	public Player getSender();
	
	public Player getReceiver();
	
	public Island getIsland();
	
	public Instant getSentTime();
	
}