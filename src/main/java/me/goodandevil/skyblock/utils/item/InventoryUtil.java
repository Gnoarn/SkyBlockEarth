package me.goodandevil.skyblock.utils.item;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import me.goodandevil.skyblock.utils.version.NMSUtil;

public class InventoryUtil {

	@SuppressWarnings("deprecation")
	public static void removeItem(Inventory inv, int amount, boolean hasDisplayname, Material material) {
		Map<Integer, ? extends ItemStack> ammo = inv.all(material);

		for (Integer index : ammo.keySet()) {
			ItemStack is = ammo.get(index);
			ItemMeta im = is.getItemMeta();

			if (NMSUtil.getVersionNumber() > 12) {
				if (((Damageable) im).getDamage() != 0) {
					continue;
				}
			} else {
				if (is.getDurability() != 0) {
					continue;
				}
			}

			int removed = Math.min(amount, is.getAmount());
			amount -= removed;

			if (is.getAmount() == removed) {
				inv.setItem(index, null);
			} else {
				is.setAmount(is.getAmount() - removed);
			}

			if (amount <= 0) {
				break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean isInventoryFull(Inventory inv, int subtract, int amount, Material material) {
		for (int i = 0; i < inv.getSize() - subtract; i++) {
			ItemStack is = inv.getItem(i);

			if (is == null) {
				return false;
			} else if (is.getType() == material) {
				ItemMeta im = is.getItemMeta();

				if (!im.hasDisplayName()) {
					if (NMSUtil.getVersionNumber() > 12) {
						if (((Damageable) im).getDamage() != 0) {
							continue;
						}
					} else {
						if (is.getDurability() != 0) {
							continue;
						}
					}

					if (is.getAmount() < is.getMaxStackSize() && (is.getAmount() + amount) <= is.getMaxStackSize()) {
						return false;
					}
				}
			}
		}

		return true;
	}
}
