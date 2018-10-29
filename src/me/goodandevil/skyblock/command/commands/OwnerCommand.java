package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.confirmation.Confirmation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.menus.Ownership;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.ChatComponent;
import me.goodandevil.skyblock.utils.OfflinePlayer;
import me.goodandevil.skyblock.utils.version.Sounds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class OwnerCommand extends SubCommand {

	private String info;
	
	@Override
	public void onCommand(Player player, String[] args) {
		IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
		FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
		
		PlayerData playerData = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager)).getPlayerData(player);
		
		Config config = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (islandManager.hasIsland(player)) {
			me.goodandevil.skyblock.island.Island island = islandManager.getIsland(playerData.getOwner());
			
			if (args.length == 0) {
				if (island.isRole(IslandRole.Owner, player.getUniqueId())) {
					playerData.setType(Ownership.Visibility.Hidden);
					Ownership.getInstance().open(player);
					player.playSound(player.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1.0F, 1.0F);
					
					return;
				}
			} else if (args.length == 1) {
				if (island.isRole(IslandRole.Owner, player.getUniqueId())) {
					if (playerData.getConfirmationTime() > 0) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Confirmation.Pending.Message")));
						player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
					} else {
						UUID targetPlayerUUID;
						String targetPlayerName;
						
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						
						if (targetPlayer == null) {
							OfflinePlayer offlinePlayer = new OfflinePlayer(args[0]);
							targetPlayerUUID = offlinePlayer.getUniqueId();
							targetPlayerName = offlinePlayer.getName();
						} else {
							targetPlayerUUID = targetPlayer.getUniqueId();
							targetPlayerName = targetPlayer.getName();
						}
						
						if (targetPlayerUUID == null || (!island.isRole(IslandRole.Member, targetPlayerUUID) && !island.isRole(IslandRole.Operator, targetPlayerUUID) && !island.isRole(IslandRole.Owner, targetPlayerUUID))) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Member.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else if (targetPlayerUUID.equals(player.getUniqueId())) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Yourself.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						} else {
							int confirmationTime = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "config.yml")).getFileConfiguration().getInt("Island.Confirmation.Timeout");
							
							playerData.setOwnership(targetPlayerUUID);
							playerData.setConfirmation(Confirmation.Ownership);
							playerData.setConfirmationTime(confirmationTime);
							
							player.spigot().sendMessage(new ChatComponent(configLoad.getString("Command.Island.Ownership.Confirmation.Confirm.Message").replace("%player", targetPlayerName).replace("%time", "" + confirmationTime) + "   ", false, null, null, null).addExtra(new ChatComponent(configLoad.getString("Command.Island.Ownership.Confirmation.Confirm.Word.Confirm").toUpperCase(), true, ChatColor.RED, new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/island confirm"), new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Confirmation.Confirm.Word.Tutorial"))).create()))));
							player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
						}
					}
				} else {
					if (island.hasPassword()) {
						if (args[0].equalsIgnoreCase(island.getPassword())) {
							for (Player all : Bukkit.getOnlinePlayers()) {
								if ((island.isRole(IslandRole.Member, all.getUniqueId()) || island.isRole(IslandRole.Operator, all.getUniqueId()) || island.isRole(IslandRole.Owner, all.getUniqueId())) && (!all.getUniqueId().equals(player.getUniqueId()))) {
									all.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Assigned.Broadcast.Message").replace("%player", player.getName())));
									all.playSound(all.getLocation(), Sounds.ANVIL_USE.bukkitSound(), 1.0F, 1.0F);
								}
							}
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Assigned.Sender.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_USE.bukkitSound(), 1.0F, 1.0F);
							
							islandManager.giveIslandOwnership(player.getUniqueId());
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Password.Incorrect.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
						}
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Password.Unset.Message")));
						player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					}
				}
				
				return;
			}
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Invalid.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Ownership.Owner.Message")));
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
		return new String[] { "ownership", "transfer", "makeleader", "makeowner" };
	}

	@Override
	public Type getType() {
		return CommandManager.Type.Default;
	}
}
