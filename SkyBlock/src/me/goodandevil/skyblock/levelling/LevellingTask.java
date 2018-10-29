package me.goodandevil.skyblock.levelling;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.IslandManager;

public class LevellingTask extends BukkitRunnable {

	@Override
	public void run() {
		IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
		LevellingManager levellingManager = ((LevellingManager) Main.getInstance(Main.Instance.LevellingManager));
		
		for (UUID islandList : islandManager.getIslands().keySet()) {
			if (levellingManager.hasLevelling(islandList)) {
				Levelling levelling = levellingManager.getLevelling(islandList);
				levelling.setTime(levelling.getTime() - 1);
				
				if (levelling.getTime() == 0) {
					levellingManager.removeLevelling(islandList);
					levellingManager.unloadLevelling(islandList);
				}
			}
		}
	}
}
