package me.goodandevil.skyblock.api.event.island;

import me.goodandevil.skyblock.api.island.Island;

import org.bukkit.block.Biome;
import org.bukkit.event.HandlerList;

public class IslandBiomeChangeEvent extends IslandEvent {
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private final Biome oldBiome;
	private Biome newBiome;
	
	public IslandBiomeChangeEvent(Island island, Biome oldBiome, Biome newBiome) {
		super(island);
		this.oldBiome = oldBiome;
		this.newBiome = newBiome;
	}
	
	public Biome getOldBiome() {
		return oldBiome;
	}
	
	public void setNewBiome(Biome newBiome) {
		this.newBiome = newBiome;
	}
	
	public Biome getNewBiome() {
		return newBiome;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
}