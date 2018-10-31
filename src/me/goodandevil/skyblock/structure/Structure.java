package me.goodandevil.skyblock.structure;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class Structure {

	private Material material;
	private int data;
	
	private String fileName;
	private String structureName;
	private String displayName;
	private String permission;
	
	private List<String> description = new ArrayList<>();
	
	public Structure(Material material, int data, String fileName, String structureName, String displayName, String permission, List<String> description) {
		this.material = material;
		this.data = data;
		this.fileName = fileName;
		this.structureName = structureName;
		this.displayName = displayName;
		this.permission = permission;
		this.description = description;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getData() {
		return data;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getStructureName() {
		return structureName;
	}
	
	public String getDisplayname() {
		return displayName;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public List<String> getDescription() {
		return description;
	}
}
