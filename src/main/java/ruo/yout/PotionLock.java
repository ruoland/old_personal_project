package ruo.yout;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class PotionLock extends Potion {

    public PotionLock(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
        super.performEffect(entityLivingBaseIn, p_76394_2_);
        entityLivingBaseIn.setVelocity(0,0,0);
    }

    @Override
    public String getName() {
        return "Lock";
    }
}
