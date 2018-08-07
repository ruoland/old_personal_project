package ruo.asdfrpg.skill;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SkillIronBody extends Skill {
    @Override
    public void onEffect(SkillStack skillStack, int data) {
        super.onEffect(skillStack, data);
        skillStack.getPlayer().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("iron_body"), skillStack.getLevel() * 100));

    }
}