package me.goodandevil.skyblock.utils.world.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Art;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.util.EulerAngle;

import me.goodandevil.skyblock.utils.item.ItemStackUtil;
import me.goodandevil.skyblock.utils.version.NMSUtil;
import me.goodandevil.skyblock.utils.world.block.BlockDegreesType;

@SuppressWarnings("deprecation")
public final class EntityUtil {
	
	public static EntityData convertEntityToEntityData(Entity entity, int x, int y, int z) {
        EntityData entityData = new EntityData(entity.getType().toString(), x, y, z, entity.getCustomName(), entity.isCustomNameVisible(), entity.getFireTicks(), entity.getTicksLived());
        entityData.setVersion(NMSUtil.getVersionNumber());
        
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            entityData.setArms(armorStand.hasArms());
            entityData.setHand(ItemStackUtil.serializeItemStack(armorStand.getItemInHand()));
            entityData.setHelmet(ItemStackUtil.serializeItemStack(armorStand.getHelmet()));
            entityData.setChestplate(ItemStackUtil.serializeItemStack(armorStand.getChestplate()));
            entityData.setLeggings(ItemStackUtil.serializeItemStack(armorStand.getLeggings()));
            entityData.setBoots(ItemStackUtil.serializeItemStack(armorStand.getBoots()));
            entityData.setBasePlate(armorStand.hasBasePlate());
            entityData.setVisible(armorStand.isVisible());
            entityData.setSmall(armorStand.isSmall());
            entityData.setMarker(armorStand.isMarker());
            entityData.setBodyPose(armorStand.getBodyPose().getX() + " " + armorStand.getBodyPose().getY() + " " + armorStand.getBodyPose().getZ());
            entityData.setHeadPose(armorStand.getHeadPose().getX() + " " + armorStand.getHeadPose().getY() + " " + armorStand.getHeadPose().getZ());
            entityData.setLeftArmPose(armorStand.getLeftArmPose().getX() + " " + armorStand.getLeftArmPose().getY() + " " + armorStand.getLeftArmPose().getZ());
            entityData.setLeftLegPose(armorStand.getLeftLegPose().getX() + " " + armorStand.getLeftLegPose().getY() + " " + armorStand.getLeftLegPose().getZ());
            entityData.setRightArmPose(armorStand.getRightArmPose().getX() + " " + armorStand.getRightArmPose().getY() + " " + armorStand.getRightArmPose().getZ());
            entityData.setRightLegPose(armorStand.getRightLegPose().getX() + " " + armorStand.getRightLegPose().getY() + " " + armorStand.getRightLegPose().getZ());
            
            return entityData;
        }
        
        int NMSVersion = NMSUtil.getVersionNumber();
        
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            
            if (NMSVersion > 8) {
            	entityData.setAI(livingEntity.hasAI());
            	entityData.setHand(ItemStackUtil.serializeItemStack(livingEntity.getEquipment().getItemInMainHand()));
                entityData.setOffHand(ItemStackUtil.serializeItemStack(livingEntity.getEquipment().getItemInOffHand()));
                entityData.setHelmet(ItemStackUtil.serializeItemStack(livingEntity.getEquipment().getHelmet()));
                entityData.setChestplate(ItemStackUtil.serializeItemStack(livingEntity.getEquipment().getChestplate()));
                entityData.setLeggings(ItemStackUtil.serializeItemStack(livingEntity.getEquipment().getLeggings()));
                entityData.setBoots(ItemStackUtil.serializeItemStack(livingEntity.getEquipment().getBoots()));
                entityData.setHandChance(livingEntity.getEquipment().getItemInMainHandDropChance());
                entityData.setOffHandChange(livingEntity.getEquipment().getItemInOffHandDropChance());
                entityData.setHelmetChance(livingEntity.getEquipment().getHelmetDropChance());
                entityData.setChestplateChance(livingEntity.getEquipment().getChestplateDropChance());
                entityData.setLeggingsChance(livingEntity.getEquipment().getLeggingsDropChance());
                entityData.setBootsChance(livingEntity.getEquipment().getBootsDropChance());
            }
            
