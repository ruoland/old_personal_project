package ruo.awild.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import ruo.awild.EntityWildHorse;

public class EntityAIAvoidEntityWildHorse extends EntityAIAvoidEntity {
    private EntityWildHorse wildHorse;
    public EntityAIAvoidEntityWildHorse(EntityCreature theEntityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(theEntityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
        wildHorse = (EntityWildHorse) theEntityIn;
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !wildHorse.isTame() && !this.closestLivingEntity.isSneaking();
    }
}
