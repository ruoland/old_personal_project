package map.escaperoom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import oneline.api.EntityAPI;

import java.util.Random;

/**
 */
public class BlockWhatBlock extends Block {
    public BlockWhatBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        if(entityIn instanceof EntityLivingBase){
            EntityLivingBase livingBase = (EntityLivingBase) entityIn;
            BlockPos livingPos = livingBase.getPosition();
            if(livingBase.posY < pos.getY() && livingPos.getZ() == pos.getZ() && livingBase.posX == pos.getX()){
                ItemStack itemStack = new ItemStack(Items.POTIONITEM);
                itemStack = PotionUtils.addPotionToItemStack(itemStack, PotionTypes.STRONG_HEALING);
                EntityAPI.spawnItem(worldIn, itemStack,pos.getX(), pos.getY(), pos.getZ()).addVelocity(Math.random(), Math.random(), Math.random());
            }
        }
    }
}