            if (entity instanceof Bat) {
            	entityData.setAwake(((Bat) entityData).isAwake());
            } else if (entity instanceof Creeper) {
            	entityData.setPowered(((Creeper) entity).isPowered());
            } else if (entity instanceof Enderman) {
				MaterialData materialData = ((Enderman) entity).getCarriedMaterial();
                
                if (materialData == null) {
                    entityData.setCarryBlock("");
                } else {
                	entityData.setCarryBlock(materialData.getItemType().toString() + ":" + materialData.getData());
                }
            } else if (entity instanceof Horse) {
            	Horse horse = ((Horse) entity);
            	entityData.setHorseColor(horse.getColor().toString());
            	entityData.setHorseStyle(horse.getStyle().toString());
            	
                List<String> items = new ArrayList<>();
                
                for(ItemStack itemList : horse.getInventory().getContents()){
                    if (itemList == null){
                    	items.add(ItemStackUtil.serializeItemStack(new ItemStack(Material.AIR)));
                    } else {
                    	items.add(ItemStackUtil.serializeItemStack(itemList));
                    }
                }
                
                entityData.setInventory(items.toArray(new String[0]));
            } else if (entity instanceof IronGolem) {
            	entityData.setCreatedByPlayer(((IronGolem) entity).isPlayerCreated());
            } else if (entity instanceof Ocelot) {
            	entityData.setOcelotType(((Ocelot) entity).getCatType().toString());
            } else if (entity instanceof Pig) {
            	entityData.setSaddle(((Pig) entity).hasSaddle());
            } else if (entity instanceof Zombie) {
            	entityData.setBaby(((Zombie) entity).isBaby());
            } else if (entity instanceof PigZombie) {
            	PigZombie pigZombie = ((PigZombie) entity);
            	entityData.setAngry(pigZombie.isAngry());
            	entityData.setAngerLevel(pigZombie.getAnger());
            } else if (entity instanceof Rabbit) {
            	entityData.setRabbitType(((Rabbit) entity).getRabbitType().toString());
            } else if (entity instanceof Sheep) {
            	entityData.setSheared(((Sheep) entity).isSheared());
            	entityData.setColor(((Colorable) entity).getColor().toString());
            } else if (entity instanceof Slime) {
            	entityData.setSlimeSize(((Slime) entity).getSize());
            } else if (entity instanceof Snowman) {
            	entityData.setDerp(((Snowman) entity).isDerp());
            } else if (entity instanceof Villager) {
            	Villager villager = ((Villager) entity);
            	entityData.setProfession(villager.getProfession().toString());
            	entityData.setRiches(villager.getRiches());
            	
                List<String> items = new ArrayList<>();
                
                for(ItemStack itemList : villager.getInventory().getContents()){
                    if(itemList == null){
                    	items.add(ItemStackUtil.serializeItemStack(new ItemStack(Material.AIR)));
                    } else {
                    	items.add(ItemStackUtil.serializeItemStack(itemList));
                    }
                }
                
                entityData.setInventory(items.toArray(new String[0]));
            }
            
