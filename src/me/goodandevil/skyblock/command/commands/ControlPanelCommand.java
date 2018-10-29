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
import me.goodandevil.skyblock.menus.ControlPanel;
import me.goodandevil.skyblock.utils.version.Sounds;

public class ControlPanelCommand extends SubCommand {

	private final Main plugin;
	private String info;
	
	public ControlPanelCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (plugin.getIslandManager().hasIsland(player)) {
			ControlPanel.getInstance().open(player);
			player.playSound(player.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1.0F, 1.0F);
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.ControlPanel.Owner.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "controlpanel";
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
		return new String[] { "cp" };
	}

	@Override
	public Type getType() {
		return CommandManager.Type.Default;
	}
}
