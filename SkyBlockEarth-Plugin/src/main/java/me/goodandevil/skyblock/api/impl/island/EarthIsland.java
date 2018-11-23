package me.goodandevil.skyblock.api.impl.island;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import me.goodandevil.skyblock.api.impl.APIUtils;
import me.goodandevil.skyblock.api.island.Island;
import me.goodandevil.skyblock.api.island.IslandEnvironment;
import me.goodandevil.skyblock.api.island.IslandMessage;
import me.goodandevil.skyblock.api.island.IslandRole;
import me.goodandevil.skyblock.api.island.IslandSetting;
import me.goodandevil.skyblock.api.island.IslandWorld;
import me.goodandevil.skyblock.api.level.Level;
import me.goodandevil.skyblock.island.Role;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class EarthIsland implements Island {
	
	private final me.goodandevil.skyblock.island.Island handle;
	
	public EarthIsland(me.goodandevil.skyblock.island.Island handle) {
		this.handle = handle;
	}

	@Override
	public OfflinePlayer getOwner() {
		return Bukkit.getOfflinePlayer(getOwnerUUID());
	}

	@Override
	public void setOwner(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot set owner to null player");
		this.handle.setOwnerUUID(player.getUniqueId());
	}

	@Override
	public UUID getOwnerUUID() {
		return handle.getOwnerUUID();
	}

	@Override
	public void setOwner(UUID playerUUID) {
		Preconditions.checkArgument(playerUUID != null, "Cannot set owner to null player");
		this.handle.setOwnerUUID(playerUUID);
	}

	@Override
	public OfflinePlayer getOriginalOwner() {
		return Bukkit.getOfflinePlayer(getOriginalOwnerUUID());
	}

	@Override
	public UUID getOriginalOwnerUUID() {
		return handle.getOriginalOwnerUUID();
	}

	@Override
	public boolean hasPassword() {
		return handle.hasPassword();
	}

	@Override
	public String getPassword() {
		return handle.getPassword();
	}

	@Override
	public void setPassword(String password) {
		this.handle.setPassword(password);
	}

	@Override
	public Location getLocation(IslandWorld world, IslandEnvironment environment) {
		Preconditions.checkArgument(world != null, "World in island world null does not exist");
		Preconditions.checkArgument(environment != null, "World in environment null does not exist");
		
		return handle.getLocation(APIUtils.toImplementation(world), APIUtils.toImplementation(environment));
	}

	@Override
	public void setLocation(IslandWorld world, IslandEnvironment environment, int x, int y, int z) {
		World bukkitWorld = getLocation(world, environment).getWorld();
		this.handle.setLocation(APIUtils.toImplementation(world), APIUtils.toImplementation(environment), new Location(bukkitWorld, x, y, z));
	}

	@Override
	public Biome getBiome() {
		return handle.getBiome();
	}

	@Override
	public void setBiome(Biome biome) {
		this.handle.setBiome(biome);
	}

	@Override
	public boolean isWeatherSynchronized() {
		return handle.isWeatherSynchronised();
	}

	@Override
	public void setWeatherSynchronized(boolean sync) {
		this.handle.setWeatherSynchronised(sync);
	}

	@Override
	public WeatherType getWeather() {
		return handle.getWeather();
	}

	@Override
	public void setWeather(WeatherType weather) {
		this.handle.setWeather(weather);
	}

	@Override
	public IslandRole getRole(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot get role for null player");
		
		for (Role role : Role.values()) { // ew...
			if (handle.isRole(role, player.getUniqueId())) {
				return APIUtils.fromImplementation(role);
			}
		}
		
		return null;
	}

	@Override
	public Set<UUID> getPlayersWithRole(IslandRole role) {
		Preconditions.checkArgument(role != null, "Cannot get players will null role");
		return new HashSet<>(handle.getRole(APIUtils.toImplementation(role))); // TODO: This should be a HashSet already...
	}

	@Override
	public void setRole(OfflinePlayer player, IslandRole role) {
		Preconditions.checkArgument(player != null, "Cannot set role of null player");
		this.setRole(player.getUniqueId(), role);
	}

	@Override
	public void setRole(UUID playerUUID, IslandRole role) {
		Preconditions.checkArgument(playerUUID != null, "Cannot set role of null player");
		Preconditions.checkArgument(role != null, "Cannot set player to null role");
		
		this.handle.setRole(APIUtils.toImplementation(role), playerUUID);
	}

	@Override
	public boolean removeRole(IslandRole role, UUID uuid) {
		this.handle.removeRole(APIUtils.toImplementation(role), uuid);
		return true; // TODO: Return based on the result of #removeRole()
	}

	@Override
	public boolean hasRole(OfflinePlayer player, IslandRole role) {
		Preconditions.checkArgument(player != null, "Cannot check role of null player");
		return handle.isRole(APIUtils.toImplementation(role), player.getUniqueId());
	}

	@Override
	public boolean hasRole(UUID playerUUID, IslandRole role) {
		Preconditions.checkArgument(playerUUID != null, "Cannot check role of null player");
		Preconditions.checkArgument(role != null, "Cannot check for a null role");
		
		return handle.isRole(APIUtils.toImplementation(role), playerUUID);
	}

	@Override
	public Level getLevelData() {
		return null; // TODO
	}

	@Override
	public <T> T getSettingValue(IslandSetting<T> setting) {
		return null; // TODO
	}

	@Override
	public <T> void updateSetting(IslandSetting<T> setting, T value) {
		// TODO
	}

	@Override
	public Map<IslandSetting<?>, Object> getSettings() {
		return null; // TODO
	}

	@Override
	public void setOpen(boolean open) {
		this.handle.setOpen(open);
	}

	@Override
	public boolean isOpen() {
		return handle.isOpen();
	}

	@Override
	public List<String> getMessage(IslandMessage message) {
		return handle.getMessage(APIUtils.toImplementation(message));
	}

	@Override
	public UUID getMessageAuthor(IslandMessage message) {
		return UUID.fromString(handle.getMessageAuthor(APIUtils.toImplementation(message)));
	}

	@Override
	public Set<OfflinePlayer> getVisitors() {
		return handle.getVisitors().stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toSet()); // TODO
	}

	@Override
	public Set<UUID> getVisitorUUIDs() {
		return new HashSet<>(handle.getVisitors()); // TODO This should be a HashSet anyways
	}

	@Override
	public int getVisitorCount() {
		return handle.getVisitors().size();
	}

	@Override
	public void addVisitor(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "A null player cannot visit an island");
		this.handle.getVisit().addVisitor(player.getUniqueId());
	}

	@Override
	public void addVisitor(UUID playerUUID) {
		Preconditions.checkArgument(playerUUID != null, "A null player cannot visit an island");
		this.handle.getVisit().addVisitor(playerUUID);
	}

	@Override
	public boolean removeVisitor(OfflinePlayer player) {
		return true; // TODO
	}

	@Override
	public boolean removeVisitor(UUID playerUUID) {
		return false; // TODO
	}

	@Override
	public boolean isVisitor(OfflinePlayer player) {
		return player != null && handle.getVisit().isVisitor(player.getUniqueId());
	}

	@Override
	public boolean isVisitor(UUID playerUUID) {
		return handle.getVisit().isVisitor(playerUUID);
	}

	@Override
	public Set<OfflinePlayer> getBannedPlayers() {
		return handle.getBan().getBans().stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toSet());
	}

	@Override
	public Set<UUID> getBannedPlayerUUIDs() {
		return new HashSet<>(handle.getBan().getBans());
	}

	@Override
	public void addBan(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot ban a null player");
		this.handle.getBan().addBan(player.getUniqueId());
	}

	@Override
	public void addBan(UUID playerUUID) {
		Preconditions.checkArgument(playerUUID != null, "Cannot ban a null player");
		this.handle.getBan().addBan(playerUUID);
	}

	@Override
	public boolean removeBan(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot remove ban from null player");
		this.handle.getBan().removeBan(player.getUniqueId());
		return true; // TODO Result based on removal state
	}

	@Override
	public boolean removeBan(UUID playerUUID) {
		Preconditions.checkArgument(playerUUID != null, "Cannot remove ban from null player");
		this.handle.getBan().removeBan(playerUUID);
		return true; // TODO Result based on removal state
	}

	@Override
	public boolean isBanned(OfflinePlayer player) {
		return player != null && handle.getBan().isBanned(player.getUniqueId());
	}

	@Override
	public boolean isBanned(UUID playerUUID) {
		return handle.getBan().isBanned(playerUUID);
	}
	
}