            if (NMSVersion > 10) {
                if (entity instanceof Llama) {
                	Llama llama = ((Llama) entity);
                	entityData.setLlamaColor(llama.getColor().toString());
                	entityData.setLlamaStrength(llama.getStrength());
                	
                    List<String> items = new ArrayList<>();
                    
                    for(ItemStack itemList : llama.getInventory().getContents()){
                        if(itemList == null){
                        	items.add(ItemStackUtil.serializeItemStack(new ItemStack(Material.AIR)));
                        } else {
                        	items.add(ItemStackUtil.serializeItemStack(itemList));
                        }
                    }
                }
                
                if (NMSVersion > 11) {
                    if(entity instanceof Parrot) {
                    	entityData.setParrotVariant(((Parrot) entity).getVariant().toString());
                    }
                }
            }
        }
        
        if (entity instanceof Ageable) {
        	Ageable ageable = ((Ageable) entity);
        	entityData.setBreed(ageable.canBreed());
        	entityData.setAge(ageable.getAge());
        	entityData.setAgeLock(ageable.getAgeLock());
        	entityData.setBaby(!ageable.isAdult());
        } else if (entity instanceof Vehicle) {
            if (entity instanceof Boat) {
                entityData.setWoodType(((Boat) entity).getWoodType().toString());
            } else if (entity instanceof StorageMinecart || entity instanceof HopperMinecart) {
                List<String> items = new ArrayList<>();
                
                for(ItemStack itemList : ((InventoryHolder) entity).getInventory().getContents()){
                    if(itemList == null){
                    	items.add(ItemStackUtil.serializeItemStack(new ItemStack(Material.AIR)));
                    } else {
                    	items.add(ItemStackUtil.serializeItemStack(itemList));
                    }
                }
                
                entityData.setInventory(items.toArray(new String[0]));
            }
        } else if (entity instanceof Hanging) {
            if (entity instanceof ItemFrame) {
            	ItemFrame itemFrame = ((ItemFrame) entity);
            	ItemStack is = itemFrame.getItem();
                
                if (is == null) {
                   	entityData.setItem("");
                } else {
                	entityData.setItem(ItemStackUtil.serializeItemStack(is));
                }
                
                entityData.setRotate(itemFrame.getRotation().toString());
            } else if (entity instanceof Painting) {
            	entityData.setArt(((Painting) entity).getArt().toString());
            }
        }
        
        return entityData;
    }
	
    public static void convertEntityDataToEntity(EntityData entityData, Location loc, BlockDegreesType type) {
    	Entity entity = loc.getWorld().spawnEntity(loc, EntityType.valueOf(entityData.getEntityType().toUpperCase()));
        entity.setCustomName(entityData.getCustomName());
        entity.setCustomNameVisible(entityData.isCustomNameVisible());
        entity.setFireTicks(entityData.getFireTicks());
        entity.setTicksLived(entityData.getTicksLived());
        
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            armorStand.setArms(entityData.hasArms());
            armorStand.setItemInHand(ItemStackUtil.deserializeItemStack(entityData.getHand()));
            armorStand.setHelmet(ItemStackUtil.deserializeItemStack(entityData.getHelmet()));
            armorStand.setChestplate(ItemStackUtil.deserializeItemStack(entityData.getChestplate()));
            armorStand.setLeggings(ItemStackUtil.deserializeItemStack(entityData.getLeggings()));
            armorStand.setBoots(ItemStackUtil.deserializeItemStack(entityData.getBoots()));
            armorStand.setBasePlate(entityData.hasBasePlate());
            armorStand.setVisible(entityData.isVisible());
            armorStand.setSmall(entityData.isSmall());
            armorStand.setMarker(entityData.isMarker());
            
            String[] bodyPose = entityData.getBodyPose().split(" ");
            armorStand.setBodyPose(new EulerAngle(Double.parseDouble(bodyPose[0]), Double.parseDouble(bodyPose[1]), Double.parseDouble(bodyPose[2])));
            
            String[] headPose = entityData.getHeadPose().split(" ");
            armorStand.setHeadPose(new EulerAngle(Double.parseDouble(headPose[0]), Double.parseDouble(headPose[1]), Double.parseDouble(headPose[2])));
            
            String[] leftArmPose = entityData.getLeftArmPose().split(" ");
            armorStand.setLeftArmPose(new EulerAngle(Double.parseDouble(leftArmPose[0]), Double.parseDouble(leftArmPose[1]), Double.parseDouble(leftArmPose[2])));
            
            String[] leftLegPose = entityData.getLeftLegPose().split(" ");
            armorStand.setLeftLegPose(new EulerAngle(Double.parseDouble(leftLegPose[0]), Double.parseDouble(leftLegPose[1]), Double.parseDouble(leftLegPose[2])));
           
            String[] rightArmPose = entityData.getRightArmPose().split(" ");
            armorStand.setRightArmPose(new EulerAngle(Double.parseDouble(rightArmPose[0]), Double.parseDouble(rightArmPose[1]), Double.parseDouble(rightArmPose[2])));
            
            String[] rightLegPose = entityData.getRightLegPose().split(" ");
            armorStand.setRightLegPose(new EulerAngle(Double.parseDouble(rightLegPose[0]), Double.parseDouble(rightLegPose[1]), Double.parseDouble(rightLegPose[2])));
        }
        
        int NMSVersion = NMSUtil.getVersionNumber();
        
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            
            if (NMSVersion > 8) {
                livingEntity.setAI(entityData.hasAI());
                livingEntity.getEquipment().setItemInMainHand(ItemStackUtil.deserializeItemStack(entityData.getHand()));
                livingEntity.getEquipment().setItemInOffHand(ItemStackUtil.deserializeItemStack(entityData.getOffHand()));
                livingEntity.getEquipment().setHelmet(ItemStackUtil.deserializeItemStack(entityData.getHelmet()));
                livingEntity.getEquipment().setChestplate(ItemStackUtil.deserializeItemStack(entityData.getChestplate()));
                livingEntity.getEquipment().setLeggings(ItemStackUtil.deserializeItemStack(entityData.getLeggings()));
                livingEntity.getEquipment().setBoots(ItemStackUtil.deserializeItemStack(entityData.getBoots()));
                livingEntity.getEquipment().setItemInMainHandDropChance(entityData.getHandChance());
                livingEntity.getEquipment().setItemInOffHandDropChance(entityData.getOffHandChance());
                livingEntity.getEquipment().setHelmetDropChance(entityData.getHelmetChance());
                livingEntity.getEquipment().setChestplateDropChance(entityData.getChestplateChance());
                livingEntity.getEquipment().setLeggingsDropChance(entityData.getLeggingsChance());
                livingEntity.getEquipment().setBootsDropChance(entityData.getBootsChance());
            }
            
            if (entity instanceof Bat) {
                ((Bat) entity).setAwake(entityData.isAwake());
            } else if (entity instanceof Creeper) {
                ((Creeper) entity).setPowered(entityData.isPowered());
            } else if (entity instanceof Enderman) {
                if (entityData.getCarryBlock().length() == 0) {
                    String[] material = entityData.getCarryBlock().split(":");
                    ((Enderman) entity).setCarriedMaterial(new MaterialData(Material.valueOf(material[0].toUpperCase()), Byte.parseByte(material[1])));
                }
            } else if (entity instanceof Horse) {
            	Horse horse = ((Horse) entity);
            	horse.setColor(Horse.Color.valueOf(entityData.getHorseColor().toUpperCase()));
            	horse.setStyle(Horse.Style.valueOf(entityData.getHorseStyle().toUpperCase()));
            	
            	List<ItemStack> items = new ArrayList<>();
            	
            	for (String inventoryList : entityData.getInventory()) {
            		items.add(ItemStackUtil.deserializeItemStack(inventoryList));
            	}
            	
            	horse.getInventory().setContents(items.toArray(new ItemStack[0]));
            } else if (entity instanceof IronGolem) {
                ((IronGolem) entity).setPlayerCreated(entityData.isCreatedByPlayer());
            } else if (entity instanceof Ocelot) {
                ((Ocelot) entity).setCatType(Ocelot.Type.valueOf(entityData.getOcelotType().toUpperCase()));
            } else if (entity instanceof Pig) {
                ((Pig) entity).setSaddle(entityData.hasSaddle());
            } else if (entity instanceof Zombie) {
                ((Zombie) entity).setBaby(entityData.isBaby());
            } else if (entity instanceof PigZombie) {
            	PigZombie pigZombie = ((PigZombie) entity);
                pigZombie.setAngry(entityData.isAngry());
                pigZombie.setAnger(entityData.getAngerLevel());
            } else if (entity instanceof Rabbit) {
                ((Rabbit) entity).setRabbitType(Rabbit.Type.valueOf(entityData.getRabbitType().toUpperCase()));
            } else if (entity instanceof Sheep) {
            	Sheep sheep = ((Sheep) entity);
                sheep.setSheared(entityData.isSheared());
                sheep.setColor(DyeColor.valueOf(entityData.getColor().toUpperCase()));
            } else if (entity instanceof Slime) {
                ((Slime) entity).setSize(entityData.getSlimeSize());
            } else if (entity instanceof Snowman) {
                ((Snowman) entity).setDerp(entityData.isDerp());
            } else if (entity instanceof Villager) {
            	Villager villager = ((Villager) entity);
            	villager.setProfession(Villager.Profession.valueOf(entityData.getProfession().toUpperCase()));
            	
            	List<ItemStack> items = new ArrayList<>();
            	
            	for (String inventoryList : entityData.getInventory()) {
            		items.add(ItemStackUtil.deserializeItemStack(inventoryList));
            	}
            	
            	villager.getInventory().setContents(items.toArray(new ItemStack[0]));
            	
            	villager.setRiches(entityData.getRiches());
            }
            
            if (NMSVersion > 10) {
                if(entity instanceof Llama) {
                	Llama llama = ((Llama) entity);
                	llama.setColor(Llama.Color.valueOf(entityData.getLlamaColor().toUpperCase()));
                	llama.setStrength(entityData.getLlamaStrength());
                	
                	List<ItemStack> items = new ArrayList<>();
                	
                	for (String inventoryList : entityData.getInventory()) {
                		items.add(ItemStackUtil.deserializeItemStack(inventoryList));
                	}
                	
                	llama.getInventory().setContents(items.toArray(new ItemStack[0]));
                }
                
                if (NMSVersion > 11) {
                    if(entity instanceof Parrot) {
                        ((Parrot) entity).setVariant(Parrot.Variant.valueOf(entityData.getParrotVariant().toUpperCase()));
                    }
                }
            }
        }
        
        if (entity instanceof Ageable) {
        	Ageable ageable = ((Ageable) entity);
        	ageable.setBreed(entityData.canBreed());
        	ageable.setAge(entityData.getAge());
        	ageable.setAgeLock(entityData.isAgeLock());
        	
            if(!entityData.isBaby()){
            	ageable.setAdult();
            }
        } else if (entity instanceof Vehicle) {
            if (entity instanceof Boat) {
                ((Boat) entity).setWoodType(TreeSpecies.valueOf(entityData.getWoodType().toUpperCase()));
            } else if (entity instanceof StorageMinecart || entity instanceof HopperMinecart) {
            	
            	List<ItemStack> items = new ArrayList<>();
            	
            	for (String inventoryList : entityData.getInventory()) {
            		items.add(ItemStackUtil.deserializeItemStack(inventoryList));
            	}
            	
            	((InventoryHolder) entity).getInventory().setContents(items.toArray(new ItemStack[0]));
            }
        } else if (entity instanceof Hanging) {
            if (entity instanceof ItemFrame) {
            	ItemFrame itemFrame = ((ItemFrame) entity);
            	
                if(0 < entityData.getItem().length()) {
                    itemFrame.setItem(ItemStackUtil.deserializeItemStack(entityData.getItem()));
                }
                
                itemFrame.setRotation(Rotation.valueOf(entityData.getRotate().toUpperCase()));
            } else if (entity instanceof Painting) {
                ((Painting) entity).setArt(Art.valueOf(entityData.getArt()));
            }
        }
    }
}
