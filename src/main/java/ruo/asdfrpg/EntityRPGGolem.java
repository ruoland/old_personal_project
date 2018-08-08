package ruo.asdfrpg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;
import java.util.List;

public class EntityRPGGolem extends EntityDefaultNPC {

    public EntityRPGGolem(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(2, new EntityAIAttackGolem(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[] {EntityRPGGolem.class}));

    }
    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        findAndThrow((EntityLivingBase) entityIn);
        return super.attackEntityAsMob(entityIn);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        return super.onInitialSpawn(difficulty, livingdata);
    }

    private void findAndThrow(EntityLivingBase target){
        List<EntityRPGGolem> golemList = worldObj.getEntitiesWithinAABB(EntityRPGGolem.class, getEntityBoundingBox().expand(10, 3, 10));
        if (golemList.size() > 0) {
            EntityRPGGolem rpgGolem = golemList.get(worldObj.rand.nextInt(Math.max(golemList.size() - 1, 0)));
            Vec3d throwVector = rpgGolem.getPositionVector().subtract(getPositionVector()).addVector(0, 1, 0).normalize().scale(1.3);
            target.setVelocity(throwVector.xCoord, throwVector.yCoord, throwVector.zCoord);
        }
    }
}
