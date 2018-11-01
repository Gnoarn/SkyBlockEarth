package me.goodandevil.skyblock.api.island;

import java.util.Objects;

public final class IslandSetting<T> {
	
	public static final IslandSetting<Boolean> DESTROY = new IslandSetting<>(Boolean.class, "Destroy");
	public static final IslandSetting<Boolean> PLACE = new IslandSetting<>(Boolean.class, "Place");
	public static final IslandSetting<Boolean> ANVIL = new IslandSetting<>(Boolean.class, "Anvil");
	public static final IslandSetting<Boolean> ARMOR_STAND = new IslandSetting<>(Boolean.class, "ArmorStand");
	public static final IslandSetting<Boolean> BEACON = new IslandSetting<>(Boolean.class, "Beacon");
	public static final IslandSetting<Boolean> BED = new IslandSetting<>(Boolean.class, "Bed");
	public static final IslandSetting<Boolean> ANIMAL_BREEDING = new IslandSetting<>(Boolean.class, "AnimalBreeding");
	public static final IslandSetting<Boolean> BREWING = new IslandSetting<>(Boolean.class, "Brewing");
	public static final IslandSetting<Boolean> BUCKET = new IslandSetting<>(Boolean.class, "Bucket");
	public static final IslandSetting<Boolean> WATER_COLLECTION = new IslandSetting<>(Boolean.class, "WaterCollection");
	public static final IslandSetting<Boolean> STORAGE = new IslandSetting<>(Boolean.class, "Storage");
	public static final IslandSetting<Boolean> WORKBENCH = new IslandSetting<>(Boolean.class, "Workbench");
	public static final IslandSetting<Boolean> CROP = new IslandSetting<>(Boolean.class, "Crop");
	public static final IslandSetting<Boolean> DOOR = new IslandSetting<>(Boolean.class, "Door");
	public static final IslandSetting<Boolean> GATE = new IslandSetting<>(Boolean.class, "Gate");
	public static final IslandSetting<Boolean> PROJECTILE = new IslandSetting<>(Boolean.class, "Projectile");
	public static final IslandSetting<Boolean> ENCHANT = new IslandSetting<>(Boolean.class, "Enchant");
	public static final IslandSetting<Boolean> FIRE = new IslandSetting<>(Boolean.class, "Fire");
	public static final IslandSetting<Boolean> FURNACE = new IslandSetting<>(Boolean.class, "Furnace");
	public static final IslandSetting<Boolean> HORSE_INVENTORY = new IslandSetting<>(Boolean.class, "HorseInventory");
	public static final IslandSetting<Boolean> MOB_RIDING = new IslandSetting<>(Boolean.class, "MobRiding");
	public static final IslandSetting<Boolean> MOB_HURTING = new IslandSetting<>(Boolean.class, "MobHurting");
	public static final IslandSetting<Boolean> MOB_TAMING = new IslandSetting<>(Boolean.class, "MobTaming");
	public static final IslandSetting<Boolean> LEASH = new IslandSetting<>(Boolean.class, "Leash");
	public static final IslandSetting<Boolean> LEVER_BUTTON = new IslandSetting<>(Boolean.class, "LeverButton");
	public static final IslandSetting<Boolean> MILKING = new IslandSetting<>(Boolean.class, "Milking");
	public static final IslandSetting<Boolean> JUKEBOX = new IslandSetting<>(Boolean.class, "Jukebox");
	public static final IslandSetting<Boolean> PRESSURE_PLATE = new IslandSetting<>(Boolean.class, "PressurePlate");
	public static final IslandSetting<Boolean> REDSTONE = new IslandSetting<>(Boolean.class, "Redstone");
	public static final IslandSetting<Boolean> SHEARING = new IslandSetting<>(Boolean.class, "Shearing");
	public static final IslandSetting<Boolean> TRADING = new IslandSetting<>(Boolean.class, "Trading");
	public static final IslandSetting<Boolean> ITEM_DROP = new IslandSetting<>(Boolean.class, "ItemDrop");
	public static final IslandSetting<Boolean> ITEM_PICKUP = new IslandSetting<>(Boolean.class, "ItemPickup");
	public static final IslandSetting<Boolean> FISHING = new IslandSetting<>(Boolean.class, "Fishing");
	public static final IslandSetting<Boolean> DROPPER_DISPENSER = new IslandSetting<>(Boolean.class, "DropperDispenser");
	public static final IslandSetting<Boolean> SPAWN_EGG = new IslandSetting<>(Boolean.class, "SpawnEgg");
	public static final IslandSetting<Boolean> CAKE = new IslandSetting<>(Boolean.class, "Cake");
	public static final IslandSetting<Boolean> MINECART_BOAT = new IslandSetting<>(Boolean.class, "MinecartBoat");
	public static final IslandSetting<Boolean> PORTAL = new IslandSetting<>(Boolean.class, "Portal");
	public static final IslandSetting<Boolean> HOPPER = new IslandSetting<>(Boolean.class, "Hopper");
	
	public static final IslandSetting<Boolean> INVITE = new IslandSetting<>(Boolean.class, "Invite");
	public static final IslandSetting<Boolean> KICK = new IslandSetting<>(Boolean.class, "Kick");
	public static final IslandSetting<Boolean> BAN = new IslandSetting<>(Boolean.class, "Ban");
	public static final IslandSetting<Boolean> UNBAN = new IslandSetting<>(Boolean.class, "Unban");
	public static final IslandSetting<Boolean> VISITOR = new IslandSetting<>(Boolean.class, "Visitor");
	public static final IslandSetting<Boolean> MEMBER = new IslandSetting<>(Boolean.class, "Member");
	public static final IslandSetting<Boolean> ISLAND = new IslandSetting<>(Boolean.class, "Island");
	public static final IslandSetting<Boolean> MAIN_SPAWN = new IslandSetting<>(Boolean.class, "MainSpawn");
	public static final IslandSetting<Boolean> VISITOR_SPAWN = new IslandSetting<>(Boolean.class, "VisitorSpawn");
	public static final IslandSetting<Boolean> BIOME = new IslandSetting<>(Boolean.class, "Biome");
	public static final IslandSetting<Boolean> WEATHER = new IslandSetting<>(Boolean.class, "Weather");

	public static final IslandSetting<Boolean> NATURAL_MOB_SPAWNING = new IslandSetting<>(Boolean.class, "NaturalMobSpawning");
	public static final IslandSetting<Boolean> MOB_GRIEFING = new IslandSetting<>(Boolean.class, "MobGriefing");
	public static final IslandSetting<Boolean> PVP = new IslandSetting<>(Boolean.class, "PvP");
	public static final IslandSetting<Boolean> EXPLOSIONS = new IslandSetting<>(Boolean.class, "Explosions");
	public static final IslandSetting<Boolean> FIRE_SPREAD = new IslandSetting<>(Boolean.class, "FireSpread");
	public static final IslandSetting<Boolean> LEAF_DECAY = new IslandSetting<>(Boolean.class, "LeafDecay");
	public static final IslandSetting<Boolean> KEEP_ITEMS_ON_DEATH = new IslandSetting<>(Boolean.class, "KeepItemsOnDeath");
	
	
	private final Class<T> type;
	private final String name;
	
	private IslandSetting(Class<T> type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Class<T> getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(type, name);
	}
	
}