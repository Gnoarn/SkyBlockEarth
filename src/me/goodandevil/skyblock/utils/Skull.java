package me.goodandevil.skyblock.utils;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.goodandevil.skyblock.utils.version.Materials;

public class Skull {

    private static Skull instance;

    public static Skull getInstance(){
        if(instance == null) {
            instance = new Skull();
        }
        
        return instance;
    }
	
	public ItemStack create(String skinTexture) {
		ItemStack is = Materials.LEGACY_SKULL_ITEM.getPostItem();
		SkullMeta sm = (SkullMeta) is.getItemMeta();
		
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
       
		byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinTexture).getBytes());
        gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        
        Field profileField = null;
        
        try {
            profileField = sm.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(sm, gameProfile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        
        is.setItemMeta(sm);
        
        return is;
	}
	
	public ItemStack create(String signature, String value) {
		ItemStack is = Materials.LEGACY_SKULL_ITEM.getPostItem();
		SkullMeta sm = (SkullMeta) is.getItemMeta();
		
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
        
        Field profileField = null;
        
        try {
            profileField = sm.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(sm, gameProfile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        
        is.setItemMeta(sm);
        
        return is;
	}
}
