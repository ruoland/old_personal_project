package ruo.asdfrpg.skill.potion;

import net.minecraft.potion.Potion;

public class PotionIronBody extends Potion {

    public PotionIronBody(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @Override
    public String getName() {
        return "IronBody";
    }
}
