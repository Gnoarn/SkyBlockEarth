package me.goodandevil.skyblock.api.island;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import me.goodandevil.skyblock.api.SkyBlockAPI;
import me.goodandevil.skyblock.api.ban.Ban;
import me.goodandevil.skyblock.api.utils.APIUtil;
import me.goodandevil.skyblock.api.visit.Visit;

public class Island {

	private me.goodandevil.skyblock.island.Island handle;
	private OfflinePlayer player;

	public Island(me.goodandevil.skyblock.island.Island handle, OfflinePlayer player) {
		this.handle = handle;
		this.player = player;
	}

	/**
	 * @return The Island owner UUID
	 */
	public UUID getOwnerUUID() {
		return this.handle.getOwnerUUID();
	}

	/**
	 * @return The original Island owner UUID
	 */
	public UUID getOriginalOwnerUUID() {
		return this.handle.getOriginalOwnerUUID();
	}

	/**
	 * @return The Island size
	 */
	public int getSize() {
		return this.handle.getSize();
	}

	/**
	 * Set the size of the Island
	 */
	public void setSize(int size) {
		Preconditions.checkArgument(size <= 1000, "Cannot set size to greater than 1000");
		Preconditions.checkArgument(size >= 50, "Cannot set size to less than 50");
		this.handle.setSize(size);
	}

	/**
	 * @return The Island radius
	 */
	public double getRadius() {
		return this.handle.getRadius();
	}

	/**
	 * @return true if not null, false otherwise
	 */
	public boolean hasPassword() {
		return this.handle.hasPassword();
	}

	/**
	 * Set the password for ownership
	 */
	public void setPassword(String password) {
		Preconditions.checkArgument(password != null, "Cannot set password to null password");
		this.handle.setPassword(password);
	}

	/**
	 * Get the Location from the World in island world from World in environment.
	 * 
	 * @return Location of Island
	 */
	public Location getLocation(IslandWorld world, IslandEnvironment environment) {
		Preconditions.checkArgument(world != null, "World in island world null does not exist");
		Preconditions.checkArgument(environment != null, "World in environment null does not exist");

		return handle.getLocation(APIUtil.toImplementation(world), APIUtil.toImplementation(environment));
	}

