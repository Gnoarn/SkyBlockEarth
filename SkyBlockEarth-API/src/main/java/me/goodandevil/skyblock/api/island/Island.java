package me.goodandevil.skyblock.api.island;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.goodandevil.skyblock.api.level.Level;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;

public interface Island {
	
	public OfflinePlayer getOwner();
	
	public void setOwner(OfflinePlayer player);
	
	public UUID getOwnerUUID();
	
	public void setOwner(UUID playerUUID);
	
	public OfflinePlayer getOriginalOwner();
	
	public UUID getOriginalOwnerUUID();
	
	public boolean hasPassword();
	
	public String getPassword();
	
	public void setPassword(String password);
	
	public Location getLocation(IslandWorld world, IslandEnvironment environment);
	
	public void setLocation(IslandWorld world, IslandEnvironment environment, int x, int y, int z);
	
	public Biome getBiome();
	
	public void setBiome(Biome biome);
	
	public boolean isWeatherSynchronized();
	
	public void setWeatherSynchronized(boolean sync);
	
	public WeatherType getWeather();
	
	public void setWeather(WeatherType weather);
	
	public IslandRole getRole(OfflinePlayer player);

	public Set<UUID> getPlayersWithRole(IslandRole role);

	public void setRole(OfflinePlayer player, IslandRole role);

	public void setRole(UUID playerUUID, IslandRole role);

	public boolean removeRole(IslandRole role, UUID uuid);

	public boolean hasRole(OfflinePlayer player, IslandRole role);

	public boolean hasRole(UUID playerUUID, IslandRole role);
	
	public Level getLevelData();
	
	public <T> T getSettingValue(IslandSetting<T> setting);
	
	public <T> void updateSetting(IslandSetting<T> setting, T value);
	
	public Map<IslandSetting<?>, Object> getSettings();
	
	public void setOpen(boolean open);
	
	public boolean isOpen();
	
	public List<String> getMessage(IslandMessage message);

	public UUID getMessageAuthor(IslandMessage message);
	
	public Set<OfflinePlayer> getVisitors();
	
	public Set<UUID> getVisitorUUIDs();
	
	public int getVisitorCount();
	
	public void addVisitor(OfflinePlayer player);
	
	public void addVisitor(UUID playerUUID);
	
	public boolean removeVisitor(OfflinePlayer player);
	
	public boolean removeVisitor(UUID playerUUID);
	
	public boolean isVisitor(OfflinePlayer player);
	
	public boolean isVisitor(UUID playerUUID);
	
	public Set<OfflinePlayer> getBannedPlayers();
	
	public Set<UUID> getBannedPlayerUUIDs();
	
	public void addBan(OfflinePlayer player);
	
	public void addBan(UUID playerUUID);
	
	public boolean removeBan(OfflinePlayer player);
	
	public boolean removeBan(UUID playerUUID);
	
	public boolean isBanned(OfflinePlayer player);
	
	public boolean isBanned(UUID playerUUID);
	
}