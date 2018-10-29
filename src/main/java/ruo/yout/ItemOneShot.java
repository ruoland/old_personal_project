package ruo.yout;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import ruo.cmplus.cm.v18.function.FunctionFor;

import java.util.List;

public class ItemOneShot extends Item {

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;
            livingBase.setHealth(0);
            System.out.println(entity);
            if (player.isSneaking()) {
                livingBase.setHealth(0);
                List<EntityLivingBase> list = livingBase.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, livingBase.getEntityBoundingBox().expand(3,3,3));
                for(EntityLivingBase livingBase1 : list){
                    if(livingBase1 != player)
                    livingBase1.setHealth(0);
                }
            }
        }
        return super.
                onLeftClickEntity(stack, player, entity);

    }
}
