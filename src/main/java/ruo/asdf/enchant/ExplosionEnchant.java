package ruo.asdf.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ExplosionEnchant extends Enchantment {
    public ExplosionEnchant() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, null);
        this.setName("EXPLOSION!!!!!!!!!!!!!!!!!!!!!!");
        this.setRegistryName("SUAKGE", "EXPLOSION");
    }


    @Override
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        super.onEntityDamaged(user, target, level);
        user.worldObj.createExplosion(user, user.posX, user.posY, user.posZ, level * 2, false);
        user.getHeldItemMainhand().damageItem(50, user);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return super.canApply(stack);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }
}
