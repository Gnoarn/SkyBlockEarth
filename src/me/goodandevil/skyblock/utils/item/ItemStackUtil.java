package me.goodandevil.skyblock.utils.item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.math.BigInteger;

import org.bukkit.inventory.ItemStack;

import me.goodandevil.skyblock.utils.NMSManager;

public class ItemStackUtil {

    private static ItemStackUtil instance;

    public static ItemStackUtil getInstance(){
        if(instance == null) {
            instance = new ItemStackUtil();
        }
        
        return instance;
    }
    
    public ItemStack deserializeItemStack(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
        DataInputStream dataInputStream = new DataInputStream(inputStream);
       
        ItemStack itemStack = null;
        
        try {
            Class<?> NBTTagCompoundClass = NMSManager.getInstance().getNMSClass("NBTTagCompound");
            Class<?> NMSItemStackClass = NMSManager.getInstance().getNMSClass("ItemStack");
            Object NBTTagCompound = NMSManager.getInstance().getNMSClass("NBTCompressedStreamTools").getMethod("a", DataInputStream.class).invoke(null, dataInputStream);
            Object craftItemStack = NMSItemStackClass.getMethod("createStack", NBTTagCompoundClass).invoke(null, NBTTagCompound);
            itemStack = (ItemStack) NMSManager.getInstance().getCraftClass("inventory.CraftItemStack").getMethod("asBukkitCopy", NMSItemStackClass).invoke(null, craftItemStack);
        } catch(Exception e) {
            e.printStackTrace();
        }
      
        return itemStack;
    }
   
    public String serializeItemStack(ItemStack item) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
       
        try {
            Class<?> NBTTagCompoundClass = NMSManager.getInstance().getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = NBTTagCompoundClass.getConstructor();
            Object NBTTagCompound = nbtTagCompoundConstructor.newInstance();
            Object NMSItemStackClass = NMSManager.getInstance().getCraftClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            NMSManager.getInstance().getNMSClass("ItemStack").getMethod("save", NBTTagCompoundClass).invoke(NMSItemStackClass, NBTTagCompound);
            NMSManager.getInstance().getNMSClass("NBTCompressedStreamTools").getMethod("a", NBTTagCompoundClass, DataOutput.class).invoke(null, NBTTagCompound, (DataOutput)dataOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }
}
