package me.goodandevil.skyblock.biome;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;

public class BiomeTask extends BukkitRunnable {

	@Override
	public void run() {
		BiomeManager biomeManager = ((BiomeManager) Main.getInstance(Main.Instance.BiomeManager));
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (biomeManager.hasBiome(all)) {
				me.goodandevil.skyblock.biome.Biome biome = biomeManager.getBiome(all);
				biome.setTime(biome.getTime() - 1);
				
				if (biome.getTime() == 0) {
					biomeManager.removeBiome(all);
				}
			}
		}
	}
}
