package ruo.asdfrpg.skill;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ruo.asdfrpg.skill.system.Skill;
import ruo.asdfrpg.skill.system.SkillStack;

public class SkillIronBody extends Skill {
    @Override
    public void onEffect(SkillStack skillStack, int data) {
        super.onEffect(skillStack, data);
        skillStack.getPlayer().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("iron_body"), skillStack.getLevel() * 100));

    }
}
