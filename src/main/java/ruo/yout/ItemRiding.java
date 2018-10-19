package ruo.yout;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

public class ItemRiding extends Item {
    private EntityLivingBase firstEntity, secondEntity;

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(playerIn.isSneaking())
        {
            playerIn.startRiding(target);
            return false;
        }
        if (firstEntity == null)
            firstEntity = target;
        else {
            secondEntity = target;
            firstEntity.startRiding(secondEntity);
            firstEntity = null;
            secondEntity = null;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
