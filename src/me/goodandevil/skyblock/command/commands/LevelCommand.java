package me.goodandevil.skyblock.command.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.levelling.LevellingManager;
import me.goodandevil.skyblock.menus.Levelling;
import me.goodandevil.skyblock.utils.NumberUtil;
import me.goodandevil.skyblock.utils.version.Sounds;

public class LevelCommand extends SubCommand {

	private final Main plugin;
	private String info;
	
	public LevelCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		IslandManager islandManager = plugin.getIslandManager();
		
		Config languageConfig = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		
		if (islandManager.hasIsland(player)) {
			me.goodandevil.skyblock.island.Island island = islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
			
			Config config = plugin.getFileManager().getConfig(new File(new File(plugin.getDataFolder().toString() + "/island-data"), island.getOwnerUUID().toString() + ".yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			player.closeInventory();
			
			if (configLoad.getString("Levelling.Points") == null) {
				LevellingManager levellingManager = plugin.getLevellingManager();
				
	    		if (levellingManager.hasLevelling(island.getOwnerUUID())) {
					me.goodandevil.skyblock.levelling.Levelling levelling = levellingManager.getLevelling(island.getOwnerUUID());
					long[] durationTime = NumberUtil.getDuration(levelling.getTime());
					
					if (levelling.getTime() >= 3600) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Message").replace("%time", durationTime[1] + " " + languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Minute") + " " + durationTime[2] + " " + languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Minute") + " " + durationTime[3] + " " + languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Second"))));
					} else if (levelling.getTime() >= 60) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Message").replace("%time", durationTime[2] + " " + languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Minute") + " " + durationTime[3] + " " + languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Second"))));							
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Message").replace("%time", levelling.getTime() + " " + languageConfig.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Second"))));
					}
					
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					
					return;
				}
	    		
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Level.Processing.Message")));
				player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
				
				levellingManager.createLevelling(island.getOwnerUUID());
				levellingManager.loadLevelling(island.getOwnerUUID());
				levellingManager.calculatePoints(player, island);
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Level.Loading.Message")));
				player.playSound(player.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1.0F, 1.0F);
				Levelling.getInstance().open(player);
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getFileConfiguration().getString("Command.Island.Level.Owner.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "level";
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public SubCommand setInfo(String info) {
		this.info = info;
		
		return this;
	}

	@Override
	public String[] getAliases() {
		return new String[] { "levelling" , "points" };
	}

	@Override
	public Type getType() {
		return CommandManager.Type.Default;
	}
}
