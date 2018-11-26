package me.goodandevil.skyblock.command.commands;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.Role;
import me.goodandevil.skyblock.island.Settings;
import me.goodandevil.skyblock.menus.Bans;
import me.goodandevil.skyblock.message.MessageManager;
import me.goodandevil.skyblock.sound.SoundManager;
import me.goodandevil.skyblock.utils.version.Sounds;

public class BansCommand extends SubCommand {

	private final SkyBlock skyblock;
	private String info;
	
	public BansCommand(SkyBlock skyblock) {
		this.skyblock = skyblock;
	}
	
	@Override
	public void onCommand(Player player, String[] args) {
		MessageManager messageManager = skyblock.getMessageManager();
		IslandManager islandManager = skyblock.getIslandManager();
		SoundManager soundManager = skyblock.getSoundManager();
		
		Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		if (islandManager.hasIsland(player)) {
			me.goodandevil.skyblock.island.Island island = islandManager.getIsland(skyblock.getPlayerDataManager().getPlayerData(player).getOwner());
			
			if ((island.isRole(Role.Operator, player.getUniqueId()) && island.getSetting(Settings.Role.Operator, "Unban").getStatus()) || island.isRole(Role.Owner, player.getUniqueId())) {
				if (island.getBan().getBans().size() == 0) {
					messageManager.sendMessage(player, configLoad.getString("Command.Island.Bans.Bans.Message"));
					soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				} else {
					Bans.getInstance().open(player);
					soundManager.playSound(player, Sounds.CHEST_OPEN.bukkitSound(), 1.0F, 1.0F);	
				}
			} else {
				messageManager.sendMessage(player, configLoad.getString("Command.Island.Bans.Permission.Message"));
				soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
			}
		} else {
			messageManager.sendMessage(player, configLoad.getString("Command.Island.Bans.Owner.Message"));
			soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public String getName() {
		return "bans";
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
		return new String[] { "banned" };
	}

	@Override
	public Type getType() {
		return CommandManager.Type.Default;
	}
}
