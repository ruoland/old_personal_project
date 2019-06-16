package ruo.what;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemTheHeart extends Item {

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(entity instanceof EntityLivingBase){
            ((EntityLivingBase) entity).setHealth(((EntityLivingBase) entity).getMaxHealth() * 2);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
