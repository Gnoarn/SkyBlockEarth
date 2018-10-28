package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandChatEvent;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;

public class Chat implements Listener {

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
		IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
		
		PlayerData playerData = playerDataManager.getPlayerData(player);
		
		if (playerData.isChat()) {
			event.setCancelled(true);
			
			Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			Island island = ((IslandManager) Main.getInstance(Main.Instance.IslandManager)).getIsland(playerData.getOwner());
			String islandRole = "";
			
			if (island.isRole(IslandRole.Member, player.getUniqueId())) {
				islandRole = configLoad.getString("Island.Chat.Format.Role.Member");
			} else if (island.isRole(IslandRole.Operator, player.getUniqueId())) {
				islandRole = configLoad.getString("Island.Chat.Format.Role.Operator");
			} else if (island.isRole(IslandRole.Owner, player.getUniqueId())) {
				islandRole = configLoad.getString("Island.Chat.Format.Role.Owner");
			}
			
			IslandChatEvent islandChatEvent = new IslandChatEvent(player, island, event.getMessage(), configLoad.getString("Island.Chat.Format.Message"));
			Bukkit.getServer().getPluginManager().callEvent(islandChatEvent);
			
			if (!islandChatEvent.isCancelled()) {
				for (UUID islandMembersOnlineList : islandManager.getMembersOnline(island)) {
					Player targetPlayer = Bukkit.getServer().getPlayer(islandMembersOnlineList);
					targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', islandChatEvent.getFormat().replace("%role", islandRole).replace("%player", player.getName())).replace("%message", islandChatEvent.getMessage()));
				}
			}
		}
	}
}
