package me.goodandevil.skyblock.utils.structure;

public class Structure {
	
	private StructureStorage structureStorage;
    private String file;
    
    public Structure(StructureStorage structureStorage, String file) {
        this.structureStorage = structureStorage;
    	this.file = file;
    }
    
    public StructureStorage getStructureStorage() {
    	return structureStorage;
    }

    public String getStructureFile(){
        return this.file;
    }
}