	/**
	 * Set the Location from the World in island world from world in environment
	 * followed by position
	 */
	public void setLocation(IslandWorld world, IslandEnvironment environment, int x, int y, int z) {
		Preconditions.checkArgument(world != null, "World in island world null does not exist");
		Preconditions.checkArgument(environment != null, "World in environment null does not exist");

		World bukkitWorld = getLocation(world, environment).getWorld();
		this.handle.setLocation(APIUtil.toImplementation(world), APIUtil.toImplementation(environment),
				new Location(bukkitWorld, x, y, z));
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isBorder() {
		return this.handle.isBorder();
	}

	/**
	 * Set the border visible to players for the Island
	 */
	public void setBorder(boolean border) {
		this.handle.setBorder(border);
	}

	/**
	 * @return The color of the Island border
	 */
	public IslandBorderColor getBorderColor() {
		return APIUtil.fromImplementation(this.handle.getBorderColor());
	}

	/**
	 * Set the border color for the Island
	 */
	public void setBorderColor(IslandBorderColor color) {
		Preconditions.checkArgument(color != null, "IslandBorderColor null does not exist");
		this.handle.setBorderColor(APIUtil.toImplementation(color));
	}

	/**
	 * @return The biome set for the Island
	 */
	public Biome getBiome() {
		return this.handle.getBiome();
	}

	/**
	 * Set the biome for the Island
	 */
	public void setBiome(Biome biome) {
		Preconditions.checkArgument(biome != null, "Cannot set biome to null biome");
		this.handle.setBiome(biome);
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isDayCycleSynchronizedSynchronized() {
		return this.handle.isWeatherSynchronized();
	}

	/**
	 * Set the Day Cycle of the Island to be Synchronized with the World cycle
	 */
	public void setDayCycleSynchronzied(boolean sync) {
		this.handle.setWeatherSynchronized(sync);
	}

	/**
	 * @return The WeatherType set for the Island
	 */
	public WeatherType getWeather() {
		return this.handle.getWeather();
	}

	/**
	 * Set the weather for the Island
	 */
	public void setWeather(WeatherType weatherType) {
		Preconditions.checkArgument(weatherType != null, "Cannot set weather to null weather");
		this.handle.setWeather(weatherType);
	}

	/**
	 * @return The time set for the Island
	 */
	public int getTime() {
		return this.handle.getTime();
	}

	/**
	 * Set the time for the Island
	 */
	public void setTime(int time) {
		this.handle.setTime(time);
	}

	/**
	 * @return A Set of cooped players
	 */
	public Set<UUID> getCoopPlayers() {
		return this.handle.getCoopPlayers();
	}

	/**
	 * Add a player to the coop players for the Island
	 */
	public void addCoopPlayer(UUID uuid) {
		Preconditions.checkArgument(uuid != null, "Cannot add coop player to null uuid");
		this.handle.addCoopPlayer(uuid);
	}

	/**
	 * Add a player to the coop players for the Island
	 */
	public void addCoopPlayer(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot add coop player to null player");
		this.handle.addCoopPlayer(player.getUniqueId());
	}

	/**
	 * Remove a player from the coop players for the Island
	 */
	public void removeCoopPlayer(UUID uuid) {
		Preconditions.checkArgument(uuid != null, "Cannot remove coop player to null uuid");
		this.handle.removeCoopPlayer(uuid);
	}

	/**
	 * Remove a player from the coop players for the Island
	 */
	public void removeCoopPlayer(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot remove coop player to null player");
		this.handle.removeCoopPlayer(player.getUniqueId());
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isCoopPlayer(UUID uuid) {
		Preconditions.checkArgument(uuid != null, "Cannot return condition to null uuid");
		return this.handle.isCoopPlayer(uuid);
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isCoopPlayer(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot return condition to null player");
		return this.handle.isCoopPlayer(player.getUniqueId());
	}

	/**
	 * @return The IslandRole of a player
	 */
	public IslandRole getRole(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot get role for null player");

		for (me.goodandevil.skyblock.island.IslandRole role : me.goodandevil.skyblock.island.IslandRole.values()) {
			if (this.handle.hasRole(role, player.getUniqueId())) {
				return APIUtil.fromImplementation(role);
			}
		}

		return null;
	}

	/**
	 * @return A Set of players with IslandRole
	 */
	public Set<UUID> getPlayersWithRole(IslandRole role) {
		Preconditions.checkArgument(role != null, "Cannot get players will null role");
		return this.handle.getRole(APIUtil.toImplementation(role));
	}

	/**
	 * Set the IslandRole of a player for the Island
	 *
	 * @return true of conditions met, false otherwise
	 */
	public boolean setRole(OfflinePlayer player, IslandRole role) {
		Preconditions.checkArgument(player != null, "Cannot set role of null player");
		return setRole(player.getUniqueId(), role);
	}

	/**
	 * Set the IslandRole of a player for the Island
	 *
	 * @return true of conditions met, false otherwise
	 */
	public boolean setRole(UUID uuid, IslandRole role) {
		Preconditions.checkArgument(uuid != null, "Cannot set role of null player");
		Preconditions.checkArgument(role != null, "Cannot set role to null role");

		return this.handle.setRole(APIUtil.toImplementation(role), uuid);
	}

	/**
	 * Remove the IslandRole of a player for the Island
	 *
	 * @return true of conditions met, false otherwise
	 */
	public boolean removeRole(OfflinePlayer player, IslandRole role) {
		Preconditions.checkArgument(player != null, "Cannot remove role of null player");
		return removeRole(player.getUniqueId(), role);
	}

	/**
	 * Remove the IslandRole of a player for the Island
	 *
	 * @return true of conditions met, false otherwise
	 */
	public boolean removeRole(UUID uuid, IslandRole role) {
		Preconditions.checkArgument(uuid != null, "Cannot remove role of null player");
		Preconditions.checkArgument(role != null, "Cannot remove role to null role");

		return this.handle.removeRole(APIUtil.toImplementation(role), uuid);
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean hasRole(OfflinePlayer player, IslandRole role) {
		Preconditions.checkArgument(player != null, "Cannot check role of null player");
		return handle.hasRole(APIUtil.toImplementation(role), player.getUniqueId());
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean hasRole(UUID uuid, IslandRole role) {
		Preconditions.checkArgument(uuid != null, "Cannot check role of null player");
		Preconditions.checkArgument(role != null, "Cannot check role to null role");

		return handle.hasRole(APIUtil.toImplementation(role), uuid);
	}

	/**
	 * Set the condition of an IslandUpgrade for the Island
	 */
	public void setUpgrade(Player player, IslandUpgrade upgrade, boolean status) {
		Preconditions.checkArgument(upgrade != null, "Cannot set upgrade to null upgrade");
		this.handle.setUpgrade(player, APIUtil.toImplementation(upgrade), status);
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean hasUpgrade(IslandUpgrade upgrade) {
		Preconditions.checkArgument(upgrade != null, "Cannot check upgrade to null upgrade");
		return this.handle.hasUpgrade(APIUtil.toImplementation(upgrade));
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isUpgrade(IslandUpgrade upgrade) {
		Preconditions.checkArgument(upgrade != null, "Cannot check upgrade to null upgrade");
		return this.handle.isUpgrade(APIUtil.toImplementation(upgrade));
	}

	/**
	 * @return Setting of an IslandRole for the Island
	 */
	public IslandSetting getSetting(IslandRole role, String setting) {
		Preconditions.checkArgument(role != null, "Cannot get setting to null role");
		Preconditions.checkArgument(setting != null, "Cannot get setting for null setting");

		return new IslandSetting(this.handle.getSetting(APIUtil.toImplementation(role), setting));
	}

	/**
	 * @return A List of Settings of an IslandRole for the Island
	 */
	public List<IslandSetting> getSettings(IslandRole role) {
		Preconditions.checkArgument(role != null, "Cannot get settings to null role");
		List<IslandSetting> settings = new ArrayList<>();

		for (me.goodandevil.skyblock.island.IslandSetting settingList : this.handle
				.getSettings(APIUtil.toImplementation(role))) {
			settings.add(new IslandSetting(settingList));
		}

		return settings;
	}

	public void setOpen(boolean open) {
		this.handle.setOpen(open);
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isOpen() {
		return handle.isOpen();
	}

	/**
	 * @return A List from IslandMessage for the Island
	 */
	public List<String> getMessage(IslandMessage message) {
		Preconditions.checkArgument(message != null, "Cannot get message for null message");
		return this.handle.getMessage(APIUtil.toImplementation(message));
	}

	/**
	 * @return The author of an IslandMessage for the Island
	 */
	public String getMessageAuthor(IslandMessage message) {
		Preconditions.checkArgument(message != null, "Cannot get message author for null message");
		return this.handle.getMessageAuthor(APIUtil.toImplementation(message));
	}

	/**
	 * Set the IslandMessage for the Island
	 */
	public void setMessage(IslandMessage message, String author, List<String> messageLines) {
		Preconditions.checkArgument(message != null, "Cannot set message for null message");
		this.handle.setMessage(APIUtil.toImplementation(message), author, messageLines);
	}

	/**
	 * Set the Structure for the Island
	 */
	public void setStructure(String structure) {
		Preconditions.checkArgument(structure != null, "Cannot set structure to null structure");
		this.handle.setStructure(structure);
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean hasStructure() {
		return this.handle.hasStructure();
	}

	/**
	 * @return The Structure name for the Island
	 */
	public String getStructure() {
		return this.handle.getStructure();
	}

	/**
	 * @return The Visit implementation for the Island
	 */
	public Visit getVisit() {
		return new Visit(this);
	}

	/**
	 * @return The Ban implementation for the Island
	 */
	public Ban getBan() {
		return new Ban(this);
	}

	/**
	 * @return The Level implementation for the Island
	 */
	public IslandLevel getLevel() {
		return new IslandLevel(this);
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isLoaded() {
		return this.handle != null;
	}

	/**
	 * Loads the Island if unloaded
	 */
	public void load() {
		if (this.handle == null) {
			this.handle = SkyBlockAPI.getImplementation().getIslandManager().loadIsland(player);
		}
	}

	/**
	 * Unloads the Island if loaded
	 */
	public void unload() {
		if (this.handle != null) {
			SkyBlockAPI.getImplementation().getIslandManager().unloadIsland(getIsland(), null);
			this.handle = null;
		}
	}

	/**
	 * Sets the player of the Island
	 */
	public void setPlayer(OfflinePlayer player) {
		this.player = player;
	}

	/**
	 * @return Implementation for the Island
	 */
	public me.goodandevil.skyblock.island.Island getIsland() {
		return handle;
	}
}
