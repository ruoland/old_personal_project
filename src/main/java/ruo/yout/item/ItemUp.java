package ruo.yout.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemUp extends Item {
    public static double motion=100;
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        target.motionY += 1000;
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.motionY+=motion;
        if(playerIn.getRidingEntity() != null){
            playerIn.getRidingEntity().motionY+=motion;
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
