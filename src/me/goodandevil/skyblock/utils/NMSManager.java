package me.goodandevil.skyblock.utils;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSManager {

    private static NMSManager instance;

    public static NMSManager getInstance(){
        if(instance == null) {
            instance = new NMSManager();
        }
        
        return instance;
    }
    
	public String getVersion() {
	    String name = Bukkit.getServer().getClass().getPackage().getName();
	    return name.substring(name.lastIndexOf('.') + 1) + ".";
	}

	public int getVersionNumber() {
		String name = getVersion().substring(3);
	    return Integer.valueOf(name.substring(0, name.length() - 4));
	}
	
    public Class<?> getNMSClass(String className) {
        try {
            String fullName = "net.minecraft.server." + getVersion() + className;
            Class<?> clazz = Class.forName(fullName);
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Class<?> getCraftClass(String className) throws ClassNotFoundException {
        try {
            String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
            Class<?> clazz = Class.forName(fullName);
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Field getField(Class<?> clazz, String name, boolean declared) {
        try {
            Field field;
            
            if (declared) {
            	field = clazz.getDeclaredField(name);
            } else {
            	field = clazz.getField(name);
            }
            
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Object getFieldObject(Object object, Field field) {
    	try {
    		return field.get(object);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public void setField(Object object, String fieldName, Object fieldValue, boolean declared) {
    	try {
            Field field;
            
            if (declared) {
            	field = object.getClass().getDeclaredField(fieldName);
            } else {
            	field = object.getClass().getField(fieldName);
            }
            
            field.setAccessible(true);
            field.set(object, fieldValue);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void sendPacket(Player player, Object packet) {
    	try {
    		Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
