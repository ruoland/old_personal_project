package ruo.asdfrpg.skill;

import ruo.asdfrpg.skill.system.Skill;
import ruo.asdfrpg.skill.system.SkillStack;

public class SkillBonusAttack extends Skill {

    @Override
    public void onEffect(SkillStack playerSkill, int data) {
        super.onEffect(playerSkill, data);
        playerSkill.getTagCompound().setBoolean("available", data == 0);

    }
}
