package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.WeatherType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class IslandWeatherChangeEvent extends IslandEvent implements Cancellable {
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	private WeatherType weather;
	private int time;
	private boolean cancelled = false;
	
	private final boolean sync;
	
	public IslandWeatherChangeEvent(Island island, WeatherType weather, int time, boolean sync) {
		super(island);
		this.weather = weather;
		this.time = time;
		this.sync = sync;
	}
	
	public void setWeather(WeatherType weather) {
		this.weather = weather;
	}
	
	public WeatherType getWeather() {
		return weather;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public int getTime() {
		return time;
	}
	
	public boolean isSync() {
		return sync;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}