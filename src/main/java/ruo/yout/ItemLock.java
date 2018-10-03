package ruo.yout;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

public class ItemLock extends Item {
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target.isPotionActive(MoJaYo.lockPotion)) {
            target.removePotionEffect(MoJaYo.lockPotion);
            System.out.println("포션 제거됨"+target);
        } else {
            target.setCustomNameTag("잠금");
            target.getEntityData().setDouble("LPX", target.posX);
            target.getEntityData().setDouble("LPY", target.posY);
            target.getEntityData().setDouble("LPZ", target.posZ);
            target.addPotionEffect(new PotionEffect(MoJaYo.lockPotion, 400));
            System.out.println("포션 적용됨"+target);
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
