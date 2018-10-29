package me.goodandevil.skyblock.utils.structure;

public class StructureStorage {
	
    private String blocks;
    private String entities;
    private String originLocation;
    
    private long time;
    
    public StructureStorage(String blocks, String entities, String originLocation, long time) {
    	this.blocks = blocks;
    	this.entities = entities;
    	this.originLocation = originLocation;
    	this.time = time;
    }
    
    public String getBlocks() {
    	return blocks;
    }
    
    public String getEntities() {
    	return entities;
    }
    
    public String getOriginLocation() {
    	return originLocation;
    }
    
    public long getTime() {
    	return time;
    }
}