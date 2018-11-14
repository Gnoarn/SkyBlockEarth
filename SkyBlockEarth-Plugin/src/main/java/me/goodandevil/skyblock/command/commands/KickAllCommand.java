package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.events.IslandKickEvent;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Role;
import me.goodandevil.skyblock.island.Settings;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class KickAllCommand extends SubCommand {

	private final SkyBlockEarth plugin;
	private String info;
	
	public KickAllCommand(SkyBlockEarth plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
		IslandManager islandManager = plugin.getIslandManager();
		
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (islandManager.hasIsland(player)) {
			PlayerData playerData = playerDataManager.getPlayerData(player);
			Island island = islandManager.getIsland(playerData.getOwner());
			
			if (island.isRole(Role.Owner, player.getUniqueId()) || (island.isRole(Role.Operator, player.getUniqueId()) && island.getSetting(Settings.Role.Operator, "Kick").getStatus())) {
				if (island.isOpen()) {
					List<UUID> islandVisitors = island.getVisitors();
					
					if (islandVisitors.size() == 0) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.KickAll.Visitors.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else {
						for (UUID islandVisitorList : islandVisitors) {
							Player targetPlayer = Bukkit.getServer().getPlayer(islandVisitorList);
							
							IslandKickEvent islandKickEvent = new IslandKickEvent(island, Role.Visitor, islandVisitorList, player);
							Bukkit.getServer().getPluginManager().callEvent(islandKickEvent);
							
							if (!islandKickEvent.isCancelled()) {
								LocationUtil.teleportPlayerToSpawn(targetPlayer);
								
								targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.KickAll.Kicked.Target.Message").replace("%player", player.getName())));
								targetPlayer.playSound(targetPlayer.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
							}
						}
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.KickAll.Kicked.Sender.Message").replace("%visitors", "" + islandVisitors.size())));
						player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.KickAll.Closed.Message")));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.KickAll.Role.Message")));
				player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.KickAll.Owner.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "expel";
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
