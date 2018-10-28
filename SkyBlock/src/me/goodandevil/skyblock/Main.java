package me.goodandevil.skyblock;

import org.bukkit.plugin.java.JavaPlugin;

import me.goodandevil.skyblock.ban.BanManager;
import me.goodandevil.skyblock.biome.BiomeManager;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.confirmation.ConfirmationTask;
import me.goodandevil.skyblock.invite.InviteManager;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.levelling.LevellingManager;
import me.goodandevil.skyblock.listeners.Block;
import me.goodandevil.skyblock.listeners.Bucket;
import me.goodandevil.skyblock.listeners.Chat;
import me.goodandevil.skyblock.listeners.Death;
import me.goodandevil.skyblock.listeners.Entity;
import me.goodandevil.skyblock.listeners.Interact;
import me.goodandevil.skyblock.listeners.Inventory;
import me.goodandevil.skyblock.listeners.Item;
import me.goodandevil.skyblock.listeners.Join;
import me.goodandevil.skyblock.listeners.Move;
import me.goodandevil.skyblock.listeners.Portal;
import me.goodandevil.skyblock.listeners.Projectile;
import me.goodandevil.skyblock.listeners.Quit;
import me.goodandevil.skyblock.listeners.Respawn;
import me.goodandevil.skyblock.listeners.Teleport;
import me.goodandevil.skyblock.menus.Bans;
import me.goodandevil.skyblock.menus.Biome;
import me.goodandevil.skyblock.menus.ControlPanel;
import me.goodandevil.skyblock.menus.Creator;
import me.goodandevil.skyblock.menus.Levelling;
import me.goodandevil.skyblock.menus.Members;
import me.goodandevil.skyblock.menus.Ownership;
import me.goodandevil.skyblock.menus.Rollback;
import me.goodandevil.skyblock.menus.Settings;
import me.goodandevil.skyblock.menus.Visit;
import me.goodandevil.skyblock.menus.Visitors;
import me.goodandevil.skyblock.menus.Weather;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.playtime.PlaytimeTask;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;
import me.goodandevil.skyblock.structure.StructureManager;
import me.goodandevil.skyblock.visit.VisitManager;
import me.goodandevil.skyblock.visit.VisitTask;
import me.goodandevil.skyblock.world.WorldManager;

public class Main extends JavaPlugin {

	private static Main instance;
	
	private FileManager fileManager;
	private WorldManager worldManager;
	private VisitManager visitManager;
	private BanManager banManager;
	private IslandManager islandManager;
	private PlayerDataManager playerDataManager;
	private ScoreboardManager scoreboardManager;
	private InviteManager inviteManager;
	private BiomeManager biomeManager;
	private LevellingManager levellingManager;
	private CommandManager commandManager;
	private StructureManager structureManager;
	
	public void onEnable() {
		instance = this;
		
		fileManager = new FileManager();
		worldManager = new WorldManager();
		visitManager = new VisitManager();
		banManager = new BanManager();
		islandManager = new IslandManager();
		playerDataManager = new PlayerDataManager();
		scoreboardManager = new ScoreboardManager();
		inviteManager = new InviteManager();
		biomeManager = new BiomeManager();
		levellingManager = new LevellingManager();
		commandManager = new CommandManager();
		structureManager = new StructureManager();
		
		new PlaytimeTask().runTaskTimerAsynchronously(this, 0L, 20L);
		new VisitTask().runTaskTimerAsynchronously(this, 0L, 20L);
		new ConfirmationTask().runTaskTimerAsynchronously(this, 0L, 20L);
		
		//getCommand("island").setExecutor(new Island());
		
		getServer().getPluginManager().registerEvents(new Join(), this);
		getServer().getPluginManager().registerEvents(new Quit(), this);
		getServer().getPluginManager().registerEvents(new Block(), this);
		getServer().getPluginManager().registerEvents(new Interact(), this);
		getServer().getPluginManager().registerEvents(new Entity(), this);
		getServer().getPluginManager().registerEvents(new Bucket(), this);
		getServer().getPluginManager().registerEvents(new Projectile(), this);
		getServer().getPluginManager().registerEvents(new Inventory(), this);
		getServer().getPluginManager().registerEvents(new Item(), this);
		getServer().getPluginManager().registerEvents(new Teleport(), this);
		getServer().getPluginManager().registerEvents(new Portal(), this);
		getServer().getPluginManager().registerEvents(new Move(), this);
		getServer().getPluginManager().registerEvents(new Death(), this);
		getServer().getPluginManager().registerEvents(new Respawn(), this);
		getServer().getPluginManager().registerEvents(new Chat(), this);
		
		getServer().getPluginManager().registerEvents(new Biome(), this);
		getServer().getPluginManager().registerEvents(new Weather(), this);
		getServer().getPluginManager().registerEvents(new Rollback(), this);
		getServer().getPluginManager().registerEvents(new Levelling(), this);
		getServer().getPluginManager().registerEvents(new Settings(), this);
		getServer().getPluginManager().registerEvents(new Members(), this);
		getServer().getPluginManager().registerEvents(new Ownership(), this);
		getServer().getPluginManager().registerEvents(new Visit(), this);
		getServer().getPluginManager().registerEvents(new Visitors(), this);
		getServer().getPluginManager().registerEvents(new Bans(), this);
		getServer().getPluginManager().registerEvents(new ControlPanel(), this);
		getServer().getPluginManager().registerEvents(new Creator(), this);
	}
	
	public void onDisable() {
		((LevellingManager) getInstance(Main.Instance.LevellingManager)).onDisable();
		((IslandManager) getInstance(Main.Instance.IslandManager)).onDisable();
		((VisitManager) getInstance(Main.Instance.VisitManager)).onDisable();
		((BanManager) getInstance(Main.Instance.BanManager)).onDisable();
		((BiomeManager) getInstance(Main.Instance.BiomeManager)).onDisable();
		((PlayerDataManager) getInstance(Main.Instance.PlayerDataManager)).onDisable();
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static Object getInstance(Main.Instance instanceType) {
		if (instanceType == Main.Instance.Main) {
			return getInstance();
		} else if (instanceType == Main.Instance.FileManager) {
			return getInstance().fileManager;
		} else if (instanceType == Main.Instance.WorldManager) {
			return getInstance().worldManager;
		} else if (instanceType == Main.Instance.VisitManager) {
			return getInstance().visitManager;
		} else if (instanceType == Main.Instance.BanManager) {
			return getInstance().banManager;
		} else if (instanceType == Main.Instance.IslandManager) {
			return getInstance().islandManager;
		} else if (instanceType == Main.Instance.PlayerDataManager) {
			return getInstance().playerDataManager;
		} else if (instanceType == Main.Instance.ScoreboardManager) {
			return getInstance().scoreboardManager;
		} else if (instanceType == Main.Instance.InviteManager) {
			return getInstance().inviteManager;
		} else if (instanceType == Main.Instance.BiomeManager) {
			return getInstance().biomeManager;
		} else if (instanceType == Main.Instance.LevellingManager) {
			return getInstance().levellingManager;
		} else if (instanceType == Main.Instance.CommandManager) {
			return getInstance().commandManager;
		} else if (instanceType == Main.Instance.StructureManager) {
			return getInstance().structureManager;
		}
		
		return null;
	}
	
	public enum Instance {
		
		Main,
		FileManager,
		WorldManager,
		VisitManager,
		BanManager,
		IslandManager,
		PlayerDataManager,
		ScoreboardManager,
		InviteManager,
		BiomeManager,
		LevellingManager,
		CommandManager,
		StructureManager;
		
	}
}
