package me.goodandevil.skyblock.invite;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.SkyBlockEarth;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.utils.ChatComponent;
import me.goodandevil.skyblock.utils.version.Sounds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class InviteTask extends BukkitRunnable {
	
	private final SkyBlockEarth plugin;
	private final InviteManager inviteManager;
	
 	protected InviteTask(InviteManager inviteManager, SkyBlockEarth plugin) {
		this.inviteManager = inviteManager;
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (inviteManager.hasInvite(all.getUniqueId())) {
				Invite invite = inviteManager.getInvite(all.getUniqueId());
				invite.setTime(invite.getTime() - 1);
				
				if (invite.getTime() == 0) {
					Player targetPlayer = Bukkit.getServer().getPlayer(invite.getOwnerUUID());
					
					if (targetPlayer != null) {
						targetPlayer.spigot().sendMessage(new ChatComponent(config.getFileConfiguration().getString("Command.Island.Invite.Invited.Sender.Expired.Message").replace("%player", all.getName()) + "   ", false, null, null, null).addExtra(new ChatComponent(config.getFileConfiguration().getString("Command.Island.Invite.Invited.Word.Resend").toUpperCase(), true, ChatColor.AQUA, new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/island invite " + all.getName()), new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Invite.Invited.Word.Tutorial").replace("%action", config.getFileConfiguration().getString("Command.Island.Invite.Invited.Word.Resend")))).create()))));
						targetPlayer.playSound(targetPlayer.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					}
					
					all.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Invite.Invited.Target.Expired.Message").replace("%player", invite.getSenderName())));
					all.playSound(all.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					inviteManager.removeInvite(all.getUniqueId());
				}
			}
		}
	}
}
