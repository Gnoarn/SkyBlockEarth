package me.goodandevil.skyblock.listeners;

import java.io.File;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.message.MessageManager;
import me.goodandevil.skyblock.sound.SoundManager;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.utils.item.InventoryUtil;
import me.goodandevil.skyblock.utils.structure.StructureUtil;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.NMSUtil;
import me.goodandevil.skyblock.utils.version.Sounds;

public class Interact implements Listener {

	private final SkyBlock skyblock;

	public Interact(SkyBlock skyblock) {
		this.skyblock = skyblock;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		org.bukkit.block.Block block = event.getClickedBlock();

		if (block != null && !skyblock.getWorldManager().isIslandWorld(block.getWorld())) {
			return;
		}

		MessageManager messageManager = skyblock.getMessageManager();
		IslandManager islandManager = skyblock.getIslandManager();
		SoundManager soundManager = skyblock.getSoundManager();

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (block.getType() == Material.ANVIL) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Anvil")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.BEACON) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Beacon")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Materials.LEGACY_BED_BLOCK.parseMaterial()
					|| block.getType() == Materials.WHITE_BED.parseMaterial()
					|| block.getType() == Materials.ORANGE_BED.parseMaterial()
					|| block.getType() == Materials.MAGENTA_BED.parseMaterial()
					|| block.getType() == Materials.LIGHT_BLUE_BED.parseMaterial()
					|| block.getType() == Materials.YELLOW_BED.parseMaterial()
					|| block.getType() == Materials.LIME_BED.parseMaterial()
					|| block.getType() == Materials.PINK_BED.parseMaterial()
					|| block.getType() == Materials.GRAY_BED.parseMaterial()
					|| block.getType() == Materials.LIGHT_GRAY_BED.parseMaterial()
					|| block.getType() == Materials.CYAN_BED.parseMaterial()
					|| block.getType() == Materials.CYAN_BED.parseMaterial()
					|| block.getType() == Materials.PURPLE_BED.parseMaterial()
					|| block.getType() == Materials.BLUE_BED.parseMaterial()
					|| block.getType() == Materials.BROWN_BED.parseMaterial()
					|| block.getType() == Materials.GREEN_BED.parseMaterial()
					|| block.getType() == Materials.RED_BED.parseMaterial()
					|| block.getType() == Materials.BLACK_BED.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Bed")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.BREWING_STAND) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Brewing")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST
					|| (NMSUtil.getVersionNumber() > 9
							&& (block.getType() == Materials.BLACK_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.BLUE_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.BROWN_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.CYAN_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.GRAY_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.GREEN_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.LIGHT_BLUE_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.LIGHT_GRAY_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.LIME_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.MAGENTA_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.ORANGE_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.PINK_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.PURPLE_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.RED_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.WHITE_SHULKER_BOX.parseMaterial()
									|| block.getType() == Materials.YELLOW_SHULKER_BOX.parseMaterial()))) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Storage")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Materials.CRAFTING_TABLE.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Workbench")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.BIRCH_DOOR || block.getType() == Material.ACACIA_DOOR
					|| block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.JUNGLE_DOOR
					|| block.getType() == Material.SPRUCE_DOOR
					|| block.getType() == Materials.LEGACY_WOODEN_DOOR.parseMaterial()
					|| block.getType() == Materials.OAK_DOOR.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Door")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Materials.ENCHANTING_TABLE.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Enchant")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.FURNACE
					|| block.getType() == Materials.LEGACY_BURNING_FURNACE.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Furnace")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.STONE_BUTTON
					|| block.getType() == Materials.OAK_BUTTON.parseMaterial()
					|| block.getType() == Materials.SPRUCE_BUTTON.parseMaterial()
					|| block.getType() == Materials.BIRCH_BUTTON.parseMaterial()
					|| block.getType() == Materials.JUNGLE_BUTTON.parseMaterial()
					|| block.getType() == Materials.ACACIA_BUTTON.parseMaterial()
					|| block.getType() == Materials.DARK_OAK_BUTTON.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "LeverButton")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.JUKEBOX) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Jukebox")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Materials.OAK_TRAPDOOR.parseMaterial()
					|| block.getType() == Materials.SPRUCE_TRAPDOOR.parseMaterial()
					|| block.getType() == Materials.BIRCH_TRAPDOOR.parseMaterial()
					|| block.getType() == Materials.JUNGLE_TRAPDOOR.parseMaterial()
					|| block.getType() == Materials.ACACIA_TRAPDOOR.parseMaterial()
					|| block.getType() == Materials.DARK_OAK_TRAPDOOR.parseMaterial()
					|| block.getType() == Material.NOTE_BLOCK || block.getType() == Material.HOPPER
					|| block.getType() == Materials.COMPARATOR.parseMaterial()
					|| block.getType() == Materials.LEGACY_REDSTONE_COMPARATOR_OFF.parseMaterial()
					|| block.getType() == Materials.LEGACY_REDSTONE_COMPARATOR_ON.parseMaterial()
					|| block.getType() == Materials.REPEATER.parseMaterial()
					|| block.getType() == Materials.LEGACY_DIODE_BLOCK_OFF.parseMaterial()
					|| block.getType() == Materials.LEGACY_DIODE_BLOCK_ON.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Redstone")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Materials.OAK_FENCE_GATE.parseMaterial()
					|| block.getType() == Material.ACACIA_FENCE_GATE || block.getType() == Material.BIRCH_FENCE_GATE
					|| block.getType() == Material.DARK_OAK_FENCE_GATE || block.getType() == Material.JUNGLE_FENCE_GATE
					|| block.getType() == Material.SPRUCE_FENCE_GATE) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Gate")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.DROPPER || block.getType() == Material.DISPENSER) {
				if (!islandManager.hasPermission(player, block.getLocation(), "DropperDispenser")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Materials.LEGACY_CAKE_BLOCK.getPostMaterial()) {
				if (player.getFoodLevel() < 20 && !islandManager.hasPermission(player, block.getLocation(), "Cake")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.DRAGON_EGG) {
				if (!islandManager.hasPermission(player, block.getLocation(), "DragonEggUse")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (block.getType() == Material.HOPPER) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Hopper")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if ((player.getGameMode() == GameMode.SURVIVAL) && (block.getType() == Material.OBSIDIAN)
					&& (event.getItem() != null) && (event.getItem().getType() != Material.AIR)
					&& (event.getItem().getType() == Material.BUCKET)) {
				if (skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"))
						.getFileConfiguration().getBoolean("Island.Block.Obsidian.Enable")
						&& islandManager.hasPermission(player, block.getLocation(), "Bucket")) {
					int NMSVersion = NMSUtil.getVersionNumber();
					boolean isInventoryFull = false;

					if (NMSVersion > 8) {
						isInventoryFull = InventoryUtil.isInventoryFull(player.getInventory(), 5, 1, Material.BUCKET);
					} else {
						isInventoryFull = InventoryUtil.isInventoryFull(player.getInventory(), 0, 1, Material.BUCKET);
					}

					soundManager.playSound(block.getLocation(), Sounds.FIZZ.bukkitSound(), 1.0F, 1.0F);

					InventoryUtil.removeItem(player.getInventory(), 1, false, Material.BUCKET);
					block.setType(Material.AIR);

					if (isInventoryFull) {
						player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.LAVA_BUCKET));
					} else {
						if (NMSVersion > 8) {
							isInventoryFull = InventoryUtil.isInventoryFull(player.getInventory(), 5, 1,
									Material.LAVA_BUCKET);
						} else {
							isInventoryFull = InventoryUtil.isInventoryFull(player.getInventory(), 0, 1,
									Material.LAVA_BUCKET);
						}

						if (isInventoryFull) {
							player.getWorld().dropItemNaturally(player.getLocation(),
									new ItemStack(Material.LAVA_BUCKET));
						} else {
							player.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
						}
					}

					event.setCancelled(true);

					return;
				}
			} else if (block.getType() == Materials.END_PORTAL_FRAME.parseMaterial()) {
				if (skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"))
						.getFileConfiguration().getBoolean("Island.Block.EndFrame.Enable")
						&& islandManager.hasPermission(player, block.getLocation(), "Destroy")) {
					ItemStack is = event.getItem();

					if (is == null || is.getType() == Material.AIR) {
						block.setType(Material.AIR);
						player.getInventory().setItemInHand(new ItemStack(Materials.END_PORTAL_FRAME.parseMaterial()));
						player.updateInventory();

						soundManager.playSound(player, Sounds.CHICKEN_EGG_POP.bukkitSound(), 10.0F, 10.0F);

						event.setCancelled(true);

						return;
					}
				}
			}

			if ((event.getItem() != null) && (event.getItem().getType() != Material.AIR)) {
				if (event.getItem().getType() == Material.BUCKET || event.getItem().getType() == Material.WATER_BUCKET
						|| event.getItem().getType() == Material.LAVA_BUCKET) {
					if (!islandManager.hasPermission(player, block.getLocation(), "Bucket")) {
						event.setCancelled(true);

						messageManager.sendMessage(player,
								skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
										.getFileConfiguration().getString("Island.Settings.Permission.Message"));
						soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

						player.updateInventory();
					}
				} else if (event.getItem().getType() == Material.GLASS_BOTTLE) {
					if (block.getType() == Material.WATER
							|| block.getType() == Materials.LEGACY_STATIONARY_WATER.getPostMaterial()
							|| block.getType() == Material.CAULDRON) {
						if (!islandManager.hasPermission(player, block.getLocation(), "WaterCollection")) {
							event.setCancelled(true);

							messageManager.sendMessage(player,
									skyblock.getFileManager()
											.getConfig(new File(skyblock.getDataFolder(), "language.yml"))
											.getFileConfiguration().getString("Island.Settings.Permission.Message"));
							soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

							player.updateInventory();
						}
					}
				} else if (event.getItem().getType() == Materials.BAT_SPAWN_EGG.parseMaterial()) {
					if (!islandManager.hasPermission(player, block.getLocation(), "SpawnEgg")) {
						event.setCancelled(true);

						messageManager.sendMessage(player,
								skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
										.getFileConfiguration().getString("Island.Settings.Permission.Message"));
						soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

						player.updateInventory();
					}
				} else if (event.getItem().getType() == Material.ARMOR_STAND) {
					if (!islandManager.hasPermission(player, block.getLocation(), "EntityPlacement")) {
						event.setCancelled(true);

						messageManager.sendMessage(player,
								skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
										.getFileConfiguration().getString("Island.Settings.Permission.Message"));
						soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

						player.updateInventory();
					}
				}
			}
		} else if (event.getAction() == Action.LEFT_CLICK_BLOCK)

		{
			if (player.getTargetBlock((Set<Material>) null, 5).getType() == Material.FIRE) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Fire")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			}
		} else if (event.getAction() == Action.PHYSICAL) {
			if (block.getType() == Materials.FARMLAND.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Crop")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			} else if (block.getType() == Materials.STONE_PRESSURE_PLATE.parseMaterial()
					|| block.getType() == Materials.OAK_PRESSURE_PLATE.parseMaterial()
					|| block.getType() == Materials.SPRUCE_PRESSURE_PLATE.parseMaterial()
					|| block.getType() == Materials.BIRCH_PRESSURE_PLATE.parseMaterial()
					|| block.getType() == Materials.JUNGLE_PRESSURE_PLATE.parseMaterial()
					|| block.getType() == Materials.ACACIA_PRESSURE_PLATE.parseMaterial()
					|| block.getType() == Materials.DARK_OAK_PRESSURE_PLATE.parseMaterial()) {
				if (!islandManager.hasPermission(player, block.getLocation(), "PressurePlate")) {
					event.setCancelled(true);
				}
			} else if (block.getType() == Material.TRIPWIRE) {
				if (!islandManager.hasPermission(player, block.getLocation(), "Redstone")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
				}
			}

		}
	}

	@EventHandler
	public void onPlayerInteractStructure(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		MessageManager messageManager = skyblock.getMessageManager();
		SoundManager soundManager = skyblock.getSoundManager();

		if (event.getItem() != null) {
			try {
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					ItemStack structureTool = StructureUtil.getTool();

					if ((event.getItem().getType() == structureTool.getType()) && (event.getItem().hasItemMeta())
							&& (event.getItem().getItemMeta().getDisplayName()
									.equals(structureTool.getItemMeta().getDisplayName()))) {
						if (player.hasPermission("skyblockearth.admin.structure.selection")
								|| player.hasPermission("skyblockearth.admin.structure.*")
								|| player.hasPermission("skyblockearth.admin.*") || player.hasPermission("skyblockearth.*")) {
							event.setCancelled(true);

							skyblock.getPlayerDataManager().getPlayerData(player).getArea().setPosition(1,
									event.getClickedBlock().getLocation());

							messageManager.sendMessage(player,
									skyblock.getFileManager()
											.getConfig(new File(skyblock.getDataFolder(), "language.yml"))
											.getFileConfiguration().getString("Island.Structure.Tool.Position.Message")
											.replace("%position", "1"));
							soundManager.playSound(player, Sounds.WOOD_CLICK.bukkitSound(), 1.0F, 1.0F);
						}
					}
				} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					ItemStack structureTool = StructureUtil.getTool();

					if ((event.getItem().getType() == structureTool.getType()) && (event.getItem().hasItemMeta())
							&& (event.getItem().getItemMeta().getDisplayName()
									.equals(structureTool.getItemMeta().getDisplayName()))) {
						if (player.hasPermission("skyblockearth.admin.structure.selection")
								|| player.hasPermission("skyblockearth.admin.structure.*")
								|| player.hasPermission("skyblockearth.admin.*") || player.hasPermission("skyblockearth.*")) {
							event.setCancelled(true);

							skyblock.getPlayerDataManager().getPlayerData(player).getArea().setPosition(2,
									event.getClickedBlock().getLocation());

							messageManager.sendMessage(player,
									skyblock.getFileManager()
											.getConfig(new File(skyblock.getDataFolder(), "language.yml"))
											.getFileConfiguration().getString("Island.Structure.Tool.Position.Message")
											.replace("%position", "2"));
							soundManager.playSound(player, Sounds.WOOD_CLICK.bukkitSound(), 1.0F, 1.0F);
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		org.bukkit.entity.Entity entity = event.getRightClicked();

		ItemStack is = player.getItemInHand();

		MessageManager messageManager = skyblock.getMessageManager();
		IslandManager islandManager = skyblock.getIslandManager();
		SoundManager soundManager = skyblock.getSoundManager();

		if (skyblock.getWorldManager().isIslandWorld(entity.getWorld())) {
			if ((is != null) && (is.getType() != Material.AIR)) {
				if (is.getType() == Materials.LEAD.parseMaterial()) {
					if (!islandManager.hasPermission(player, entity.getLocation(), "Leash")) {
						event.setCancelled(true);

						messageManager.sendMessage(player,
								skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
										.getFileConfiguration().getString("Island.Settings.Permission.Message"));
						soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

						return;
					}
				}
			}

			if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.PIG) {
				if (entity.getType() == EntityType.HORSE) {
					Horse horse = (Horse) event.getRightClicked();

					if (horse.getInventory().getSaddle() != null && player.isSneaking()) {
						if (!islandManager.hasPermission(player, horse.getLocation(), "HorseInventory")) {
							event.setCancelled(true);

							messageManager.sendMessage(player,
									skyblock.getFileManager()
											.getConfig(new File(skyblock.getDataFolder(), "language.yml"))
											.getFileConfiguration().getString("Island.Settings.Permission.Message"));
							soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

							return;
						}
					} else {
						if (!islandManager.hasPermission(player, horse.getLocation(), "MobRiding")) {
							event.setCancelled(true);

							messageManager.sendMessage(player,
									skyblock.getFileManager()
											.getConfig(new File(skyblock.getDataFolder(), "language.yml"))
											.getFileConfiguration().getString("Island.Settings.Permission.Message"));
							soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

							return;
						}
					}
				} else if (entity.getType() == EntityType.PIG) {
					if (!islandManager.hasPermission(player, entity.getLocation(), "MobRiding")) {
						event.setCancelled(true);

						messageManager.sendMessage(player,
								skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
										.getFileConfiguration().getString("Island.Settings.Permission.Message"));
						soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

						return;
					}
				}
			} else if (entity.getType() == EntityType.COW || entity.getType() == EntityType.MUSHROOM_COW) {
				if (is.getType() == Material.BUCKET) {
					if (!islandManager.hasPermission(player, entity.getLocation(), "Milking")) {
						event.setCancelled(true);

						messageManager.sendMessage(player,
								skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
										.getFileConfiguration().getString("Island.Settings.Permission.Message"));
						soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

						return;
					}
				}
			} else if (entity.getType() == EntityType.VILLAGER) {
				if (!islandManager.hasPermission(player, entity.getLocation(), "Trading")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			} else if (entity.getType() == EntityType.MINECART || entity.getType() == EntityType.BOAT) {
				if (!islandManager.hasPermission(player, entity.getLocation(), "MinecartBoat")) {
					event.setCancelled(true);

					messageManager.sendMessage(player,
							skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
									.getFileConfiguration().getString("Island.Settings.Permission.Message"));
					soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);

					return;
				}
			}

			if (entity.getType() == EntityType.HORSE) {
				if (!(is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.GOLDEN_CARROT
						|| is.getType() == Material.SUGAR || is.getType() == Material.WHEAT
						|| is.getType() == Material.APPLE || is.getType() == Material.HAY_BLOCK)) {
					return;
				}
			} else if (entity.getType() == EntityType.SHEEP || entity.getType() == EntityType.COW
					|| entity.getType() == EntityType.MUSHROOM_COW) {
				if (!(is.getType() == Material.WHEAT)) {
					return;
				}
			} else if (entity.getType() == EntityType.PIG) {
				if (!(is.getType() == Materials.CARROT.parseMaterial()
						|| is.getType() == Materials.POTATO.parseMaterial())) {
					return;
				}
			} else if (entity.getType() == EntityType.CHICKEN) {
				if (!(is.getType() == Materials.WHEAT_SEEDS.parseMaterial() || is.getType() == Material.PUMPKIN_SEEDS
						|| is.getType() == Material.MELON_SEEDS)) {
					if (NMSUtil.getVersionNumber() > 8) {
						if (!(is.getType() == Materials.BEETROOT_SEEDS.parseMaterial())) {
							return;
						}
					} else {
						return;
					}
				}
			} else if (entity.getType() == EntityType.WOLF) {
				if (!(is.getType() == Material.BONE || is.getType() == Materials.PORKCHOP.parseMaterial()
						|| is.getType() == Materials.BEEF.parseMaterial()
						|| is.getType() == Materials.CHICKEN.parseMaterial() || is.getType() == Material.RABBIT
						|| is.getType() == Material.MUTTON || is.getType() == Material.ROTTEN_FLESH
						|| is.getType() == Materials.COOKED_PORKCHOP.parseMaterial()
						|| is.getType() == Material.COOKED_BEEF || is.getType() == Material.COOKED_CHICKEN
						|| is.getType() == Material.COOKED_RABBIT || is.getType() == Material.COOKED_MUTTON)) {
					return;
				}
			} else if (entity.getType() == EntityType.OCELOT) {
				if (!(is.getType() == Materials.COD.parseMaterial() || is.getType() == Materials.SALMON.parseMaterial()
						|| is.getType() == Materials.TROPICAL_FISH.parseMaterial()
						|| is.getType() == Materials.PUFFERFISH.parseMaterial())) {
					return;
				}
			} else if (entity.getType() == EntityType.RABBIT) {
				if (!(is.getType() == Materials.DANDELION.parseMaterial()
						|| is.getType() == Materials.CARROTS.parseMaterial()
						|| is.getType() == Material.GOLDEN_CARROT)) {
					return;
				}
			} else {
				int NMSVersion = NMSUtil.getVersionNumber();

				if (NMSVersion > 10) {
					if (entity.getType() == EntityType.LLAMA) {
						if (!(is.getType() == Materials.HAY_BLOCK.parseMaterial())) {
							return;
						}
					} else if (NMSVersion > 12) {
						if (entity.getType() == EntityType.TURTLE) {
							if (!(is.getType() == Materials.SEAGRASS.parseMaterial())) {
								return;
							}
						} else {
							return;
						}
					} else {
						return;
					}
				} else {
					return;
				}
			}

			if (!islandManager.hasPermission(player, entity.getLocation(), "AnimalBreeding")) {
				event.setCancelled(true);

				messageManager.sendMessage(player,
						skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
								.getFileConfiguration().getString("Island.Settings.Permission.Message"));
				soundManager.playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (!(event.getRightClicked() instanceof ArmorStand)) {
			return;
		}

		Player player = event.getPlayer();
		org.bukkit.entity.Entity entity = event.getRightClicked();

		if (skyblock.getWorldManager().isIslandWorld(entity.getWorld())) {
			if (!skyblock.getIslandManager().hasPermission(player, entity.getLocation(), "ArmorStandUse")) {
				event.setCancelled(true);

				skyblock.getMessageManager().sendMessage(player,
						skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"))
								.getFileConfiguration().getString("Island.Settings.Permission.Message"));
				skyblock.getSoundManager().playSound(player, Sounds.VILLAGER_NO.bukkitSound(), 1.0F, 1.0F);
			}
		}
	}
}
