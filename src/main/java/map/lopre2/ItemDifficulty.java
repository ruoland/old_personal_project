package map.lopre2;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import map.lopre2.jump1.EntityLavaBlock;
import map.lopre2.jump2.EntityBigBlock;
import map.lopre2.jump2.EntityKnockbackBlock;

public class ItemDifficulty extends Item {

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(target instanceof EntityLavaBlock || target instanceof EntityBigBlock || target instanceof EntityKnockbackBlock){
            EntityPreBlock preBlock = (EntityPreBlock) target;
            preBlock.setDifficulty(preBlock.getDifficulty()+1);
            if(preBlock.getDifficulty() > 3)
                preBlock.setDifficulty(1);

        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
