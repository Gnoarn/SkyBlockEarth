package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.biome.BiomeManager;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.levelling.LevellingManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.scoreboard.Scoreboard;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;

public class Join implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
		islandManager.loadIsland(player.getUniqueId());
		islandManager.loadPlayer(player);
		
		PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
		playerDataManager.loadPlayerData(player);
		
		if (playerDataManager.hasPlayerData(player)) {
			try {
				Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
				Method getProfileMethod = entityPlayer.getClass().getMethod("getProfile", new Class<?>[0]);
				GameProfile gameProfile = (GameProfile) getProfileMethod.invoke(entityPlayer);
				Property property = gameProfile.getProperties().get("textures").iterator().next();
				
				PlayerData playerData = playerDataManager.getPlayerData(player);
				playerData.setTexture(property.getSignature(), property.getValue());
				playerData.save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			playerDataManager.createPlayerData(player);
			playerDataManager.loadPlayerData(player);
		}
		
		playerDataManager.storeIsland(player);
		
		((BiomeManager) Main.getInstance(Main.Instance.BiomeManager)).loadBiome(player);
		
		FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
		
		if (fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Scoreboard.Enable")) {
			Config languageConfig = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
			Scoreboard scoreboard = new Scoreboard(player);
			
			if (islandManager.hasIsland(player)) {
				Island island = islandManager.getIsland(playerDataManager.getPlayerData(player).getOwner());
				
				((LevellingManager) Main.getInstance(Main.Instance.LevellingManager)).loadLevelling(island.getOwnerUUID());
				
				if (island.getRole(IslandRole.Member).size() == 0 && island.getRole(IslandRole.Operator).size() == 0) {
					scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Island.Solo.Displayname")));
					
					if (island.getVisitors().size() == 0) {
						scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Solo.Empty.Displaylines"));
					} else {
						scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Solo.Occupied.Displaylines"));
					}
				} else {
					scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Displayname")));
					
					if (island.getVisitors().size() == 0) {
						scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Team.Empty.Displaylines"));
					} else {
						scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Island.Team.Occupied.Displaylines"));
					}
					
					HashMap<String, String> displayVariables = new HashMap<String, String>();
					displayVariables.put("%owner", languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Word.Owner"));
					displayVariables.put("%operator", languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Word.Operator"));
					displayVariables.put("%member", languageConfig.getFileConfiguration().getString("Scoreboard.Island.Team.Word.Member"));
					
					scoreboard.setDisplayVariables(displayVariables);
				}
			} else {
				scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Scoreboard.Tutorial.Displayname")));
				scoreboard.setDisplayList(languageConfig.getFileConfiguration().getStringList("Scoreboard.Tutorial.Displaylines"));
			}
			
			scoreboard.run();
			((ScoreboardManager) Main.getInstance(Main.Instance.ScoreboardManager)).storeScoreboard(player, scoreboard);
		}
	}
}
