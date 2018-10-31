package me.goodandevil.skyblock.menus;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.biome.BiomeManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLocation;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.island.IslandRole;
import me.goodandevil.skyblock.island.IslandSettings;
import me.goodandevil.skyblock.utils.NMSUtil;
import me.goodandevil.skyblock.utils.NumberUtil;
import me.goodandevil.skyblock.utils.item.InventoryUtil;
import me.goodandevil.skyblock.utils.version.Biomes;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.Sounds;
import me.goodandevil.skyblock.utils.world.LocationUtil;

public class Biome implements Listener {

    private static Biome instance;

    public static Biome getInstance(){
        if(instance == null) {
            instance = new Biome();
        }
        
        return instance;
    }
	
    public void open(Player player) {
    	Main plugin = Main.getInstance();
    	
		Island island = plugin.getIslandManager().getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
		String islandBiomeName = island.getBiomeName();
		int NMSVersion = NMSUtil.getVersionNumber();
		
		Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
		
		InventoryUtil inv = new InventoryUtil(configLoad.getString("Menu.Biome.Title"), null, 1);
		inv.addItem(inv.createItem(new ItemStack(Material.NAME_TAG, 1), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Info.Displayname")), configLoad.getStringList("Menu.Biome.Item.Info.Lore"), inv.createItemLoreVariable(new String[] { "%biome_type#" + islandBiomeName }), null, null), 0);
		inv.addItem(inv.createItem(Materials.BLACK_STAINED_GLASS_PANE.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Barrier.Displayname")), null, null, null, null), 1);
		
		if (islandBiomeName.equals("Plains")) {
			inv.addItem(inv.createItem(Materials.SUNFLOWER.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Current.Displayname").replace("%biome_type", islandBiomeName)), configLoad.getStringList("Menu.Biome.Item.Biome.Current.Lore"), null, new Enchantment[] { Enchantment.THORNS }, new ItemFlag[] { ItemFlag.HIDE_ENCHANTS }), 2);
		} else {
			inv.addItem(inv.createItem(Materials.SUNFLOWER.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Plains")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 2);
		}
		
		if (islandBiomeName.equals("Forest")) {
			inv.addItem(inv.createItem(Materials.FERN.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Current.Displayname").replace("%biome_type", islandBiomeName)), configLoad.getStringList("Menu.Biome.Item.Biome.Current.Lore"), null, new Enchantment[] { Enchantment.THORNS }, new ItemFlag[] { ItemFlag.HIDE_ENCHANTS }), 3);
		} else {
			inv.addItem(inv.createItem(Materials.FERN.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Forest")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 3);
		}
		
		if (islandBiomeName.equals("Swampland") || islandBiomeName.equals("Swamp")) {
			inv.addItem(inv.createItem(Materials.LILY_PAD.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Current.Displayname").replace("%biome_type", islandBiomeName)), configLoad.getStringList("Menu.Biome.Item.Biome.Current.Lore"), null, new Enchantment[] { Enchantment.THORNS }, new ItemFlag[] { ItemFlag.HIDE_ENCHANTS }), 4);
		} else {
			if (NMSVersion < 13) {
				inv.addItem(inv.createItem(Materials.LILY_PAD.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Swampland")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 4);
			} else {
				inv.addItem(inv.createItem(Materials.LILY_PAD.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Swamp")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 4);
			}
		}
		
		if (islandBiomeName.equals("Desert")) {
			inv.addItem(inv.createItem(new ItemStack(Material.DEAD_BUSH, 1), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Current.Displayname").replace("%biome_type", islandBiomeName)), configLoad.getStringList("Menu.Biome.Item.Biome.Current.Lore"), null, new Enchantment[] { Enchantment.THORNS }, new ItemFlag[] { ItemFlag.HIDE_ENCHANTS }), 5);
		} else {
			inv.addItem(inv.createItem(new ItemStack(Material.DEAD_BUSH, 1), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Desert")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 5);
		}
		
		if (islandBiomeName.equals("Cold Beach") || islandBiomeName.equals("Snowy Beach")) {
			inv.addItem(inv.createItem(Materials.SNOWBALL.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Current.Displayname").replace("%biome_type", islandBiomeName)), configLoad.getStringList("Menu.Biome.Item.Biome.Current.Lore"), null, new Enchantment[] { Enchantment.THORNS }, new ItemFlag[] { ItemFlag.HIDE_ENCHANTS }), 6);
		} else {
			if (NMSVersion < 13) {
				inv.addItem(inv.createItem(Materials.SNOWBALL.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Cold Beach")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 6);
			} else {
				inv.addItem(inv.createItem(Materials.SNOWBALL.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Snowy Beach")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 6);
			}
		}
		
		if (islandBiomeName.equals("Jungle")) {
			inv.addItem(inv.createItem(new ItemStack(Material.VINE, 1), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Current.Displayname").replace("%biome_type", islandBiomeName)), configLoad.getStringList("Menu.Biome.Item.Biome.Current.Lore"), null, new Enchantment[] { Enchantment.THORNS }, new ItemFlag[] { ItemFlag.HIDE_ENCHANTS }), 7);
		} else {
			inv.addItem(inv.createItem(new ItemStack(Material.VINE, 1), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Jungle")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 7);
		}
		
		if (islandBiomeName.equals("Roofed Forest") || islandBiomeName.equals("Dark Forest")) {
			inv.addItem(inv.createItem(Materials.DARK_OAK_SAPLING.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Current.Displayname").replace("%biome_type", islandBiomeName)), configLoad.getStringList("Menu.Biome.Item.Biome.Current.Lore"), null, new Enchantment[] { Enchantment.THORNS }, new ItemFlag[] { ItemFlag.HIDE_ENCHANTS }), 8);
		} else {
			if (NMSVersion < 13) {
				inv.addItem(inv.createItem(Materials.DARK_OAK_SAPLING.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Roofed Forest")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 8);
			} else {
				inv.addItem(inv.createItem(Materials.DARK_OAK_SAPLING.parseItem(), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Biome.Select.Displayname").replace("%biome_type", "Dark Forest")), configLoad.getStringList("Menu.Biome.Item.Biome.Select.Lore"), null, null, null), 8);
			}
		}
		
		player.openInventory(inv.getInventory());
    }
    
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack is = event.getCurrentItem();

		if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
			Main plugin = Main.getInstance();
			
			Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			if (event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Title")))) {
				event.setCancelled(true);
				
				BiomeManager biomeManager = plugin.getBiomeManager();
				IslandManager islandManager = plugin.getIslandManager();
				Island island = null;
				
				if (islandManager.hasIsland(player)) {
					island = islandManager.getIsland(plugin.getPlayerDataManager().getPlayerData(player).getOwner());
					
					if (!((island.isRole(IslandRole.Operator, player.getUniqueId()) && island.getSetting(IslandSettings.Role.Operator, "Biome").getStatus()) || island.isRole(IslandRole.Owner, player.getUniqueId()))) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Biome.Permission.Message")));
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
						player.closeInventory();
						
						return;
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Biome.Owner.Message")));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					player.closeInventory();
					
					return;
				}
				
		    	if ((event.getCurrentItem().getType() == Material.NAME_TAG) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Info.Displayname"))))) {
		    		player.playSound(player.getLocation(), Sounds.CHICKEN_EGG_POP.bukkitSound(), 1.0F, 1.0F);
		    	} else if ((event.getCurrentItem().getType() == Materials.BLACK_STAINED_GLASS_PANE.parseMaterial()) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Item.Barrier.Displayname"))))) {
		    		player.playSound(player.getLocation(), Sounds.GLASS.bukkitSound(), 1.0F, 1.0F);
		    	} else {
		    		if (is.getItemMeta().hasEnchant(Enchantment.THORNS)) {
		    			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		    		} else {
						if (biomeManager.hasBiome(player)) {
							me.goodandevil.skyblock.biome.Biome biome = biomeManager.getBiome(player);
							
							if (biome.getTime() < 60) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Biome.Cooldown.Message").replace("%time", biome.getTime() + " " + config.getFileConfiguration().getString("Command.Island.Biome.Cooldown.Word.Second"))));
							} else {
								long[] durationTime = NumberUtil.getDuration(biome.getTime());
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Biome.Cooldown.Message").replace("%time", durationTime[2] + " " + config.getFileConfiguration().getString("Command.Island.Biome.Cooldown.Word.Minute") + " " + durationTime[3] + " " + config.getFileConfiguration().getString("Command.Island.Biome.Cooldown.Word.Second"))));
							}
							
							player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
							
							return;
						}
		    			
		    			org.bukkit.block.Biome selectedBiomeType = null;
		    			
		    			if (is.getType() == Materials.SUNFLOWER.parseMaterial()) {
		    				selectedBiomeType = org.bukkit.block.Biome.PLAINS;
		    			} else if (is.getType() == Materials.FERN.parseMaterial()) {
		    				selectedBiomeType = org.bukkit.block.Biome.FOREST;
		    			} else if (is.getType() == Materials.LILY_PAD.parseMaterial()) {
		    				selectedBiomeType = Biomes.SWAMPLAMD.bukkitBiome();
		    			} else if (is.getType() == Material.DEAD_BUSH) {
		    				selectedBiomeType = org.bukkit.block.Biome.DESERT;
		    			} else if (is.getType() == Materials.SNOWBALL.parseMaterial()) {
		    				selectedBiomeType = Biomes.COLD_BEACH.bukkitBiome();
		    			} else if (is.getType() == Material.VINE) {
		    				selectedBiomeType = org.bukkit.block.Biome.JUNGLE;
		    			} else if (is.getType() == Materials.DARK_OAK_SAPLING.parseMaterial()) {
		    				selectedBiomeType = Biomes.ROOFED_FOREST.bukkitBiome();
		    			}
		    			
		    			biomeManager.createBiome(player, plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "config.yml")).getFileConfiguration().getInt("Island.Biome.Cooldown"));
			    		biomeManager.setBiome(player, island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island), selectedBiomeType);
			    		
			    		island.setBiome(selectedBiomeType);
			    		island.save();
			    		island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island).getWorld().playSound(island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island), Sounds.SPLASH.bukkitSound(), 1.0F, 1.0F);
			    		
		    			open(player);
		    			
		    			if (!LocationUtil.isLocationAtLocationRadius(player.getLocation(), island.getLocation(IslandLocation.World.Normal, IslandLocation.Environment.Island), 85)) {
		    				player.playSound(player.getLocation(), Sounds.SPLASH.bukkitSound(), 1.0F, 1.0F);
		    			}
		    		}
		    	}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		
		Main plugin = Main.getInstance();
		
		Config languageConfig = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
		FileConfiguration configLoad = languageConfig.getFileConfiguration();
		
		if (event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Biome.Title")))) {
			player.playSound(player.getLocation(), Sounds.CHEST_CLOSE.bukkitSound(), 1.0F, 1.0F);
		}
	}
}
