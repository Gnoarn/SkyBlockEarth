package me.goodandevil.skyblock.api.event.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWithdrawMoneyEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private OfflinePlayer player;
	private double money;

	public PlayerWithdrawMoneyEvent(OfflinePlayer player, double money) {
		this.player = player;
		this.money = money;
	}

	public OfflinePlayer getPlayer() {
		return player;
	}

	public double getMoney() {
		return money;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
