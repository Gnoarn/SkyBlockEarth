package me.goodandevil.skyblock.utils.world;

import java.io.File;

import org.bukkit.Location;

public class Schematic {
	
    private static Schematic instance;

    public static Schematic getInstance() {
        if(instance == null) {
            instance = new Schematic();
        }
        
        return instance;
    }
    
    public void save(File schematicFile, Location pos1Location, Location pos2Location) throws Exception {
    	/*Vector bot = new Vector(pos1Location.getBlockX(), 0, pos1Location.getBlockZ());
    	Vector top = new Vector(pos2Location.getBlockX(), 255, pos2Location.getBlockZ());
    	CuboidRegion region = new CuboidRegion(new BukkitWorld(pos1Location.getWorld()), bot, top);
    	com.boydti.fawe.object.schematic.Schematic schem = new com.boydti.fawe.object.schematic.Schematic(region);
    	ClipboardFormat clipboardFormat = null;
    	
    	for (ClipboardFormat clipboardFormatList : EnumSet.allOf(ClipboardFormat.class)) {
    		if (clipboardFormatList.toString().equals("SCHEMATIC")) {
    			clipboardFormat = clipboardFormatList;
    			break;
    		}
    	}
    	
    	schem.save(schematicFile, clipboardFormat);*/
    }
    
	public void paste(File schematicFile, Location location, boolean pasteAir) throws Exception {
		/*Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		World weWorld = new BukkitWorld(location.getWorld());
		EditSession editSession = ClipboardFormat.findByFile(schematicFile).load(schematicFile).paste(weWorld, vector, false, pasteAir, (Transform) null);
		editSession.flushQueue();*/
	}
}
