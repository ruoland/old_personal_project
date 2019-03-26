package map.lopre2;

import oneline.api.EntityAPI;
import oneline.api.WorldAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import map.lopre2.jump1.EntityInvisibleBlock;
import map.lopre2.jump2.EntityKnockbackBlock;

import java.util.ArrayList;
import java.util.List;

public class ItemCopy extends Item {
    private static ArrayList<EntityPreBlock> preBlockList = new ArrayList<EntityPreBlock>();
    private static double distance = 0, yinterval, defaultYintevar;
    private static double interval = 1;

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!target.isInvisible()) {
            System.out.println(distance);
            if (hand == EnumHand.MAIN_HAND) {
                EntityPreBlock lavaBlock = (EntityPreBlock) target;
                preBlockList.clear();
                if (playerIn.isSneaking()) {
                    for (double i = interval; i <= distance; i += interval) {
                        boolean pitchUpDown = false;
                        EntityPreBlock spawnLava = null;
                        double lavaY = lavaBlock.posY;

                        if(yinterval == 0) {
                            if (playerIn.rotationPitch <= -60 && playerIn.rotationPitch >= -90) {
                                spawnLava = lavaBlock.spawn(lavaBlock.posX, lavaBlock.posY + i, lavaBlock.posZ);
                                pitchUpDown = true;
                            }
                            if (playerIn.rotationPitch >= 60 && playerIn.rotationPitch <= 90) {
                                spawnLava = lavaBlock.spawn(lavaBlock.posX, lavaBlock.posY - i, lavaBlock.posZ);
                                pitchUpDown = true;
                            }
                        }
                        if (yinterval != 0) {
                            lavaY = lavaBlock.posY + yinterval;
                            yinterval+=defaultYintevar;
                            pitchUpDown = false;
                            System.out.println("Y "+yinterval);
                        }
                        if (!pitchUpDown) {
                            System.out.println("Y "+ yinterval);
                            spawnLava = lavaBlock.spawn(lavaBlock.posX - EntityAPI.lookX(playerIn, i),lavaY, lavaBlock.posZ - EntityAPI.lookZ(playerIn, i));
                        }
                        spawnLava.setPosition(spawnLava.getSpawnX(), spawnLava.getSpawnY(), spawnLava.getSpawnZ());
                        preBlockList.add(spawnLava);
                        System.out.println(pitchUpDown+" - "+spawnLava.getSpawnX()+ " - "+spawnLava.getSpawnY()+" - "+spawnLava.getSpawnZ());
                    }
                } else {
                    for (double i = interval; i <= distance; i += interval) {
                        double lavaY = lavaBlock.posY;
                        EntityPreBlock spawnLava = lavaBlock.spawn(lavaBlock.posX + -WorldAPI.getVecXZ(playerIn, i)[0], lavaY, lavaBlock.posZ + -WorldAPI.getVecXZ(playerIn, i)[1]);
                        spawnLava.setPosition(spawnLava.getSpawnX(), spawnLava.getSpawnY(), spawnLava.getSpawnZ());
                        preBlockList.add(spawnLava);
                        System.out.println("블럭 스폰함"+target.isServerWorld()+spawnLava+spawnLava.isServerWorld());
                    }
                }
            }
            yinterval = defaultYintevar;
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    private static EntityPreBlock[] preBlocks;
    private static EntityPreBlock lavaBlock;
    private static double lavaYP = 0;

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if(stack.hasTagCompound()) {
            tooltip.add("Y "+stack.getTagCompound().getBoolean("YONOFF"));
            tooltip.add("PREV 표시 "+stack.getTagCompound().getBoolean("PREV"));
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!isSelected && worldIn.isRemote && preBlocks != null) {
            lavaBlock = null;
            for (EntityPreBlock preBlock : preBlocks) {
                if (preBlock == null)
                    continue;
                preBlock.setDead();
            }
            preBlocks = new EntityPreBlock[(int)distance];
        }
    }

    public static void setDistance(int distance) {
        ItemCopy.distance = distance;
        preBlocks = new EntityPreBlock[distance];
    }

    public static void setYinterval(double distance) {
        ItemCopy.yinterval = distance;
        defaultYintevar = distance;
    }
    public static void setInterval(double interval) {
        ItemCopy.interval = interval;
    }

    public static ArrayList<EntityPreBlock> getPreBlockList() {
        return preBlockList;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer playerIn, Entity target) {
        if (target instanceof EntityInvisibleBlock) {
            EntityInvisibleBlock lavaBlock = (EntityInvisibleBlock) target;
            String name = target.getClass().getSimpleName().replace("Entity", "");
            lavaBlock.defaultDelay -= 5;
            lavaBlock.setCustomNameTag(name + " 딜레이:" + (lavaBlock.defaultDelay / 20F));
        }
        if (target instanceof EntityKnockbackBlock) {
            EntityKnockbackBlock lavaBlock = (EntityKnockbackBlock) target;
            if (!playerIn.isSneaking())
                lavaBlock.knockbackSize += 0.1;
            else
                lavaBlock.knockbackSize -= 0.1;
        }
        return super.onLeftClickEntity(stack, playerIn, target);
    }
}
