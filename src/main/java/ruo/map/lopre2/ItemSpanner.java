package ruo.map.lopre2;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.CommandDeb;
import ruo.map.lopre2.jump2.EntityBigBlock;
import ruo.map.lopre2.jump2.EntityKnockbackBlock;

import java.util.List;

public class ItemSpanner extends Item {
    public static String renderText;
    public static double debX, debZ;
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        String name = target.getClass().getSimpleName().replace("Entity", "");


        if (!target.isInvisible()) {
            if(target instanceof EntityBuildBlock){
                EntityBuildBlock lavaBlock = (EntityBuildBlock) target;
                lavaBlock.setRotate(lavaBlock.getRotateX(), lavaBlock.getRotateZ(), lavaBlock.getRotateY());
            }
            if (target instanceof EntityWaterBlockCreator && hand == EnumHand.MAIN_HAND) {
                EntityWaterBlockCreator lavaBlock = (EntityWaterBlockCreator) target;
                lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay()+1);
            }
            if (target instanceof EntityLavaBlock && hand == EnumHand.MAIN_HAND) {
                EntityLavaBlock lavaBlock = (EntityLavaBlock) target;
                lavaBlock.setCanFalling(!lavaBlock.canFalling());
                System.out.println("대상 위치" + lavaBlock.posX + " - " + lavaBlock.posY + " - " + lavaBlock.posZ);
            }
            if (target instanceof EntityInvisibleBlock) {
                EntityInvisibleBlock lavaBlock = (EntityInvisibleBlock) target;
                lavaBlock.defaultDelay += 5;
                lavaBlock.setCustomNameTag(name + " 딜레이:" + (lavaBlock.defaultDelay / 20F));
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
                lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay()+1);
                //if (lavaBlock.getRotateX() != 0 || lavaBlock.getRotateY() != 0 || lavaBlock.getRotateZ() != 0) {
                //   lavaBlock.setCanFalling(!lavaBlock.canFalling());
                //} else
                //    System.out.println("기울어진 빅 블럭만 캔 폴링 설정 가능함");
            }
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

        if (raytraceresult == null) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        } else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        } else {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
                return new ActionResult(EnumActionResult.FAIL, itemStackIn);
            } else {
                {
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    Material material = iblockstate.getMaterial();

                    if (material == Material.WATER) {
                        iblockstate.withProperty(BlockLiquid.LEVEL, 0);
                    } else if (material == Material.LAVA) {
                        iblockstate.withProperty(BlockLiquid.LEVEL, 0);
                    }
                }
            }
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    int delay = 0;

    public void on2Update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof EntityPlayer) {
            RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;
            if (rayTraceResult != null && rayTraceResult.typeOfHit != RayTraceResult.Type.MISS) {
                if (rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
                    if (rayTraceResult.entityHit instanceof EntityPreBlock) {
                        EntityPreBlock preBlock = (EntityPreBlock) rayTraceResult.entityHit;
                        if (preBlock instanceof EntityMoveBlock) {
                            EntityMoveBlock block = (EntityMoveBlock) preBlock;
                            renderText = "블럭 방향:" + block.getFacing() + "/line/"
                                    + "블럭 방향을 바꾸려면, 이동시킬 방향을 보고 블럭을 우클릭 하세요./line/"
                                    + "이동 거리:" + block.getMoveDistance() + "/line/"
                                    + "거리를 조절하려면 +키나 -키를 누르세요.";

                            if ((Keyboard.isKeyDown(13) || Keyboard.isKeyDown(78)) && delay == 0) {
                                delay = 5;
                                block.setMoveDistance(block.getMoveDistance() + 1, true);
                            }

                            if ((Keyboard.isKeyDown(12) || Keyboard.isKeyDown(74)) && delay == 0) {
                                delay = 5;
                                block.setMoveDistance(block.getMoveDistance() - 1, true);
                            }
                        }
                    }
                }
            } else
                renderText = null;
        }
        if (delay > 0 && !worldIn.isRemote)
            delay--;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer playerIn, Entity target) {
        if (target instanceof EntityBigBlock) {
            EntityBigBlock lavaBlock = (EntityBigBlock) target;
            lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay()-1);
        }
        if (target instanceof EntityWaterBlockCreator) {
            EntityWaterBlockCreator lavaBlock = (EntityWaterBlockCreator) target;
            lavaBlock.setDefaultDelay(lavaBlock.getDefaultDelay()-1);
        }
        if (target instanceof EntityLavaBlock) {
            EntityLavaBlock lavaBlock = (EntityLavaBlock) target;
            lavaBlock.setWidth(0.5F);
            lavaBlock.setHeight(1F);
            lavaBlock.setScale(0.5F,1F,0.5F);
            lavaBlock.setSpawnPosition();
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
