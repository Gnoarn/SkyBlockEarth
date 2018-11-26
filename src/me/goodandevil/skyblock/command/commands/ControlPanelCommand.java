package me.goodandevil.skyblock.command.commands;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.menus.ControlPanel;
import me.goodandevil.skyblock.sound.SoundManager;
import me.goodandevil.skyblock.utils.version.Sounds;

public class ControlPanelCommand extends SubCommand {

	private final SkyBlock skyblock;
	private String info;
	
	public ControlPanelCommand(SkyBlock skyblock) {
		this.skyblock = skyblock;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		SoundManager soundManager = skyblock.getSoundManager();
		
		Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (skyblock.getIslandManager().hasIsland(player)) {
			ControlPanel.getInstance().open(player);
			soundManager.playSound(player, Sounds.CHEST_OPEN.bukkitSound(), 1.0F, 1.0F);
		} else {
			skyblock.getMessageManager().sendMessage(player, configLoad.getString("Command.Island.ControlPanel.Owner.Message"));
			soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
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
