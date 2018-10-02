package ruo.yout;

import net.minecraft.potion.Potion;

public class PotionLock extends Potion {

    public PotionLock(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @Override
    public String getName() {
        return "Lock";
    }
}
