package ruo.awild.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;
import ruo.awild.EntityWildHorse;

public class EntityAIAvoidEntityCreeper extends EntityAIAvoidEntity {
    private EntityCreature creature;
    public EntityAIAvoidEntityCreeper(EntityCreature theEntityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(theEntityIn, EntityCreeper.class, avoidDistanceIn, farSpeedIn, nearSpeedIn);
        creature = theEntityIn;
    }

    @Override
    public boolean shouldExecute() {
        if(super.shouldExecute()) {
            EntityCreeper creeper = (EntityCreeper) closestLivingEntity;
            if (creeper.hasIgnited() && !creeper.isDead) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        EntityCreeper  creeper = (EntityCreeper) closestLivingEntity;
        System.out.println(creeper.getCustomNameTag()+creeper.isServerWorld()+creeper.hasIgnited());
        if(creeper.hasIgnited() && !creeper.isDead) {
            super.updateTask();
            System.out.println(creature+"크리퍼 터짐");
        }
    }
}
