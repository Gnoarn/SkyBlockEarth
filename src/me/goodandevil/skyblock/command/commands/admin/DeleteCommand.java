package me.goodandevil.skyblock.command.commands.admin;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.scoreboard.Scoreboard;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class DeleteCommand extends SubCommand {

	private final Main plugin;
	private String info;
	
	public DeleteCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		FileManager fileManager = plugin.getFileManager();
		IslandManager islandManager = plugin.getIslandManager();
		
		Config config = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (player.hasPermission("skyblock.admin.delete") || player.hasPermission("skyblock.admin.*") || player.hasPermission("skyblock.*")) {
			if (args.length == 1) {
				Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
				UUID targetPlayerUUID;
				String targetPlayerName;
				
				if (targetPlayer == null) {
					OfflinePlayer targetPlayerOffline = new OfflinePlayer(args[0]);
					targetPlayerUUID = targetPlayerOffline.getUniqueId();
					targetPlayerName = targetPlayerOffline.getName();
				} else {
					targetPlayerUUID = targetPlayer.getUniqueId();
					targetPlayerName = targetPlayer.getName();
				}
				
				if (islandManager.isIslandExist(targetPlayerUUID)) {
					islandManager.loadIsland(targetPlayerUUID);
					
					boolean hasSpawnPoint = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "locations.yml")).getFileConfiguration().getString("Location.Spawn") != null;
					Island island = islandManager.getIsland(targetPlayerUUID);
					
					for (Player all : Bukkit.getOnlinePlayers()) {
						if (island.isRole(IslandRole.Member, all.getUniqueId()) || island.isRole(IslandRole.Operator, all.getUniqueId()) || island.isRole(IslandRole.Owner, all.getUniqueId())) {
							Scoreboard scoreboard = plugin.getScoreboardManager().getScoreboard(all);
							scoreboard.cancel();
							scoreboard.setDisplayName(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Scoreboard.Tutorial.Displayname")));
							scoreboard.setDisplayList(configLoad.getStringList("Scoreboard.Tutorial.Displaylines"));
							scoreboard.run();
							
							for (IslandLocation.World worldList : IslandLocation.World.values()) {
								if (LocationUtil.isLocationAtLocationRadius(all.getLocation(), island.getLocation(worldList, IslandLocation.Environment.Island), 85)) {
									if (hasSpawnPoint) {
										LocationUtil.teleportPlayerToSpawn(all);
									} else {
										Bukkit.getServer().getLogger().log(Level.WARNING, "SkyBlock | Error: A spawn point hasn't been set.");
									}
									
									break;
								}
							}
							
							if (!island.isRole(IslandRole.Owner, all.getUniqueId())) {
								all.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Confirmation.Deletion.Broadcast.Message")));
								all.playSound(player.getLocation(), Sounds.EXPLODE.bukkitSound(), 10.0F, 10.0F);
							}
						}
					}
					
					islandManager.deleteIsland(island);
					plugin.getVisitManager().deleteIsland(player.getUniqueId());
					plugin.getBanManager().deleteIsland(player.getUniqueId());
				
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Delete.Deleted.Message").replace("%player", targetPlayerName)));
					player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Delete.Owner.Message")));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Delete.Invalid.Message")));
				player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Delete.Permission.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "delete";
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
		return new String[] { "remove", "disband" };
	}
	
	@Override
	public Type getType() {
		return CommandManager.Type.Admin;
	}
}
