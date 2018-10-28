package me.goodandevil.skyblock.playerdata;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.confirmation.Confirmation;
import me.goodandevil.skyblock.utils.structure.StructureArea;

public class PlayerData {

	private UUID uuid;
	private UUID islandOwnerUUID;
	private UUID ownershipUUID;
	
	private int page;
	private int playTime;
	private int visitTime;
	private int confirmationTime;
	
	private Confirmation confirmation;
	
	private Object type;
	private Object sort;
	
	private StructureArea structureArea;
	
	private boolean chat;
	
	public PlayerData(Player player) {
		uuid = player.getUniqueId();
		islandOwnerUUID = null;
		
		page = 1;
		confirmationTime = 0;
		playTime = getConfig().getFileConfiguration().getInt("Statistics.Island.Playtime");
		
		structureArea = new StructureArea();
		
		chat = false;
	}
	
	public int getMoneyBalance() {
		return getConfig().getFileConfiguration().getInt("Statistics.Money.Balance");
	}
	
	public void setMoneyBalance(int balance) {
		getConfig().getFileConfiguration().set("Statistics.Money.Balance", balance);
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setType(Object type) {
		this.type = type;
	}
	
	public Object getType() {
		return type;
	}
	
	public void setSort(Object sort) {
		this.sort = sort;
	}
	
	public Object getSort() {
		return sort;
	}
	
	public void setIsland(UUID islandOwnerUUID) {
		this.islandOwnerUUID = islandOwnerUUID;
	}
	
	public UUID getIsland() {
		return islandOwnerUUID;
	}
	
	public void setOwnership(UUID ownershipUUID) {
		this.ownershipUUID = ownershipUUID;
	}
	
	public UUID getOwnership() {
		return ownershipUUID;
	}
	
	public void setConfirmationTime(int confirmationTime) {
		this.confirmationTime = confirmationTime;
	}
	
	public int getConfirmationTime() {
		return confirmationTime;
	}
	
	public void setConfirmation(Confirmation confirmation) {
		this.confirmation = confirmation;
	}
	
	public Confirmation getConfirmation() {
		return confirmation;
	}
	
	public boolean hasConfirmation() {
		return confirmationTime > 0;
	}
	
	public void setPlaytime(int playTime) {
		this.playTime = playTime;
	}
	
	public int getPlaytime() {
		return playTime;
	}
	
	public void setVisitTime(int visitTime) {
		this.visitTime = visitTime;
	}
	
	public int getVisitTime() {
		return visitTime;
	}
	
	public String getMemberSince() {
		return getConfig().getFileConfiguration().getString("Statistics.Island.Join");
	}
	
	public void setMemberSince(String date) {
		getConfig().getFileConfiguration().set("Statistics.Island.Join", date);
	}
	
	public UUID getOwner() {
		String islandOwnerUUID = getConfig().getFileConfiguration().getString("Island.Owner");
		
		if (islandOwnerUUID == null) {
			return null;
		} else {
			return UUID.fromString(islandOwnerUUID);
		}
	}
	
	public void setOwner(UUID islandOwnerUUID) {
		if (islandOwnerUUID == null) {
			getConfig().getFileConfiguration().set("Island.Owner", null);
		} else {
			getConfig().getFileConfiguration().set("Island.Owner", islandOwnerUUID.toString());
		}
	}
	
	public String[] getTexture() {
		FileConfiguration configLoad = getConfig().getFileConfiguration();
		
		return new String[] { configLoad.getString("Texture.Signature") , configLoad.getString("Texture.Value") };
	}
	
	public void setTexture(String signature, String value) {
		getConfig().getFileConfiguration().set("Texture.Signature", signature);
		getConfig().getFileConfiguration().set("Texture.Value", value);
	}
	
	public String getLastOnline() {
		return getConfig().getFileConfiguration().getString("Statistics.Island.LastOnline");
	}
	
	public void setLastOnline(String date) {
		getConfig().getFileConfiguration().set("Statistics.Island.LastOnline", date);
	}
	
	public StructureArea getStructureArea() {
		return structureArea;
	}
	
	public boolean isChat() {
		return chat;
	}
	
	public void setChat(boolean chat) {
		this.chat = chat;
	}
	
	public void save() {
		Config config = getConfig();
		FileConfiguration configLoad = config.getFileConfiguration();
		configLoad.set("Statistics.Island.Playtime", getPlaytime());
		
		try {
			configLoad.save(config.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Config getConfig() {
		return ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(new File(Main.getInstance().getDataFolder().toString() + "/player-data"), uuid.toString() + ".yml"));
	}
}
