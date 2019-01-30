package ruo.asdfrpg.skill;

import atomicstryker.dynamiclights.client.DynamicLights;
import net.minecraft.entity.player.EntityPlayer;
import ruo.asdfrpg.skill.entity.EntityLight;
import ruo.asdfrpg.skill.entity.EntityLightAdapter;
import ruo.asdfrpg.skill.system.Skill;
import ruo.asdfrpg.skill.system.SkillStack;

public class SkillLight extends Skill {

    @Override
    public void onEffect(SkillStack skillStack, int data) {
        super.onEffect(skillStack, data);
        EntityLight light = new EntityLight(skillStack.getPlayer().worldObj);
        light.setPosition(skillStack.getPlayer().getPositionVector());
        skillStack.getPlayer().worldObj.spawnEntityInWorld(light);
        light.setFollower(skillStack.getPlayer(), skillStack.getLevel());
        System.out.println(skillStack.getPlayer());
        DynamicLights.addLightSource(new EntityLightAdapter(light, 15));

    }


    @Override
    public int maxLevel() {
        return 5;
    }

}
