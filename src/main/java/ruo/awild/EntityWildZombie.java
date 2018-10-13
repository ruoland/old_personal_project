package ruo.awild;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.awild.ai.EntityAIAvoidEntityCreeper;
import ruo.awild.ai.EntityAIBreakBlock;
import ruo.awild.ai.EntityAIFindSound;

import javax.annotation.Nullable;

public class EntityWildZombie extends EntityZombie {

    private int findDelay = 0;
    public EntityWildZombie(World world){
        super(world);
        this.tasks.addTask(4, new EntityAIBreakBlock(this));

        //this.tasks.addTask(4, new EntityAIBreakFarm(this));
    }
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAIAvoidEntityCreeper(this,  6.0F, 1.3D, 1.5D));
        this.tasks.addTask(5, new EntityAIFindSound(this));

        //this.tasks.addTask(2, new EntityAIBlockPlace(this));
    }
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        //findChest()
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }
}
