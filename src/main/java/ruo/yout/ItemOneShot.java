package ruo.yout;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

public class ItemOneShot extends Item {
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(entity instanceof EntityLivingBase){
            EntityLivingBase livingBase = (EntityLivingBase) entity;
            livingBase.setHealth(0);
        }
        return super.onLeftClickEntity(stack, player, entity);

    }
}
