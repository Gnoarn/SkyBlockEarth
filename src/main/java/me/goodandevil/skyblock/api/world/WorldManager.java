package me.goodandevil.skyblock.api.world;

import org.bukkit.World;

import com.google.common.base.Preconditions;

import me.goodandevil.skyblock.api.island.IslandWorld;
import me.goodandevil.skyblock.api.utils.APIUtil;

public class WorldManager {

	private final me.goodandevil.skyblock.world.WorldManager worldManager;

	public WorldManager(me.goodandevil.skyblock.world.WorldManager worldManager) {
		this.worldManager = worldManager;
	}

	public World getWorld(IslandWorld world) {
		Preconditions.checkArgument(world != null, "Cannot get world to null world");

		return this.worldManager.getWorld(APIUtil.toImplementation(world));
	}

	public IslandWorld getIslandWorld(World world) {
		Preconditions.checkArgument(world != null, "Cannot get world to null world");

		return APIUtil.fromImplementation(this.worldManager.getIslandWorld(world));
	}

	public boolean isIslandWorld(World world) {
		Preconditions.checkArgument(world != null, "Cannot check world to null world");

		return this.worldManager.isIslandWorld(world);
	}
}
