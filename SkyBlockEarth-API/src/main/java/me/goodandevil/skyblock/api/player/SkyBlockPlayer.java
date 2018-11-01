package me.goodandevil.skyblock.api.player;

import java.util.UUID;

import me.goodandevil.skyblock.api.invite.IslandInvitation;
import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.OfflinePlayer;

public interface SkyBlockPlayer {
	
	public OfflinePlayer getBukkitPlayer();
	
	public UUID getUUID();
	
	public Island getIsland();
	
	public void setIsland(Island island);
	
	public boolean hasPendingInvitation();
	
	public IslandInvitation getPendingInvitation();
	
	public void setPendingInvitation(IslandInvitation invitation);
	
	public boolean hasChatEnabled();
	
	public void setChatEnabled(boolean enabled);
	
//	public Config getDataFile();
	
}