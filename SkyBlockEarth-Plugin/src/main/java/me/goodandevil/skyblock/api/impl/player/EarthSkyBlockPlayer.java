package me.goodandevil.skyblock.api.impl.player;

import java.util.UUID;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.api.invite.IslandInvitation;
import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.player.SkyBlockPlayer;
import me.goodandevil.skyblock.playerdata.PlayerData;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EarthSkyBlockPlayer implements SkyBlockPlayer {
	
	private final PlayerData handle;
	
	public EarthSkyBlockPlayer(PlayerData handle) {
		this.handle = handle;
	}

	@Override
	public OfflinePlayer getBukkitPlayer() {
		return Bukkit.getOfflinePlayer(getUUID());
	}

	@Override
	public UUID getUUID() {
		return handle.getOwner();
	}

	@Override
	public Island getIsland() {
		return null; // return handle.getIsland(); TODO
	}

	@Override
	public void setIsland(Island island) {
		// TODO
	}

	// TODO: Handle invitations on a per-player basis
	@Override
	public boolean hasPendingInvitation() {
		return SkyBlockEarth.getInstance().getInviteManager().hasInvite(getUUID());
	}

	@Override
	public IslandInvitation getPendingInvitation() {
		return null; // SkyBlockEarth.getInstance().getInviteManager().getInvite(getUUID()); TODO
	}

	@Override
	public void setPendingInvitation(IslandInvitation invitation) {
		// TODO
	}

	@Override
	public IslandInvitation sendIslandInvitationTo(SkyBlockPlayer player) {
		return null; // TODO
	}

	@Override
	public IslandInvitation sendIslandInvitationTo(OfflinePlayer player) {
		return null; // TODO
	}

	@Override
	public IslandInvitation sendIslandInvitationTo(UUID playerUUID) {
		return null; // TODO
	}

	@Override
	public boolean hasChatEnabled() {
		return handle.isChat();
	}

	@Override
	public void setChatEnabled(boolean enabled) {
		this.handle.setChat(enabled);
	}
	
}