package me.goodandevil.skyblock.api.island;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import me.goodandevil.skyblock.api.structure.Structure;
import me.goodandevil.skyblock.api.utils.APIUtil;

public class IslandManager {

	private final me.goodandevil.skyblock.island.IslandManager islandManager;

	public IslandManager(me.goodandevil.skyblock.island.IslandManager islandManager) {
		this.islandManager = islandManager;
	}

	/**
	 * Updates the Island border for players occupying an Island
	 */
	public void updateBorder(Island island) {
		Preconditions.checkArgument(island != null, "Cannot update border to null island");

		this.islandManager.updateBorder(island.getIsland());
	}

	/**
	 * Gives Island ownership to a player of their Island
	 */
	public void giveOwnership(Island island, OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot give ownership to null island");
		Preconditions.checkArgument(player != null, "Cannot give ownership to null player");

		this.islandManager.giveOwnership(island.getIsland(), player);
	}

	/**
	 * @return The Visitors occupying an Island
	 */
	public Set<UUID> getVisitorsAtIsland(Island island) {
		Preconditions.checkArgument(island != null, "Cannot get visitors at island to null island");

		return this.islandManager.getVisitorsAtIsland(island.getIsland());
	}

	/**
	 * Makes a player a Visitor of an Island
	 */
	public void visitIsland(Player player, Island island) {
		Preconditions.checkArgument(player != null, "Cannot visit island to null player");
		Preconditions.checkArgument(island != null, "Cannot visit island to null island");

		this.islandManager.visitIsland(player, island.getIsland());
	}

	/**
	 * Closes an Island from Visitors
	 */
	public void closeIsland(Island island) {
		Preconditions.checkArgument(island != null, "Cannot closed island to null island");

		this.islandManager.closeIsland(island.getIsland());
	}

	/**
	 * Checks if a player has permission at an Island for a Setting
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean hasPermission(Player player, String setting) {
		Preconditions.checkArgument(player != null, "Cannot check permission to null player");

		return this.islandManager.hasPermission(player, setting);
	}

	/**
	 * Checks if a player has permission at a location of an Island for a Setting
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean hasPermission(Player player, Location location, String setting) {
		Preconditions.checkArgument(player != null, "Cannot check permission to null player");
		Preconditions.checkArgument(location != null, "Cannot check permission to null location");

		return this.islandManager.hasPermission(player, location, setting);
	}

	/**
	 * Checks the permission of a Setting for a Role at a Location
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean hasSetting(Location location, IslandRole role, String setting) {
		Preconditions.checkArgument(location != null, "Cannot check setting to null location");
		Preconditions.checkArgument(role != null, "Cannot check setting to null role");

		return this.islandManager.hasSetting(location, APIUtil.toImplementation(role), setting);
	}

	/**
	 * @return A Set of Members of an Island that are online
	 */
	public Set<UUID> getMembersOnline(Island island) {
		Preconditions.checkArgument(island != null, "Cannot get online members to null island");

		return this.islandManager.getMembersOnline(island.getIsland());
	}

	/**
	 * @return A List of Players at an Island
	 */
	public List<Player> getPlayersAtIsland(Island island) {
		Preconditions.checkArgument(island != null, "Cannot get players at island to null island");

		return this.islandManager.getPlayersAtIsland(island.getIsland());
	}

	/**
	 * @return A List of Players at an Island by IslandWorld
	 */
	public List<Player> getPlayersAtIsland(Island island, IslandWorld world) {
		Preconditions.checkArgument(island != null, "Cannot get players at island to null island");
		Preconditions.checkArgument(world != null, "Cannot get players at island to null world");

		return this.islandManager.getPlayersAtIsland(island.getIsland(), APIUtil.toImplementation(world));
	}

	/**
	 * Gives the Island Upgrades to a player
	 */
	public void giveUgrades(Player player, Island island) {
		Preconditions.checkArgument(player != null, "Cannot give upgrades to null player");
		Preconditions.checkArgument(island != null, "Cannot give upgrades to null island");

		this.islandManager.giveUpgrades(player, island.getIsland());
	}

	/**
	 * Gives Fly to a player if they have permission at an Island
	 */
	public void giveFly(Player player, Island island) {
		Preconditions.checkArgument(player != null, "Cannot give upgrades to null player");
		Preconditions.checkArgument(island != null, "Cannot give upgrades to null island");

		this.islandManager.giveFly(player, island.getIsland());
	}

	/**
	 * Removes the Island Upgrades from a player
	 */
	public void removeUpgrades(Player player) {
		Preconditions.checkArgument(player != null, "Cannot remove upgrades to null player");

		this.islandManager.removeUpgrades(player, false);
	}

	/**
	 * @return A Set of Cooped Players at an Island
	 */
	public Set<UUID> getCoopPlayersAtIsland(Island island) {
		Preconditions.checkArgument(island != null, "Cannot get coop players to null island");

		return this.islandManager.getCoopPlayersAtIsland(island.getIsland());
	}

