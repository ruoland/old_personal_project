package ruo.yout.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemExplosion extends Item {
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        playerIn.worldObj.createExplosion(playerIn, target.posX, target.posY, target.posZ, 3F, false);
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
