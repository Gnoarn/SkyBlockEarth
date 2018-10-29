package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.version.Sounds;

public class DemoteCommand extends SubCommand {

	private final Main plugin;
	private String info;
	
	public DemoteCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		IslandManager islandManager = plugin.getIslandManager();
		PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
		
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (args.length == 1) {
			if (islandManager.hasIsland(player)) {
				me.goodandevil.skyblock.island.Island island = islandManager.getIsland(playerData.getOwner());
				
				if (island.isRole(IslandRole.Owner, player.getUniqueId())) {
					if (Bukkit.getServer().getPlayer(args[0]) == null) {
						OfflinePlayer targetPlayer = new OfflinePlayer(args[0]);
						List<UUID> islandMembers = island.getRole(IslandRole.Member);
						
						if (targetPlayer.getUniqueId() != null && (islandMembers.contains(targetPlayer.getUniqueId()) || island.getRole(IslandRole.Operator).contains(targetPlayer.getUniqueId()))) {
							if (islandMembers.contains(targetPlayer.getUniqueId())) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Role.Message")));
								player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Promoted.Sender.Message").replace("%player", targetPlayer.getName())));
								player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
								
								island.removeRole(IslandRole.Operator, targetPlayer.getUniqueId());
								island.setRole(IslandRole.Member, targetPlayer.getUniqueId());
								island.save();
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Member.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						}
					} else {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						
						if (island.isRole(IslandRole.Member, targetPlayer.getUniqueId()) || island.isRole(IslandRole.Operator, targetPlayer.getUniqueId())) {
							if (island.isRole(IslandRole.Member, targetPlayer.getUniqueId())) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Role.Message")));
								player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Demoted.Sender.Message").replace("%player", targetPlayer.getName())));
								targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Demoted.Target.Message")));
								player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
								targetPlayer.playSound(targetPlayer.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
								
								island.removeRole(IslandRole.Operator, targetPlayer.getUniqueId());
								island.setRole(IslandRole.Member, targetPlayer.getUniqueId());
								island.save();
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Promote.Member.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						}
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Owner.Message")));
				player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			}	
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Demote.Invalid.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "demote";
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
		return new String[0];
	}

	@Override
	public Type getType() {
		return CommandManager.Type.Default;
	}
}
