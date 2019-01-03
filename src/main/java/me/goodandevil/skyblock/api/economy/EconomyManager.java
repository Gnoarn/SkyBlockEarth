package me.goodandevil.skyblock.api.economy;

import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

public class EconomyManager {

	private final me.goodandevil.skyblock.economy.EconomyManager economyManager;

	public EconomyManager(me.goodandevil.skyblock.economy.EconomyManager economyManager) {
		this.economyManager = economyManager;
	}

	public double getBalance(Player player) {
		Preconditions.checkArgument(player != null, "Cannot get balance to null player");

		return this.economyManager.getBalance(player);
	}

	public boolean hasBalance(Player player, double amount) {
		Preconditions.checkArgument(player != null, "Cannot check balance to null player");

		return this.economyManager.hasBalance(player, amount);
	}

	public void withdraw(Player player, double amount) {
		Preconditions.checkArgument(player != null, "Cannot withdraw balance to null player");

		this.economyManager.withdraw(player, amount);
	}

	public void deposit(Player player, double amount) {
		Preconditions.checkArgument(player != null, "Cannot deposit balance to null player");

		this.economyManager.deposit(player, amount);
	}
}
