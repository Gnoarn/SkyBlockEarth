package me.goodandevil.skyblock.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.goodandevil.skyblock.api.event.player.PlayerDepositMoneyEvent;
import me.goodandevil.skyblock.api.event.player.PlayerWithdrawMoneyEvent;
import net.milkbowl.vault.economy.Economy;
import net.nifheim.beelzebu.coins.CoinsAPI;

public class EconomyManager {

	private EconomyPlugin economyPlugin;
	private Economy economy;

	public EconomyManager() {
		setup();
	}

	public void setup() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServer().getServicesManager()
					.getRegistration(Economy.class);

			if (registeredServiceProvider != null) {
				economy = registeredServiceProvider.getProvider();
			}

			economyPlugin = EconomyPlugin.Vault;
		} else if (Bukkit.getServer().getPluginManager().getPlugin("Coins") != null) {
			economyPlugin = EconomyPlugin.Coins;
		}
	}

	public double getBalance(OfflinePlayer player) {
		if (economy != null) {
			return economy.getBalance(player);
		} else if (economyPlugin == EconomyPlugin.Coins) {
			return CoinsAPI.getCoins(player.getUniqueId());
		}

		return 0.0D;
	}

	public boolean hasBalance(OfflinePlayer player, double amount) {
		if (getBalance(player) >= amount) {
			return true;
		}

		return false;
	}

	public void withdraw(OfflinePlayer player, double amount) {
		if (economy != null) {
			economy.withdrawPlayer(player, amount);
		} else if (economyPlugin == EconomyPlugin.Coins) {
			CoinsAPI.takeCoins(player.getUniqueId(), amount);
		}

		Bukkit.getServer().getPluginManager().callEvent(new PlayerWithdrawMoneyEvent(player, amount));
	}

	public void deposit(OfflinePlayer player, double amount) {
		if (economy != null) {
			economy.depositPlayer(player, amount);
		} else if (economyPlugin == EconomyPlugin.Coins) {
			CoinsAPI.addCoins(player.getUniqueId(), amount, true);
		}

		Bukkit.getServer().getPluginManager().callEvent(new PlayerDepositMoneyEvent(player, amount));
	}

	public boolean isEconomy() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null
				&& Bukkit.getServer().getPluginManager().getPlugin("Coins") == null) {
			return false;
		}

		return true;
	}

	public enum EconomyPlugin {

		Vault, Coins;

	}
}
