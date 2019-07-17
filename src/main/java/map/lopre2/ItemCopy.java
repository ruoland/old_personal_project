package map.lopre2;

import olib.api.EntityAPI;
import olib.api.WorldAPI;
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
    private static ArrayList<EntityPreBlock> undoBlockList = new ArrayList<EntityPreBlock>();
    private static double distance = 1, yinterval, defaultYintevar;
    private static double interval = 1;

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!target.isInvisible()) {
            if (hand == EnumHand.MAIN_HAND) {
                undoBlockList.clear();

                EntityPreBlock lavaBlock = (EntityPreBlock) target;

                for (double i = interval; i <= distance; i += interval) {
                    boolean pitchUpDown = false;
                    EntityPreBlock spawnLava = null;
                    double lavaY = lavaBlock.posY;

                    if (yinterval == 0) {
                        if (playerIn.rotationPitch <= -60 && playerIn.rotationPitch >= -90) {
                            spawnLava = lavaBlock.spawn(lavaBlock.posX, lavaBlock.posY - i, lavaBlock.posZ);
                            pitchUpDown = true;
                        }
                        if (playerIn.rotationPitch >= 60 && playerIn.rotationPitch <= 90) {
                            spawnLava = lavaBlock.spawn(lavaBlock.posX, lavaBlock.posY + i, lavaBlock.posZ);
                            pitchUpDown = true;
                        }
                    }
                    if (yinterval != 0) {
                        lavaY = lavaBlock.posY + yinterval;
                        yinterval += defaultYintevar;
                        pitchUpDown = false;
                        System.out.println("Y " + yinterval);
                    }
                    if (!pitchUpDown) {
                        System.out.println("Y " + yinterval);
                        spawnLava = lavaBlock.spawn(lavaBlock.posX - EntityAPI.lookX(playerIn, i), lavaY, lavaBlock.posZ - EntityAPI.lookZ(playerIn, i));
                    }
                    spawnLava.setPosition(spawnLava.getSpawnX(), spawnLava.getSpawnY(), spawnLava.getSpawnZ());
                    undoBlockList.add(spawnLava);
                    System.out.println(pitchUpDown + " - " + spawnLava.getSpawnX() + " - " + spawnLava.getSpawnY() + " - " + spawnLava.getSpawnZ());
                }
            }
        }
        yinterval = defaultYintevar;

        return super.itemInteractionForEntity(stack, playerIn, target, hand);

}

    private static EntityPreBlock[] preBlocks;

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (stack.hasTagCompound()) {
            tooltip.add("Y " + stack.getTagCompound().getBoolean("YONOFF"));
            tooltip.add("PREV 표시 " + stack.getTagCompound().getBoolean("PREV"));
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!isSelected && worldIn.isRemote && preBlocks != null) {
            for (EntityPreBlock preBlock : preBlocks) {
                if (preBlock == null)
                    continue;
                preBlock.setDead();
            }
            preBlocks = new EntityPreBlock[(int) distance];
        }
    }

    public static void setDistance(double distance) {
        ItemCopy.distance = distance;
        preBlocks = new EntityPreBlock[(int) distance+1];
    }

    public static void setYinterval(double distance) {
        ItemCopy.yinterval = distance;
        defaultYintevar = distance;
    }

    public static void setInterval(double interval) {
        ItemCopy.interval = interval;
    }

    public static double getDistance() {
        return distance;
    }

    public static double getInterval() {
        return interval;
    }

    public static ArrayList<EntityPreBlock> getUndoBlockList() {
        return undoBlockList;
    }
}
