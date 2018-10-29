package me.goodandevil.skyblock.playtime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;

public class PlaytimeTask extends BukkitRunnable {

	@Override
	public void run() {
		PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
		IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (playerDataManager.hasPlayerData(all) && islandManager.hasIsland(all)) {
				PlayerData playerData = playerDataManager.getPlayerData(all);
				playerData.setPlaytime(playerData.getPlaytime() + 1);
			}
		}
	}
}
