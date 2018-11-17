package ruo.yout;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIMojaeAttackMelee extends EntityAIAttackMelee {
    public EntityAIMojaeAttackMelee(EntityCreature creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        attackTick = 0;
    }


}
