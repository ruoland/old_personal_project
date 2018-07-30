package ruo.asdfrpg.skill;

public class SkillAutoAttack extends Skill{

    @Override
    public void onEffect(SkillStack playerSkill, int data) {
        super.onEffect(playerSkill, data);
        playerSkill.getTagCompound().setBoolean("available", data == 0);

    }
}
