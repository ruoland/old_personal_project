package ruo.asdfrpg.skill;

import atomicstryker.dynamiclights.client.DynamicLights;
import atomicstryker.dynamiclights.client.IDynamicLightSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
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
