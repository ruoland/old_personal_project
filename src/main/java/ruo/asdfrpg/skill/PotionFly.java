package ruo.asdfrpg.skill;

import net.minecraft.potion.Potion;

public class PotionFly extends Potion {

    public PotionFly(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @Override
    public String getName() {
        return "Fly";
    }
}