	/**
	 * Removes Coop Players occupying an Island
	 */
	public void removeCoopPlayersAtIsland(Island island) {
		Preconditions.checkArgument(island != null, "Cannot remove coop players to null island");
		this.islandManager.removeCoopPlayers(island.getIsland(), null);
	}

	/**
	 * Creates an Island for a player from a Structure
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean createIsland(Player player, Structure structure) {
		Preconditions.checkArgument(player != null, "Cannot create island to null player");
		Preconditions.checkArgument(structure != null, "Cannot create island to null structure");

		if (!hasIsland(player)) {
			return islandManager.createIsland(player, (me.goodandevil.skyblock.structure.Structure) structure);
		}

		return false;
	}

	/**
	 * Deletes an Island permanently
	 */
	public void deleteIsland(Island island) {
		Preconditions.checkArgument(island != null, "Cannot delete island to null island");

		this.islandManager.deleteIsland(island.getIsland());
	}

	/**
	 * Check if a player is occupying an Island
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean isPlayerAtIsland(Island island, Player player) {
		Preconditions.checkArgument(island != null, "Cannot check to null island");
		Preconditions.checkArgument(player != null, "Cannot check to null player");
		return this.islandManager.isPlayerAtIsland(island.getIsland(), player);
	}

	/**
	 * Check if a player is occupying an Island by IslandWorld
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean isPlayerAtIsland(Island island, Player player, IslandWorld world) {
		Preconditions.checkArgument(island != null, "Cannot check to null island");
		Preconditions.checkArgument(player != null, "Cannot check to null player");
		Preconditions.checkArgument(world != null, "Cannot check to null world");

		return this.islandManager.isPlayerAtIsland(island.getIsland(), player, APIUtil.toImplementation(world));
	}

	/**
	 * Check if a location is at an Island
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean isLocationAtIsland(Island island, Location location) {
		Preconditions.checkArgument(island != null, "Cannot check to null island");
		Preconditions.checkArgument(location != null, "Cannot check to null location");

		return this.islandManager.isLocationAtIsland(island.getIsland(), location);
	}

	/**
	 * @return The Island at a location
	 */
	public Island getIslandAtLocation(Location location) {
		Preconditions.checkArgument(location != null, "Cannot get island to null location");

		me.goodandevil.skyblock.island.Island island = this.islandManager.getIslandAtLocation(location);

		if (island != null) {
			return island.getAPIWrapper();
		}

		return null;
	}

	/**
	 * Check if a location is at an Island by IslandWorld
	 * 
	 * @return true of conditions met, false otherwise
	 */
	public boolean isPlayerAtIsland(Island island, Location location, IslandWorld world) {
		Preconditions.checkArgument(island != null, "Cannot check to null island");
		Preconditions.checkArgument(location != null, "Cannot check to null location");
		Preconditions.checkArgument(world != null, "Cannot check to null world");

		return this.islandManager.isLocationAtIsland(island.getIsland(), location, APIUtil.toImplementation(world));
	}

	/**
	 * @return The Island the player is occupying
	 */
	public Island getIslandPlayerAt(Player player) {
		Preconditions.checkArgument(player != null, "Cannot get Island to null player");

		me.goodandevil.skyblock.island.Island island = this.islandManager.getIslandPlayerAt(player);

		if (island != null) {
			return island.getAPIWrapper();
		}

		return null;
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public boolean isPlayerAtAnIsland(Player player) {
		Preconditions.checkArgument(player != null, "Cannot check to null player");
		return this.islandManager.isPlayerAtAnIsland(player);
	}

	/**
	 * Resets an Island permanently
	 */
	public void resetIsland(Island island) {
		Preconditions.checkArgument(island != null, "Cannot reset island to null island");
		this.islandManager.resetIsland(island.getIsland());
	}

	/**
	 * @return true of conditions met, false otherwise
	 */
	public static boolean hasIsland(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot check island to null player");
		return new me.goodandevil.skyblock.utils.player.OfflinePlayer(player.getUniqueId()).getOwner() != null;
	}

	/**
	 * @return The Island of a player
	 */
	public Island getIsland(OfflinePlayer player) {
		Preconditions.checkArgument(player != null, "Cannot get island to null player");

		me.goodandevil.skyblock.island.Island island = this.islandManager.getIsland(player);

		if (island != null) {
			return island.getAPIWrapper();
		}

		return new Island(null, player);
	}

	/**
	 * @return A List of loaded Islands
	 */
	public List<Island> getIslands() {
		List<Island> islands = new ArrayList<>();

		for (int i = 0; i < this.islandManager.getIslands().size(); i++) {
			islands.add(this.islandManager.getIslands().get(this.islandManager.getIslands().keySet().toArray()[i])
					.getAPIWrapper());
		}

		return islands;
	}
}
