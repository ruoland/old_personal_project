package ruo.asdfrpg.skill;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ruo.asdfrpg.skill.system.Skill;
import ruo.asdfrpg.skill.system.SkillStack;

public class SkillFly extends Skill {

    public SkillFly() {
        this.setLocalizedName("하늘 날기");
    }

    @Override
    public int maxLevel() {
        return 5;
    }

    @Override
    public void onEffect(SkillStack skillStack, int data) {
        skillStack.getPlayer().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("fly"), skillStack.getLevel() * 100));
    }
}
