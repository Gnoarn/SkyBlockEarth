package me.goodandevil.skyblock.command.commands.admin;

import java.io.File;
import java.util.UUID;

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
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.version.Sounds;

public class OwnerCommand extends SubCommand {

	private String info;
	
	@Override
	public void onCommand(Player player, String[] args) {
		PlayerDataManager playerDataManager = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager));
		FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
		
		Config config = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (player.hasPermission("skyblock.admin.owner") || player.hasPermission("skyblock.admin.*") || player.hasPermission("skyblock.*")) {
			if (args.length == 1) {
				Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
				UUID targetPlayerUUID, islandOwnerUUID;
				String targetPlayerName, islandOwnerName;
				
				if (targetPlayer == null) {
					OfflinePlayer targetPlayerOffline = new OfflinePlayer(args[0]);
					targetPlayerUUID = targetPlayerOffline.getUniqueId();
					islandOwnerUUID = targetPlayerOffline.getOwner();
					targetPlayerName = targetPlayerOffline.getName();
				} else {
					targetPlayerUUID = targetPlayer.getUniqueId();
					islandOwnerUUID = playerDataManager.getPlayerData(player).getOwner();
					targetPlayerName = targetPlayer.getName();
				}
				
				if (islandOwnerUUID == null) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Owner.Island.None.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				} else if (islandOwnerUUID.equals(targetPlayerUUID)) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Owner.Island.Owner.Message").replace("%player", targetPlayerName)));
					player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
				} else {
					targetPlayer = Bukkit.getServer().getPlayer(islandOwnerUUID);
					
					if (targetPlayer == null) {
						islandOwnerName = new OfflinePlayer(islandOwnerUUID).getName();
					} else {
						islandOwnerName = targetPlayer.getName();
					}
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Owner.Island.Member.Message").replace("%player", targetPlayerName).replace("%owner", islandOwnerName)));
					player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Owner.Invalid.Message")));
				player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Admin.Owner.Permission.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "owner";
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
		return new String[] { "ownership", "leader" };
	}
	
	@Override
	public Type getType() {
		return CommandManager.Type.Admin;
	}
}
