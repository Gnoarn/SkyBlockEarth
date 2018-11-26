package me.goodandevil.skyblock;

import java.io.File;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.goodandevil.skyblock.ban.BanManager;
import me.goodandevil.skyblock.biome.BiomeManager;
import me.goodandevil.skyblock.command.CommandManager;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.confirmation.ConfirmationTask;
import me.goodandevil.skyblock.creation.CreationManager;
import me.goodandevil.skyblock.generator.GeneratorManager;
import me.goodandevil.skyblock.invite.InviteManager;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.leaderboard.LeaderboardManager;
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
import me.goodandevil.skyblock.menus.Leaderboard;
import me.goodandevil.skyblock.menus.Levelling;
import me.goodandevil.skyblock.menus.Members;
import me.goodandevil.skyblock.menus.Ownership;
import me.goodandevil.skyblock.menus.Rollback;
import me.goodandevil.skyblock.menus.Settings;
import me.goodandevil.skyblock.menus.Visit;
import me.goodandevil.skyblock.menus.Visitors;
import me.goodandevil.skyblock.menus.Weather;
import me.goodandevil.skyblock.menus.admin.Generator;
import me.goodandevil.skyblock.message.MessageManager;
import me.goodandevil.skyblock.placeholder.PlaceholderManager;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.playtime.PlaytimeTask;
import me.goodandevil.skyblock.scoreboard.ScoreboardManager;
import me.goodandevil.skyblock.sound.SoundManager;
import me.goodandevil.skyblock.structure.StructureManager;
import me.goodandevil.skyblock.visit.VisitManager;
import me.goodandevil.skyblock.visit.VisitTask;
import me.goodandevil.skyblock.world.WorldManager;
import me.goodandevil.skyblock.world.generator.VoidGenerator;

public class SkyBlock extends JavaPlugin {

	private static SkyBlock instance;
	
	private FileManager fileManager;
	private WorldManager worldManager;
	private VisitManager visitManager;
	private BanManager banManager;
	private IslandManager islandManager;
	private CreationManager creationManager;
	private PlayerDataManager playerDataManager;
	private ScoreboardManager scoreboardManager;
	private InviteManager inviteManager;
	private BiomeManager biomeManager;
	private LevellingManager levellingManager;
	private CommandManager commandManager;
	private StructureManager structureManager;
	private SoundManager soundManager;
	private GeneratorManager generatorManager;
	private PlaceholderManager placeholderManager;
	private LeaderboardManager leaderboardManager;
	private MessageManager messageManager;
	
