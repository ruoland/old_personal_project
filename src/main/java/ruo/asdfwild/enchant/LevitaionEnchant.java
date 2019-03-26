package ruo.asdfwild.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import oneline.api.EntityAPI;

import java.util.List;

public class LevitaionEnchant extends Enchantment {
    public LevitaionEnchant() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, null);
        this.setName("Levitation");
        this.setRegistryName("SUAKGE", "Levitation");
    }


    @Override
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        super.onEntityDamaged(user, target, level);
        List<EntityLiving> mobList = EntityAPI.getEntity(user.worldObj, user.getEntityBoundingBox().expandXyz(level * 2), EntityLiving.class);
        for (EntityLiving mob : mobList) {
            if ((mob instanceof IMob && mob.getAttackTarget() instanceof EntityPlayer)) {
                mob.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, level * 50));
            }
        }
    }



    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }
}
