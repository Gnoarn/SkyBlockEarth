package me.goodandevil.skyblock.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Level;
import me.goodandevil.skyblock.island.Role;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.utils.NumberUtil;

public class Scoreboard {

	private Player player;
	private String displayName;
	private List<String> displayList;
	private Map<String, String> displayVariables;
	private BukkitTask scheduler;
	
	public Scoreboard(Player player) {
		this.player = player;
		displayList = new ArrayList<>();
		displayVariables = new HashMap<>();
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setDisplayList(List<String> displayList) {
		this.displayList = displayList;
	}
	
	public void setDisplayVariables(Map<String, String> displayVariables) {
		this.displayVariables = displayVariables;
	}
	
	public void run() {
		Main plugin = Main.getInstance();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				final org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
				
				new BukkitRunnable() {
					@Override
					@SuppressWarnings("deprecation")
					public void run() {
						String ranStr = UUID.randomUUID().toString().split("-")[0];
					    Objective obj;
					    
					    if (scoreboard.getObjective(player.getName()) != null) {
					    	obj = scoreboard.getObjective(player.getName());
					    } else {
					    	obj = scoreboard.registerNewObjective(player.getName(), "dummy");
					    }
					    
					    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					    obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', replaceDisplayName(displayName)));

					    for (int i = 0; i < ChatColor.values().length; i++) {
					    	if (i == displayList.size()) {
					    		break;
					    	}
					    	
					    	ChatColor chatColor = ChatColor.values()[i];
					    	Team team = scoreboard.registerNewTeam(ranStr + i);
					    	team.addEntry(chatColor.toString());
						    obj.getScore(chatColor.toString()).setScore(i);
					    }
					    
						scheduler = new BukkitRunnable() {
							int i1 = displayList.size();
							
							@Override
							public void run() {
								if (player.isOnline()) {
									try {
										for (String displayLine : displayList) {
									    	 i1--;
									    	 
									    	 displayLine = replaceDisplayLine(displayLine);
									    	 
									    	 if (displayLine.length() > 32) {
									    		 displayLine = "&cLine too long...";
									    	 }
									    	 
									    	 if (displayLine.length() >= 16) {
								    			 String prefixLine = displayLine.substring(0, Math.min(displayLine.length(), 16));
								    			 String suffixLine = displayLine.substring(16, Math.min(displayLine.length(), displayLine.length()));

								    			 if (prefixLine.substring(prefixLine.length() - 1).equals("&")) {
								    				 prefixLine = ChatColor.translateAlternateColorCodes('&', prefixLine.substring(0, prefixLine.length() - 1));
								    				 suffixLine = ChatColor.translateAlternateColorCodes('&', "&" + suffixLine);
								    			 } else {
									    			 String[] colourCodes = prefixLine.split("&");
									    			 String lastColourCodeText = colourCodes[colourCodes.length - 1];
									    			 String lastColourCodeValue = lastColourCodeText.substring(0, Math.min(lastColourCodeText.length(), 1));
									    			 prefixLine = ChatColor.translateAlternateColorCodes('&', prefixLine);
									    			 suffixLine = ChatColor.translateAlternateColorCodes('&', "&" + lastColourCodeValue + suffixLine);
								    			 }
								    			 
								    			 scoreboard.getTeam(ranStr + i1).setPrefix(prefixLine);
								    			 scoreboard.getTeam(ranStr + i1).setSuffix(suffixLine);
									    	 } else {
									    		 scoreboard.getTeam(ranStr + i1).setPrefix(ChatColor.translateAlternateColorCodes('&', displayLine));
									    	 }
									     }
									     
									     i1 = displayList.size();
									} catch (Exception e) {
										cancel();
									}
								} else {
									cancel();
								}
							}
						}.runTaskTimerAsynchronously(plugin, 0L, 20L);
						
					    player.setScoreboard(scoreboard);
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTask(plugin);
	}
	
	private String replaceDisplayName(String displayName) {
		displayName = displayName.replace("%players_online", "" + Bukkit.getServer().getOnlinePlayers().size()).replace("%players_max", "" + Bukkit.getServer().getMaxPlayers());
		
		return displayName;
	}
	
	private String replaceDisplayLine(String displayLine) {
		Main plugin = Main.getInstance();
		
		PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
		IslandManager islandManager = plugin.getIslandManager();
		displayLine = displayLine.replace("%players_online", "" + Bukkit.getServer().getOnlinePlayers().size()).replace("%players_max", "" + Bukkit.getServer().getMaxPlayers());
		
		if (islandManager.hasIsland(player)) {
			Island island = islandManager.getIsland(playerData.getOwner());
			Level level = island.getLevel();
			
			if (island.getRole(Role.Member).size() == 0 && island.getRole(Role.Operator).size() == 0) {
				displayLine = displayLine.replace("%island_level", "" + NumberUtil.formatNumber(level.getLevel())).replace("%island_members", ChatColor.RED + "0").replace("%island_role", ChatColor.RED + "null").replace("%island_visitors", "" + island.getVisitors().size());
			} else {
				int islandMembers = 1 + island.getRole(Role.Member).size() + island.getRole(Role.Operator).size();
				String islandRole = "";
				
				if (island.isRole(Role.Owner, player.getUniqueId())) {
					islandRole = displayVariables.get("%owner");
				} else if (island.isRole(Role.Operator, player.getUniqueId())) {
					islandRole = displayVariables.get("%operator");
				} else if (island.isRole(Role.Member, player.getUniqueId())) {
					islandRole = displayVariables.get("%member");
				}
				
				displayLine = displayLine.replace("%island_level", "" + NumberUtil.formatNumber(level.getLevel())).replace("%island_members", "" + islandMembers).replace("%island_role", islandRole).replace("%island_visitors", "" + island.getVisitors().size());
			}
		} else {
			displayLine = displayLine.replace("%island_level", ChatColor.RED + "0").replace("%island_members", ChatColor.RED + "0").replace("%island_role", ChatColor.RED + "null");
		}
		
		return displayLine;
	}

	public void cancel() {
		scheduler.cancel();
	}
}
