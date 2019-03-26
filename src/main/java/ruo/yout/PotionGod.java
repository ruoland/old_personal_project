package ruo.yout;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class PotionGod extends Potion {

    public PotionGod(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
        super.performEffect(entityLivingBaseIn, p_76394_2_);
    }

    @Override
    public String getName() {
        return "God";
    }
}
