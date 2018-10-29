package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.ban.Ban;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.island.IslandSettings;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.version.Sounds;

public class UnbanCommand extends SubCommand {

	private String info;
	
	@Override
	public void onCommand(Player player, String[] args) {
		IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
		PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
		
		PlayerData playerData = playerDataManager.getPlayerData(player);
		
		Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (args.length == 1) {
			if (islandManager.hasIsland(player)) {
				me.goodandevil.skyblock.island.Island island = islandManager.getIsland(playerData.getOwner());
				
				if (island.isRole(IslandRole.Owner, player.getUniqueId()) || (island.isRole(IslandRole.Operator, player.getUniqueId()) && island.getSetting(IslandSettings.Role.Operator, "Unban").getStatus())) {
					UUID targetPlayerUUID = null;
					String targetPlayerName = null;
					
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					
					if (targetPlayer == null) {
						OfflinePlayer targetPlayerOffline = new OfflinePlayer(args[0]);
						targetPlayerUUID = targetPlayerOffline.getUniqueId();
						targetPlayerName = targetPlayerOffline.getName();
					} else {
						targetPlayerUUID = targetPlayer.getUniqueId();
						targetPlayerName = targetPlayer.getName();
					}
					
					if (targetPlayerUUID == null) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Found.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else if (targetPlayerUUID.equals(player.getUniqueId())) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Yourself.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else if (island.isRole(IslandRole.Member, targetPlayerUUID) || island.isRole(IslandRole.Operator, targetPlayerUUID) || island.isRole(IslandRole.Owner, targetPlayerUUID)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Member.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else if (!island.getBan().isBanned(targetPlayerUUID)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Banned.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					} else {						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Unbanned.Message").replace("%player", targetPlayerName)));
						player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
						
						Ban ban = island.getBan();
						ban.removeBan(targetPlayerUUID);
						ban.save();
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Permission.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Owner.Message")));
				player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			}	
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Unban.Invalid.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "unban";
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