	@Override
	public void onEnable() {
		instance = this;
		
		fileManager = new FileManager(this);
		worldManager = new WorldManager(this);
		visitManager = new VisitManager(this);
		banManager = new BanManager(this);
		islandManager = new IslandManager(this);
		creationManager = new CreationManager(this);
		playerDataManager = new PlayerDataManager(this);
		
		if (fileManager.getConfig(new File(getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Scoreboard.Enable")) {
			scoreboardManager = new ScoreboardManager(this);
		}
		
		inviteManager = new InviteManager(this);
		biomeManager = new BiomeManager(this);
		levellingManager = new LevellingManager(this);
		commandManager = new CommandManager(this);
		structureManager = new StructureManager(this);
		soundManager = new SoundManager(this);
		
		if (fileManager.getConfig(new File(getDataFolder(), "config.yml")).getFileConfiguration().getBoolean("Island.Generator.Enable")) {
			generatorManager = new GeneratorManager(this);
		}
		
		placeholderManager = new PlaceholderManager(this);
		placeholderManager.registerPlaceholders();
		
		leaderboardManager = new LeaderboardManager(this);
		messageManager = new MessageManager(this);
		
		new PlaytimeTask(playerDataManager, islandManager).runTaskTimerAsynchronously(this, 0L, 20L);
		new VisitTask(playerDataManager).runTaskTimerAsynchronously(this, 0L, 20L);
		new ConfirmationTask(playerDataManager).runTaskTimerAsynchronously(this, 0L, 20L);
		
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new Join(this), this);
		pluginManager.registerEvents(new Quit(this), this);
		pluginManager.registerEvents(new Block(this), this);
		pluginManager.registerEvents(new Interact(this), this);
		pluginManager.registerEvents(new Entity(this), this);
		pluginManager.registerEvents(new Bucket(this), this);
		pluginManager.registerEvents(new Projectile(this), this);
		pluginManager.registerEvents(new Inventory(this), this);
		pluginManager.registerEvents(new Item(this), this);
		pluginManager.registerEvents(new Teleport(this), this);
		pluginManager.registerEvents(new Portal(this), this);
		pluginManager.registerEvents(new Move(this), this);
		pluginManager.registerEvents(new Death(this), this);
		pluginManager.registerEvents(new Respawn(this), this);
		pluginManager.registerEvents(new Chat(this), this);
		
		pluginManager.registerEvents(new Biome(), this);
		pluginManager.registerEvents(new Weather(), this);
		pluginManager.registerEvents(new Rollback(), this);
		pluginManager.registerEvents(new Levelling(), this);
		pluginManager.registerEvents(new Settings(), this);
		pluginManager.registerEvents(new Members(), this);
		pluginManager.registerEvents(new Ownership(), this);
		pluginManager.registerEvents(new Visit(), this);
		pluginManager.registerEvents(new Visitors(), this);
		pluginManager.registerEvents(new Bans(), this);
		pluginManager.registerEvents(new ControlPanel(), this);
		pluginManager.registerEvents(new Creator(), this);
		pluginManager.registerEvents(new Leaderboard(), this);
		
		pluginManager.registerEvents(new me.goodandevil.skyblock.menus.admin.Levelling(), this);
		pluginManager.registerEvents(new me.goodandevil.skyblock.menus.admin.Creator(), this);
		pluginManager.registerEvents(new me.goodandevil.skyblock.menus.admin.Settings(), this);
		pluginManager.registerEvents(new Generator(), this);
	}
	
	@Override
	public void onDisable() {
		if (this.levellingManager != null) {
			this.levellingManager.onDisable();
		}
		
		if (this.islandManager != null) {
			this.islandManager.onDisable();
		}
		
		if (this.visitManager != null) {
			this.visitManager.onDisable();
		}
		
		if (this.banManager != null) {
			this.banManager.onDisable();
		}
		
		if (this.biomeManager != null) {
			this.biomeManager.onDisable();
		}
		
		if (this.creationManager != null) {
			this.creationManager.onDisable();
		}
		
		if (this.playerDataManager != null) {
			this.playerDataManager.onDisable();
		}
	}
	
	public static SkyBlock getInstance() {
		return instance;
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	
	public WorldManager getWorldManager() {
		return worldManager;
	}
	
	public VisitManager getVisitManager() {
		return visitManager;
	}
	
	public BanManager getBanManager() {
		return banManager;
	}
	
	public IslandManager getIslandManager() {
		return islandManager;
	}
	
	public CreationManager getCreationManager() {
		return creationManager;
	}
	
	public PlayerDataManager getPlayerDataManager() {
		return playerDataManager;
	}
	
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}
	
	public InviteManager getInviteManager() {
		return inviteManager;
	}
	
	public BiomeManager getBiomeManager() {
		return biomeManager;
	}
	
	public LevellingManager getLevellingManager() {
		return levellingManager;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public StructureManager getStructureManager() {
		return structureManager;
	}
	
	public SoundManager getSoundManager() {
		return soundManager;
	}
	
	public GeneratorManager getGeneratorManager() {
		return generatorManager;
	}
	
	public PlaceholderManager getPlaceholderManager() {
		return placeholderManager;
	}
	
	public LeaderboardManager getLeaderboardManager() {
		return leaderboardManager;
	}
	
	public MessageManager getMessageManager() {
		return messageManager;
	}
	
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
    	return new VoidGenerator();
    }
}
