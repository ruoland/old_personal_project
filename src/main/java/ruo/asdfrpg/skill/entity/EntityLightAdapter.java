package ruo.asdfrpg.skill.entity;

import atomicstryker.dynamiclights.client.IDynamicLightSource;
import net.minecraft.entity.Entity;
import ruo.asdfrpg.skill.system.SkillHelper;
import ruo.asdfrpg.skill.system.Skills;

public class EntityLightAdapter implements IDynamicLightSource {
    private Entity light;
    private int level;
    public EntityLightAdapter(Entity light, int level){
        this.light = light;
        this.level = level;
    }
    @Override
    public Entity getAttachmentEntity() {
        return light;
    }

    @Override
    public int getLightLevel() {
        return level;
    }
}
