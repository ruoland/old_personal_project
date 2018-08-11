package ruo.asdfrpg.skill;

import ruo.asdfrpg.EntityLight;

public class SkillLight extends Skill {

    @Override
    public void onEffect(SkillStack skillStack, int data) {
        super.onEffect(skillStack, data);
        EntityLight light = new EntityLight(skillStack.getPlayer().worldObj);
        light.setPosition(skillStack.getPlayer().getPositionVector());
        skillStack.getPlayer().worldObj.spawnEntityInWorld(light);
        light.setFollower(skillStack.getPlayer(), skillStack.getLevel());
        System.out.println(skillStack.getPlayer());
    }


    @Override
    public int maxLevel() {
        return 5;
    }

}
