package ruo.map.lopre2;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.map.lopre2.jump1.EntityInvisibleBlock;
import ruo.map.lopre2.jump1.EntityLavaBlock;
import ruo.map.lopre2.jump1.EntityMoveBlock;
import ruo.map.lopre2.jump1.EntityWaterBlockCreator;
import ruo.map.lopre2.jump2.EntityBigBlock;
import ruo.map.lopre2.jump2.EntityKnockbackBlock;

public class ItemDifficulty extends Item {

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(target instanceof EntityPreBlock){
            EntityPreBlock preBlock = (EntityPreBlock) target;
            preBlock.setDifficulty(preBlock.getDifficulty()+1);
            if(preBlock.getDifficulty() > 2)
                preBlock.setDifficulty(1);
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
