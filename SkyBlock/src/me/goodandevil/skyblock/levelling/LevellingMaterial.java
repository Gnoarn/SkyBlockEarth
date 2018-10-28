package me.goodandevil.skyblock.levelling;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LevellingMaterial {

	private Material material;
	private int data;
	private int points;
	
	public LevellingMaterial(Material material, int data, int points) {
		this.material = material;
		this.data = data;
		this.points = points;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getData() {
		return data;
	}
	
	public int getPoints() {
		return points;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getItemStack() {
		return new ItemStack(material, 1, (short) data);
	}
}
