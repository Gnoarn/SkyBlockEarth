package me.goodandevil.skyblock.command.commands;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.confirmation.Confirmation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Role;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.utils.ChatComponent;
import me.goodandevil.skyblock.utils.version.Sounds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class DeleteCommand extends SubCommand {

	private final SkyBlockEarth plugin;
	private String info;
	
	public DeleteCommand(SkyBlockEarth plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		IslandManager islandManager = plugin.getIslandManager();
		FileManager fileManager = plugin.getFileManager();
		
		PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
		
		Config config = fileManager.getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (islandManager.hasIsland(player)) {
			if (islandManager.getIsland(playerData.getOwner()).isRole(Role.Owner, player.getUniqueId())) {
				if (playerData.getConfirmationTime() > 0) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Delete.Confirmation.Pending.Message")));
					player.playSound(player.getLocation(), Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
				} else {
					int confirmationTime = fileManager.getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getInt("Island.Confirmation.Timeout");
					
					playerData.setConfirmation(Confirmation.Deletion);
					playerData.setConfirmationTime(confirmationTime);
					
					player.spigot().sendMessage(new ChatComponent(configLoad.getString("Command.Island.Delete.Confirmation.Confirm.Message").replace("%time", "" + confirmationTime) + "   ", false, null, null, null).addExtra(new ChatComponent(configLoad.getString("Command.Island.Delete.Confirmation.Confirm.Word.Confirm").toUpperCase(), true, ChatColor.RED, new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/island confirm"), new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Delete.Confirmation.Confirm.Word.Tutorial"))).create()))));
					player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Delete.Permission.Message")));
				player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Delete.Owner.Message")));
			player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
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
		return CommandManager.Type.Default;
	}
}
