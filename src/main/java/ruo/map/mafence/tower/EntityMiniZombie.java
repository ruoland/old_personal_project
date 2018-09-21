package ruo.map.mafence.tower;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.world.World;
import ruo.map.mafence.EntityMonster;
import ruo.map.mafence.EntityTower;

public class EntityMiniZombie extends EntityTower {
    public EntityMiniZombie(World worldIn) {
        super(worldIn);
        setDeathTimer(200);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityMonster.class, false));
    }

}
