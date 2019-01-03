package me.goodandevil.skyblock.command.commands;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.api.event.island.IslandKickEvent;
import me.goodandevil.skyblock.api.utils.APIUtil;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.command.SubCommand;
import me.goodandevil.skyblock.command.CommandManager.Type;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.message.MessageManager;
import me.goodandevil.skyblock.sound.SoundManager;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class KickAllCommand extends SubCommand {

	private final SkyBlock skyblock;
	private String info;

	public KickAllCommand(SkyBlock skyblock) {
		this.skyblock = skyblock;
	}

	@Override
	public void onCommandByPlayer(Player player, String[] args) {
		MessageManager messageManager = skyblock.getMessageManager();
		IslandManager islandManager = skyblock.getIslandManager();
		SoundManager soundManager = skyblock.getSoundManager();

		Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();

		Island island = islandManager.getIsland(player);

		if (island == null) {
			messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Owner.Message"));
			soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		} else if (island.hasRole(IslandRole.Owner, player.getUniqueId())
				|| (island.hasRole(IslandRole.Operator, player.getUniqueId())
						&& island.getSetting(IslandRole.Operator, "Kick").getStatus())) {
			if (island.isOpen()) {
				Set<UUID> islandVisitors = islandManager.getVisitorsAtIsland(island);

				if (islandVisitors.size() == 0) {
					messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Visitors.Message"));
					soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
				} else {
					for (UUID islandVisitorList : islandVisitors) {
						Player targetPlayer = Bukkit.getServer().getPlayer(islandVisitorList);

						IslandKickEvent islandKickEvent = new IslandKickEvent(island.getAPIWrapper(),
								APIUtil.fromImplementation(IslandRole.Visitor),
								Bukkit.getServer().getOfflinePlayer(islandVisitorList), player);
						Bukkit.getServer().getPluginManager().callEvent(islandKickEvent);

						if (!islandKickEvent.isCancelled()) {
							LocationUtil.teleportPlayerToSpawn(targetPlayer);

							messageManager.sendMessage(targetPlayer,
									configLoad.getString("Command.Island.KickAll.Kicked.Target.Message")
											.replace("%player", player.getName()));
							soundManager.playSound(targetPlayer, Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
						}
					}

					messageManager.sendMessage(player,
							configLoad.getString("Command.Island.KickAll.Kicked.Sender.Message").replace("%visitors",
									"" + islandVisitors.size()));
					soundManager.playSound(player, Sounds.IRONGOLEM_HIT.bukkitSound(), 1.0F, 1.0F);
				}
			} else {
				messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Closed.Message"));
				soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			}
		} else {
			messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Role.Message"));
			soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
		sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
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
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public Type getType() {
		return CommandManager.Type.Default;
	}
}
