package ruo.awild.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;

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
            if (creeper != creature && (creeper.getAttackTarget() != null  || creeper.hasIgnited()) && !creeper.isDead ) {
                return true;
            }else
                return false;
        }
        return false;
    }

    @Override
    public void updateTask() {
        EntityCreeper  creeper = (EntityCreeper) closestLivingEntity;
        if(creeper != this.creature && (creeper.getAttackTarget() != null || creeper.hasIgnited()) && !creeper.isDead) {
            super.updateTask();
            System.out.println("크리퍼 터짐");
        }

    }
}
