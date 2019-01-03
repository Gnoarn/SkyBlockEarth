package me.goodandevil.skyblock.message;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.placeholder.PlaceholderManager;

public class MessageManager {

	private final SkyBlock skyblock;

	public MessageManager(SkyBlock skyblock) {
		this.skyblock = skyblock;
	}

	public void sendMessage(CommandSender sender, String message) {
		if (sender instanceof Player) {
			PlaceholderManager placeholderManager = skyblock.getPlaceholderManager();
			Player player = (Player) sender;

			if (placeholderManager.isPlaceholderAPIEnabled()) {
				message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message.replace("&", "clr"))
						.replace("clr", "&");
			}

			if (message.contains("\n") || message.contains("\\n")) {
				List<String> messages = new ArrayList<>();

				message = message.replace("\\n", "\n");

				for (String messageList : message.split("\n")) {
					messages.add(ChatColor.translateAlternateColorCodes('&', messageList));
				}

				sender.sendMessage(messages.toArray(new String[0]));
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}
		} else {
			if (message.contains("\n") || message.contains("\\n")) {
				List<String> messages = new ArrayList<>();

				message = message.replace("\\n", "\n");

				for (String messageList : message.split("\n")) {
					messages.add(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', messageList)));
				}

				sender.sendMessage(messages.toArray(new String[0]));
			} else {
				sender.sendMessage(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)));
			}
		}
	}

	public String replaceMessage(Player player, String message) {
		PlaceholderManager placeholderManager = skyblock.getPlaceholderManager();

		if (placeholderManager.isPlaceholderAPIEnabled()) {
			message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message.replace("&", "clr"))
					.replace("clr", "&");
		}

		return message;
	}
}
