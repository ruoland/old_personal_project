package ruo.asdfrpg.skill;

import net.minecraft.potion.PotionEffect;
import ruo.asdfrpg.AsdfRPG;
import ruo.minigame.api.WorldAPI;

public class SkillFly extends Skill {

    public SkillFly() {
        this.setLocalizedName("하늘 날기");
    }

    @Override
    public int maxLevel() {
        return 5;
    }

    @Override
    public int minLevel() {
        return 0;
    }

    @Override
    public void onEffect() {
        WorldAPI.getPlayer().addPotionEffect(new PotionEffect(AsdfRPG.flyPotion));
    }
}
