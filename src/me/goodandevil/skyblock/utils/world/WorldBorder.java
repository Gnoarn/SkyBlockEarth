package me.goodandevil.skyblock.utils.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.goodandevil.skyblock.utils.NMSManager;

public class WorldBorder {
	
	private static Class<?>packetPlayOutWorldBorder, packetPlayOutWorldBorderEnumClass, worldBorderClass, craftWorldClass;
	private static Constructor<?> packetPlayOutWorldBorderConstructor;
	
    private static WorldBorder instance;

    public static WorldBorder getInstance(){
        if(instance == null) {
            instance = new WorldBorder();
        }
        
        return instance;
    }
	
	static {
		try {
			packetPlayOutWorldBorder = NMSManager.getInstance().getNMSClass("PacketPlayOutWorldBorder");
			
			if (NMSManager.getInstance().getVersionNumber() < 9) {
				packetPlayOutWorldBorderEnumClass = packetPlayOutWorldBorder.getDeclaredClasses()[1];
			} else {
				packetPlayOutWorldBorderEnumClass = packetPlayOutWorldBorder.getDeclaredClasses()[0];
			}
			
			worldBorderClass = NMSManager.getInstance().getNMSClass("WorldBorder");
			craftWorldClass = NMSManager.getInstance().getCraftClass("CraftWorld");

			packetPlayOutWorldBorderConstructor = packetPlayOutWorldBorder.getConstructor(worldBorderClass,packetPlayOutWorldBorderEnumClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(Player player, double size, Location centerLocation) {
		try {
			Object worldBorder = worldBorderClass.newInstance();

			if (NMSManager.getInstance().getVersionNumber() < 9) {
				Field borderSize = worldBorder.getClass().getDeclaredField("d");
				borderSize.setAccessible(true);
				borderSize.set(worldBorder, size);	
			} else {
				Object craftWorld = craftWorldClass.cast(centerLocation.getWorld());
				Method getHandleMethod = craftWorld.getClass().getMethod("getHandle", new Class<?>[0]);
				Object worldServer = getHandleMethod.invoke(craftWorld, new Object[0]);
				NMSManager.getInstance().setField(worldBorder, "world", worldServer, false);
			}
			
			Method setCenter = worldBorder.getClass().getMethod("setCenter", double.class, double.class);
			setCenter.invoke(worldBorder, centerLocation.getX(), centerLocation.getZ());
			
			Method setSize = worldBorder.getClass().getMethod("setSize", double.class);
			setSize.invoke(worldBorder, size);
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Object packet = packetPlayOutWorldBorderConstructor.newInstance(worldBorder, Enum.valueOf((Class<Enum>) packetPlayOutWorldBorderEnumClass, "INITIALIZE"));
			NMSManager.getInstance().sendPacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
