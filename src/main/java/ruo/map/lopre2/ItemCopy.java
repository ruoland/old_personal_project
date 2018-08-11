package ruo.map.lopre2;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.map.lopre2.jump2.EntityKnockbackBlock;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.ArrayList;
import java.util.List;

public class ItemCopy extends Item {
    private static ArrayList<EntityPreBlock> preBlockList = new ArrayList<EntityPreBlock>();
    private static int distance = 0;
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
                        if (playerIn.rotationPitch <= -60 && playerIn.rotationPitch >= -90) {
                            spawnLava = lavaBlock.spawn(lavaBlock.posX, lavaBlock.posY + i, lavaBlock.posZ);
                            pitchUpDown = true;
                        }
                        if (playerIn.rotationPitch >= 60 && playerIn.rotationPitch <= 90) {
                            spawnLava = lavaBlock.spawn(lavaBlock.posX, lavaBlock.posY - i, lavaBlock.posZ);
                            pitchUpDown = true;
                        }
                        if (!pitchUpDown) {
                            spawnLava = lavaBlock.spawn(lavaBlock.posX - EntityAPI.lookX(playerIn, i), lavaBlock.posY, lavaBlock.posZ - EntityAPI.lookZ(playerIn, i));
                        }
                        spawnLava.setPosition(spawnLava.getSpawnX(), spawnLava.getSpawnY(), spawnLava.getSpawnZ());
                        preBlockList.add(spawnLava);
                        System.out.println(pitchUpDown+" - "+spawnLava.getSpawnX()+ " - "+spawnLava.getSpawnY()+" - "+spawnLava.getSpawnZ());
                    }
                } else {
                    for (double i = interval; i <= distance; i += interval) {
                        double lavaY = lavaBlock.posY;
                        if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("YONOFF")) {
                            lavaY = lavaBlock.posY + -(playerIn.getLookVec().yCoord * (i + lavaYP));
                        }
                        EntityPreBlock spawnLava = lavaBlock.spawn(lavaBlock.posX + -WorldAPI.getVecXZ(playerIn, i)[0], lavaY, lavaBlock.posZ + -WorldAPI.getVecXZ(playerIn, i)[1]);
                        spawnLava.setPosition(spawnLava.getSpawnX(), spawnLava.getSpawnY(), spawnLava.getSpawnZ());
                        preBlockList.add(spawnLava);
                    }
                }
            }
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
        if (isSelected && entityIn instanceof EntityPlayer && preBlocks != null && stack.hasTagCompound() && stack.getTagCompound().getBoolean("PREV")) {
            EntityPlayer playerIn = (EntityPlayer) entityIn;
            RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;

            if ((rayTraceResult != null && rayTraceResult.entityHit != null) || lavaBlock != null) {
                if (ItemCopy.lavaBlock == null) {
                    lavaBlock = (EntityPreBlock) rayTraceResult.entityHit;
                }
                if (lavaBlock.isCopyBlock() || lavaBlock.isTeleport()) {
                    lavaBlock = null;
                    return;
                }
                lavaBlock.setForceSpawn(true);
                double lavaY = lavaBlock.posY;
                for (int i = 1; i <= preBlocks.length; i++) {
                    if (preBlocks[i] == null)
                        preBlocks[i] = lavaBlock.spawn(lavaBlock.posX + -WorldAPI.getVecXZ(playerIn, i)[0], lavaY, lavaBlock.posZ + -WorldAPI.getVecXZ(playerIn, i)[1]);
                    preBlocks[i].setCopyBlock(true);
                    if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("YONOFF")) {
                        lavaY = lavaBlock.posY + -(playerIn.getLookVec().yCoord * (i + lavaYP));
                    }
                    preBlocks[i].setPosition(lavaBlock.posX + -WorldAPI.getVecXZ(playerIn, i)[0], lavaY, lavaBlock.posZ + -WorldAPI.getVecXZ(playerIn, i)[1]);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    lavaYP += 0.1;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    lavaYP -= 0.1;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_L) && !playerIn.getCooldownTracker().hasCooldown(this)) {
                    itemInteractionForEntity(stack, playerIn, EntityDefaultNPC.getUUIDNPC(lavaBlock.getUniqueID()), EnumHand.MAIN_HAND);
                    playerIn.getCooldownTracker().setCooldown(this, 10);
                }
            }
        }
        if (!isSelected && worldIn.isRemote && preBlocks != null) {
            lavaBlock = null;
            for (EntityPreBlock preBlock : preBlocks) {
                if (preBlock == null)
                    continue;
                ;
                preBlock.setDead();
            }
            preBlocks = new EntityPreBlock[distance];
        }
    }

    public static void setDistance(int distance) {
        ItemCopy.distance = distance;
        preBlocks = new EntityPreBlock[distance];
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
