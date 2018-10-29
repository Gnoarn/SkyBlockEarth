package me.goodandevil.skyblock.invite;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;

public class InviteManager {

	private HashMap<UUID, Invite> inviteStorage = new HashMap<UUID, Invite>();
	
	public InviteManager() {
		new InviteTask().runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
	}
	
	public Invite createInvite(Player player, Player sender, UUID owner, int time) {
		Invite invite = new Invite(player, sender, owner, time);
		inviteStorage.put(player.getUniqueId(), invite);
		
		return invite;
	}
	
	public void removeInvite(UUID uuid) {
		if (hasInvite(uuid)) {
			inviteStorage.remove(uuid);
		}
	}
	
	public void tranfer(UUID uuid, UUID islandOwnerUUID) {
		HashMap<UUID, Invite> islandInvites = getInvites();
		
		for (UUID islandInviteList : islandInvites.keySet()) {
			Invite invite = islandInvites.get(islandInviteList);
			
			if (invite.getOwnerUUID().equals(islandOwnerUUID)) {
				invite.setOwnerUUID(uuid);
			}
		}
	}

	public HashMap<UUID, Invite> getInvites() {
		return inviteStorage;
	}
	
	public Invite getInvite(UUID uuid) {
		if (hasInvite(uuid)) {
			return inviteStorage.get(uuid);
		}
		
		return null;
	}
	
	public boolean hasInvite(UUID uuid) {
		return inviteStorage.containsKey(uuid);
	}
}
