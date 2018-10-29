package me.goodandevil.skyblock.island;

public class IslandSettings {
	
	private boolean status;
	
	public IslandSettings(boolean status) {
		this.status = status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public enum Role {
		
		Visitor,
		Member,
		Operator,
		Owner;
		
	}
}
