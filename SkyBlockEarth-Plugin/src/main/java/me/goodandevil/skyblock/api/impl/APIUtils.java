package me.goodandevil.skyblock.api.impl;

import me.goodandevil.skyblock.api.island.IslandEnvironment;
import me.goodandevil.skyblock.api.island.IslandMessage;
import me.goodandevil.skyblock.api.island.IslandRole;
import me.goodandevil.skyblock.api.island.IslandWorld;
import me.goodandevil.skyblock.island.Location.Environment;
import me.goodandevil.skyblock.island.Location.World;
import me.goodandevil.skyblock.island.Message;
import me.goodandevil.skyblock.island.Role;

public final class APIUtils {
	
	private APIUtils() { }
	
	public static World toImplementation(IslandWorld world) {
		switch (world) {
			case NETHER:
				return World.Nether;
			case OVERWORLD:
				return World.Normal;
		}
		
		return null;
	}
	
	public static Environment toImplementation(IslandEnvironment environment) {
		switch (environment) {
			case ISLAND:
				return Environment.Island;
			case MAIN:
				return Environment.Main;
			case VISITOR:
				return Environment.Visitor;
		}
		
		return null;
	}
	
	public static Role toImplementation(IslandRole role) {
		switch (role) {
			case MEMBER:
				return Role.Member;
			case OPERATOR:
				return Role.Operator;
			case OWNER:
				return Role.Owner;
			case VISITOR:
				return Role.Visitor;
		}
		
		return null;
	}
	
	public static Message toImplementation(IslandMessage message) {
		switch (message) {
			case SIGN:
				return Message.Sign;
			case SIGNATURE:
				return Message.Signature;
			case WELCOME:
				return Message.Welcome;
		}
		
		return null;
	}
	
	public static IslandRole fromImplementation(Role role) {
		switch (role) {
			case Member:
				return IslandRole.MEMBER;
			case Operator:
				return IslandRole.OPERATOR;
			case Owner:
				return IslandRole.OWNER;
			case Visitor:
				return IslandRole.VISITOR;
		}
		
		return null;
	}
	
}