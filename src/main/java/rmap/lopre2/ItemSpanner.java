package rmap.lopre2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rmap.lopre2.jump1.*;
import ruo.map.lopre2.jump1.*;
import rmap.lopre2.jump2.EntityBigBlock;
import rmap.lopre2.jump2.EntityKnockbackBlock;

public class ItemSpanner extends Item {
    public static String renderText;
    public static double debX, debZ;

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(target instanceof EntityPreBlock) {
            String name = ((EntityPreBlock) target).getJumpName();

            if (!target.isInvisible()) {
                if (target instanceof EntityBuildBlock && hand == EnumHand.MAIN_HAND) {
                    EntityBuildBlock buildBlock = (EntityBuildBlock) target;//빌드 블럭 저장
                    Loop.saveBuildBlock(buildBlock);
                }
                if (target instanceof EntityWaterBlockCreator && hand == EnumHand.MAIN_HAND) {
                    EntityWaterBlockCreator lavaBlock = (EntityWaterBlockCreator) target;
                    lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay() + 1);
                }
                if (target instanceof EntityLavaBlock && hand == EnumHand.MAIN_HAND) {
                    EntityLavaBlock lavaBlock = (EntityLavaBlock) target;
                    lavaBlock.setTeleportLock(!lavaBlock.canTeleportLock());
                    System.out.println("대상 위치" + lavaBlock.posX + " - " + lavaBlock.posY + " - " + lavaBlock.posZ);
                }
                if (target instanceof EntityInvisibleBlock) {
                    EntityInvisibleBlock lavaBlock = (EntityInvisibleBlock) target;
                    if(playerIn.isSneaking()){
                        lavaBlock.setCollision(!lavaBlock.canCollision());
                    }
                }
                if (target instanceof EntityMoveBlock) {
                    EntityMoveBlock lavaBlock = (EntityMoveBlock) target;
                    if (playerIn.rotationPitch <= -60 && playerIn.rotationPitch >= -90) {
                        lavaBlock.setPos(playerIn, lavaBlock.posY + 3, 2);
                        return true;
                    }
                    if (playerIn.rotationPitch >= 60 && playerIn.rotationPitch <= 90) {
                        lavaBlock.setPos(playerIn, lavaBlock.posY - 3, 2);
                        return true;
                    }
                    lavaBlock.setPos(playerIn, lavaBlock.posY, 2);
                    lavaBlock.setCustomNameTag(name + " 방향:" + lavaBlock.getFacing());
                }
                if (target instanceof EntityBigBlock) {
                    EntityBigBlock lavaBlock = (EntityBigBlock) target;
                    lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay() + 1);
                    //if (lavaBlock.getRotateX() != 0 || lavaBlock.getRotateY() != 0 || lavaBlock.getRotateZ() != 0) {
                    //   lavaBlock.setCanFalling(!lavaBlock.canFalling());
                    //} else
                    //    System.out.println("기울어진 빅 블럭만 캔 폴링 설정 가능함");
                }
            }
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }


    int delay = 0;

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer playerIn, Entity target) {
        if (target instanceof EntityBigBlock) {
            EntityBigBlock lavaBlock = (EntityBigBlock) target;
            lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay() - 1);
        }
        if (target instanceof EntityWaterBlockCreator) {
            EntityWaterBlockCreator lavaBlock = (EntityWaterBlockCreator) target;
            lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay() - 1);
        }
        if (target instanceof EntityLavaBlock) {
            EntityLavaBlock lavaBlock = (EntityLavaBlock) target;
            if(lavaBlock.width == 1F && lavaBlock.height == 1 || lavaBlock.getWidth() == 1F && lavaBlock.getHeight() == 1F){
                lavaBlock.setWidth(0.5F);
                lavaBlock.setHeight(1F);
                lavaBlock.setScale(0.5F, 1F, 0.5F);
                lavaBlock.setTra(0,0F,0);
                lavaBlock.teleportSpawnPos();

            }else if(lavaBlock.width == 0.5F && lavaBlock.height == 1F ||lavaBlock.getWidth() == 0.5F && lavaBlock.getHeight() == 1F){
                lavaBlock.setWidth(1F);
                lavaBlock.setHeight(0.3F);
                lavaBlock.setScale(1,0.3F,1);
                lavaBlock.setTra(0,0.3F,0);
                lavaBlock.teleportSpawnPos();
            }else{
                lavaBlock.setWidth(1F);
                lavaBlock.setHeight(1F);
                lavaBlock.setScale(1,1,1);
                lavaBlock.setTra(0,0F,0);
                lavaBlock.teleportSpawnPos();
            }
        }
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
