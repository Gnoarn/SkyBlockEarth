package me.goodandevil.skyblock.menus;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.levelling.LevellingManager;
import me.goodandevil.skyblock.playerdata.PlayerData;
import me.goodandevil.skyblock.playerdata.PlayerDataManager;
import me.goodandevil.skyblock.utils.NumberUtil;
import me.goodandevil.skyblock.utils.Skull;
import me.goodandevil.skyblock.utils.item.InventoryUtil;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.Sounds;

public class Levelling implements Listener {
	
    private static Levelling instance;

    public static Levelling getInstance(){
        if(instance == null) {
            instance = new Levelling();
        }
        
        return instance;
    }
	
    public void open(Player player) {
    	FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
    	PlayerData playerData = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager)).getPlayerData(player);
    	
    	Island island = ((IslandManager) Main.getInstance(Main.Instance.IslandManager)).getIsland(playerData.getOwner());
		HashMap<Material, Integer> islandMaterials = island.getLevelMaterials();
		
		int playerMenuPage = playerData.getPage(), nextEndIndex = islandMaterials.size() - playerMenuPage * 36;
    	
		Config languageConfig = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		FileConfiguration configLoad = languageConfig.getFileConfiguration();
		
		InventoryUtil inv = new InventoryUtil(configLoad.getString("Menu.Levelling.Title"), null, 6);
		inv.addItem(inv.createItem(Materials.OAK_FENCE_GATE.parseItem(), configLoad.getString("Menu.Levelling.Item.Exit.Displayname"), null, null, null, null), 0, 8);
		inv.addItem(inv.createItem(Materials.FIREWORK_STAR.parseItem(), configLoad.getString("Menu.Levelling.Item.Rescan.Displayname"), configLoad.getStringList("Menu.Levelling.Item.Rescan.Lore"), null, null, new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS }), 3, 5);
		inv.addItem(inv.createItem(new ItemStack(Material.PAINTING), configLoad.getString("Menu.Levelling.Item.Statistics.Displayname"), configLoad.getStringList("Menu.Levelling.Item.Statistics.Lore"), inv.createItemLoreVariable(new String[] { "%level_points#" + NumberUtil.getInstance().formatNumber(island.getLevelPoints()), "%level#" + NumberUtil.getInstance().formatNumber(island.getLevel()) }), null, null), 4);
		inv.addItem(inv.createItem(Materials.BLACK_STAINED_GLASS_PANE.parseItem(), configLoad.getString("Menu.Levelling.Item.Barrier.Displayname"), null, null, null, null), 9, 10, 11, 12, 13, 14, 15, 16, 17);
		
		if (playerMenuPage != 1) {
			inv.addItem(inv.createItem(Skull.getInstance().create("ToR1w9ZV7zpzCiLBhoaJH3uixs5mAlMhNz42oaRRvrG4HRua5hC6oyyOPfn2HKdSseYA9b1be14fjNRQbSJRvXF3mlvt5/zct4sm+cPVmX8K5kbM2vfwHJgCnfjtPkzT8sqqg6YFdT35mAZGqb9/xY/wDSNSu/S3k2WgmHrJKirszaBZrZfnVnqITUOgM9TmixhcJn2obeqICv6tl7/Wyk/1W62wXlXGm9+WjS+8rRNB+vYxqKR3XmH2lhAiyVGbADsjjGtBVUTWjq+aPw670SjXkoii0YE8sqzUlMMGEkXdXl9fvGtnWKk3APSseuTsjedr7yq+AkXFVDqqkqcUuXwmZl2EjC2WRRbhmYdbtY5nEfqh5+MiBrGdR/JqdEUL4yRutyRTw8mSUAI6X2oSVge7EdM/8f4HwLf33EO4pTocTqAkNbpt6Z54asLe5Y12jSXbvd2dFsgeJbrslK7e4uy/TK8CXf0BP3KLU20QELYrjz9I70gtj9lJ9xwjdx4/xJtxDtrxfC4Afmpu+GNYA/mifpyP3GDeBB5CqN7btIvEWyVvRNH7ppAqZIPqYJ7dSDd2RFuhAId5Yq98GUTBn+eRzeigBvSi1bFkkEgldfghOoK5WhsQtQbXuBBXITMME3NaWCN6zG7DxspS6ew/rZ8E809Xe0ArllquIZ0sP+k=", "eyJ0aW1lc3RhbXAiOjE0OTU3NTE5MTYwNjksInByb2ZpbGVJZCI6ImE2OGYwYjY0OGQxNDQwMDBhOTVmNGI5YmExNGY4ZGY5IiwicHJvZmlsZU5hbWUiOiJNSEZfQXJyb3dMZWZ0Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zZWJmOTA3NDk0YTkzNWU5NTViZmNhZGFiODFiZWFmYjkwZmI5YmU0OWM3MDI2YmE5N2Q3OThkNWYxYTIzIn19fQ=="), configLoad.getString("Menu.Levelling.Item.Previous.Displayname"), null, null, null, null), 1);
		}
		
		if (!(nextEndIndex == 0 || nextEndIndex < 0)) {
			inv.addItem(inv.createItem(Skull.getInstance().create("wZPrsmxckJn4/ybw/iXoMWgAe+1titw3hjhmf7bfg9vtOl0f/J6YLNMOI0OTvqeRKzSQVCxqNOij6k2iM32ZRInCQyblDIFmFadQxryEJDJJPVs7rXR6LRXlN8ON2VDGtboRTL7LwMGpzsrdPNt0oYDJLpR0huEeZKc1+g4W13Y4YM5FUgEs8HvMcg4aaGokSbvrYRRcEh3LR1lVmgxtbiUIr2gZkR3jnwdmZaIw/Ujw28+Et2pDMVCf96E5vC0aNY0KHTdMYheT6hwgw0VAZS2VnJg+Gz4JCl4eQmN2fs4dUBELIW2Rdnp4U1Eb+ZL8DvTV7ofBeZupknqPOyoKIjpInDml9BB2/EkD3zxFtW6AWocRphn03Z203navBkR6ztCMz0BgbmQU/m8VL/s8o4cxOn+2ppjrlj0p8AQxEsBdHozrBi8kNOGf1j97SDHxnvVAF3X8XDso+MthRx5pbEqpxmLyKKgFh25pJE7UaMSnzH2lc7aAZiax67MFw55pDtgfpl+Nlum4r7CK2w5Xob2QTCovVhu78/6SV7qM2Lhlwx/Sjqcl8rn5UIoyM49QE5Iyf1tk+xHXkIvY0m7q358oXsfca4eKmxMe6DFRjUDo1VuWxdg9iVjn22flqz1LD1FhGlPoqv0k4jX5Q733LwtPPI6VOTK+QzqrmiuR6e8=", "eyJ0aW1lc3RhbXAiOjE0OTM4NjgxMDA2NzMsInByb2ZpbGVJZCI6IjUwYzg1MTBiNWVhMDRkNjBiZTlhN2Q1NDJkNmNkMTU2IiwicHJvZmlsZU5hbWUiOiJNSEZfQXJyb3dSaWdodCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI2ZjFhMjViNmJjMTk5OTQ2NDcyYWVkYjM3MDUyMjU4NGZmNmY0ZTgzMjIxZTU5NDZiZDJlNDFiNWNhMTNiIn19fQ=="), configLoad.getString("Menu.Levelling.Item.Next.Displayname"), null, null, null, null), 7);
		}
		
		if (islandMaterials.size() == 0) {
			inv.addItem(inv.createItem(new ItemStack(Material.BARRIER), configLoad.getString("Menu.Levelling.Item.Nothing.Displayname"), null, null, null, null), 31);
		} else {
			int index = playerMenuPage * 36 - 36, endIndex = index >= islandMaterials.size() ? islandMaterials.size() - 1 : index + 36, inventorySlot = 17;
			
			Config config = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "levelling.yml"));
			
			for (; index < endIndex; index++) {
				if (islandMaterials.size() > index) {
					inventorySlot++;
					
					Material material = (Material) islandMaterials.keySet().toArray()[index];
					int points = islandMaterials.get(material);
					int amount = points/config.getFileConfiguration().getInt("Materials." + material.name() + ".Points");
					
					if (material == Material.REDSTONE_WIRE) {
						material = Material.REDSTONE;
					} else if (material == Materials.LEGACY_DOUBLE_SLAB.getPostMaterial()) {
						material = Materials.SMOOTH_STONE.parseMaterial();
					} else if (material == Materials.FERN.parseMaterial()) {
						material = Material.GRASS;
					} else if (material == Materials.LEGACY_NETHER_WARTS.getPostMaterial()) {
						material = Materials.LEGACY_NETHER_STALK.getPostMaterial();
					} else if (material == Material.REDSTONE_WIRE) {
						material = Material.REDSTONE;
					} else if (material == Materials.LEGACY_SIGN_POST.getPostMaterial() || material == Material.WALL_SIGN) {
						material = Material.SIGN;
					} else if (material == Materials.LEGACY_SUGAR_CANE_BLOCK.getPostMaterial()) {
						material = Material.SUGAR_CANE;
					} else if (material == Material.TRIPWIRE) {
						material = Material.TRIPWIRE_HOOK;
					} else if (material == Material.FLOWER_POT) {
						material = Materials.LEGACY_FLOWER_POT_ITEM.getPostMaterial();
					} else if (material == Materials.LEGACY_IRON_DOOR_BLOCK.getPostMaterial()) {
						material = Material.IRON_DOOR;
					} else if (material == Material.CAULDRON) {
						material = Materials.LEGACY_CAULDRON_ITEM.getPostMaterial();
					} else if (material == Material.BREWING_STAND) {
						material = Materials.LEGACY_BREWING_STAND.getPostMaterial();
					} else if (material.name().equals("BED_BLOCK")) {
						material = Materials.RED_BED.getPostMaterial();
					}
					
					inv.addItem(inv.createItem(new ItemStack(material, amount), configLoad.getString("Menu.Levelling.Item.Material.Displayname").replace("%points", NumberUtil.getInstance().formatNumber(points)).replace("%material", WordUtils.capitalize(material.name().toLowerCase().replace("_", " ").replace("item", "").replace("block", ""))), null, null, null, null), inventorySlot);
				}
			}
		}
		
		player.openInventory(inv.getInventory());
    }
    
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack is = event.getCurrentItem();

		if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
			Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			if (event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Title")))) {
				event.setCancelled(true);
				
				IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
				
				if (!islandManager.hasIsland(player)) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Level.Owner.Message")));
					player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
					player.closeInventory();
					
					return;
				}
				
				if ((event.getCurrentItem().getType() == Materials.BLACK_STAINED_GLASS_PANE.parseMaterial()) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Item.Barrier.Displayname"))))) {
		    		player.playSound(player.getLocation(), Sounds.GLASS.bukkitSound(), 1.0F, 1.0F);
		    	} else if ((event.getCurrentItem().getType() == Materials.OAK_FENCE_GATE.parseMaterial()) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Item.Exit.Displayname"))))) {
		    		player.playSound(player.getLocation(), Sounds.CHEST_CLOSE.bukkitSound(), 1.0F, 1.0F);
		    		player.closeInventory();
		    	} else if ((event.getCurrentItem().getType() == Material.PAINTING) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Item.Statistics.Displayname"))))) {
		    		player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
		    	} else if ((event.getCurrentItem().getType() == Material.BARRIER) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Item.Nothing.Displayname"))))) {
		    		player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
		    	} else if ((event.getCurrentItem().getType() == Materials.FIREWORK_STAR.parseMaterial()) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Item.Rescan.Displayname"))))) {
		    		LevellingManager levellingManager = ((LevellingManager) Main.getInstance(Main.Instance.LevellingManager));
		    		Island island = islandManager.getIsland(((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager)).getPlayerData(player).getOwner());
		    		
		    		if (levellingManager.hasLevelling(island.getOwnerUUID())) {
						me.goodandevil.skyblock.levelling.Levelling levelling = levellingManager.getLevelling(island.getOwnerUUID());
						long[] durationTime = NumberUtil.getInstance().getDuration(levelling.getTime());
						
						if (levelling.getTime() >= 3600) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Message").replace("%time", durationTime[1] + " " + config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Minute") + " " + durationTime[2] + " " + config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Minute") + " " + durationTime[3] + " " + config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Second"))));
						} else if (levelling.getTime() >= 60) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Message").replace("%time", durationTime[2] + " " + config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Minute") + " " + durationTime[3] + " " + config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Second"))));							
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Message").replace("%time", levelling.getTime() + " " + config.getFileConfiguration().getString("Command.Island.Level.Cooldown.Word.Second"))));
						}
						
						player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
						
						return;
					}
		    		
		    		player.closeInventory();
		    		
		    		new BukkitRunnable() {
						public void run() {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getFileConfiguration().getString("Command.Island.Level.Processing.Message")));
							player.playSound(player.getLocation(), Sounds.VILLAGER_YES.bukkitSound(), 1.0F, 1.0F);
							
							levellingManager.createLevelling(island.getOwnerUUID());
							levellingManager.loadLevelling(island.getOwnerUUID());
							levellingManager.calculatePoints(player, island);
						}
					}.runTaskAsynchronously(Main.getInstance());
		    	} else if ((event.getCurrentItem().getType() == Materials.LEGACY_SKULL_ITEM.getPostMaterial()) && (is.hasItemMeta())) {
		    		PlayerData playerData = ((PlayerDataManager) Main.getInstance(Main.Instance.PlayerDataManager)).getPlayerData(player);
		    		
		    		if (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Item.Previous.Displayname")))) {
		    			playerData.setPage(playerData.getPage() - 1);
		    			open(player);
		    			player.playSound(player.getLocation(), Sounds.ARROW_HIT.bukkitSound(), 1.0F, 1.0F);
		    		} else if (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Item.Next.Displayname")))) {
		    			playerData.setPage(playerData.getPage() + 1);
		    			open(player);
		    			player.playSound(player.getLocation(), Sounds.ARROW_HIT.bukkitSound(), 1.0F, 1.0F);
		    		} else {
		    			player.playSound(player.getLocation(), Sounds.CHICKEN_EGG_POP.bukkitSound(), 1.0F, 1.0F);
		    		}
		    	} else {
		    		player.playSound(player.getLocation(), Sounds.CHICKEN_EGG_POP.bukkitSound(), 1.0F, 1.0F);
		    	}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		
		Config languageConfig = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		FileConfiguration configLoad = languageConfig.getFileConfiguration();
		
		if (event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Levelling.Title")))) {
			player.playSound(player.getLocation(), Sounds.CHEST_CLOSE.bukkitSound(), 1.0F, 1.0F);
		}
	}
}
