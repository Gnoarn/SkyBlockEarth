package me.goodandevil.skyblock.menus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.goodandevil.skyblock.Main;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.config.FileManager.Config;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.structure.Structure;
import me.goodandevil.skyblock.structure.StructureManager;
import me.goodandevil.skyblock.utils.item.InventoryUtil;
import me.goodandevil.skyblock.utils.version.Sounds;

public class Creator implements Listener {

    private static Creator instance;

    public static Creator getInstance(){
        if(instance == null) {
            instance = new Creator();
        }
        
        return instance;
    }
    
    @SuppressWarnings("deprecation")
	public void openSelector(Player player) {
		Config config = ((FileManager) Main.getInstance(Main.Instance.FileManager)).getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
		FileConfiguration configLoad = config.getFileConfiguration();
    	
		List<Structure> availableStructures = new ArrayList<Structure>();
		
		for (Structure structureList : ((StructureManager) Main.getInstance(Main.Instance.StructureManager)).getStructures()) {
			if (structureList.getPermission() != null) {
				if (!(structureList.getPermission().isEmpty() && player.hasPermission(structureList.getPermission()))) {
					continue;
				}
			}
			
			availableStructures.add(structureList);
		}
		
		int inventoryRows = 0;
		
		if (availableStructures.size() == 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Creator.Selector.None.Message")));
			player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
			
			return;
		} else if (availableStructures.size() <= 9) {
			inventoryRows = 1;
		} else if (availableStructures.size() <= 18) {
			inventoryRows = 2;
		} else if (availableStructures.size() <= 27) {
			inventoryRows = 3;
		} else if (availableStructures.size() <= 36) {
			inventoryRows = 4;
		} else if (availableStructures.size() <= 45) {
			inventoryRows = 5;
		} else if (availableStructures.size() <= 54) {
			inventoryRows = 6;
		}
		
    	InventoryUtil inv = new InventoryUtil(configLoad.getString("Menu.Creator.Selector.Title"), null, inventoryRows);
    	
    	for (int i = 0; i < availableStructures.size(); i++) {
    		Structure structure = availableStructures.get(i);
    		List<String> itemLore = new ArrayList<String>();
    		
    		for (String itemLoreList : configLoad.getStringList("Menu.Creator.Selector.Item.Island.Lore")) {
    			if (itemLoreList.contains("%description")) {
    				for (String descriptionList : structure.getDescription()) {
    					itemLore.add(ChatColor.translateAlternateColorCodes('&', descriptionList));
    				}
    			} else {
    				itemLore.add(ChatColor.translateAlternateColorCodes('&', itemLoreList));
    			}
    		}
    		
    		inv.addItem(inv.createItem(new ItemStack(structure.getMaterial(), 1, (short) structure.getData()), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Creator.Selector.Item.Island.Displayname").replace("%displayname", structure.getDisplayname())), itemLore, null, null, null), i);
    	}
    	
    	player.openInventory(inv.getInventory());
    }
    
    public void openBrowse(Player player) {
    	
    }
    
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack is = event.getCurrentItem();

		if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
			FileManager fileManager = ((FileManager) Main.getInstance(Main.Instance.FileManager));
			
			Config config = fileManager.getConfig(new File(Main.getInstance().getDataFolder(), "language.yml"));
			FileConfiguration configLoad = config.getFileConfiguration();
			
			if (event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Creator.Selector.Title")))) {
				event.setCancelled(true);
				
				IslandManager islandManager = ((IslandManager) Main.getInstance(Main.Instance.IslandManager));
				
				if (islandManager.hasIsland(player)) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Command.Island.Create.Owner.Message")));
					player.playSound(player.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
					
					return;
				}
				
				for (Structure structureList : ((StructureManager) Main.getInstance(Main.Instance.StructureManager)).getStructures()) {
					if ((event.getCurrentItem().getType() == structureList.getMaterial()) && (is.hasItemMeta()) && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menu.Creator.Selector.Item.Island.Displayname").replace("%displayname", structureList.getDisplayname()))))) {
						if (structureList.getPermission() != null) {
							if (!(structureList.getPermission().isEmpty() && player.hasPermission(structureList.getPermission()))) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Creator.Selector.Permission.Message")));
								player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
								
								openSelector(player);
								
								return;
							}
						}
						
						if (!fileManager.isFileExist(new File(new File(Main.getInstance().getDataFolder().toString() + "/structures"), structureList.getFileName()))) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Creator.Selector.File.Message")));
							player.playSound(player.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);
							
							return;
						}
						
						islandManager.createIsland(player, structureList);
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Island.Creator.Selector.Created.Message")));
						player.playSound(player.getLocation(), Sounds.NOTE_PLING.bukkitSound(), 1.0F, 1.0F);
						
						player.closeInventory();
						
						return;
					}
				}
			}
		}
	}
}
