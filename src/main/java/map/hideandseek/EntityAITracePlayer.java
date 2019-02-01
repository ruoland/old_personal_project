package map.hideandseek;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.world.World;

public class EntityAITracePlayer extends EntityAIBase {
    World worldObj;
    protected EntityGuardLoop attacker;
    protected int attackTick;
    Path entityPathEntity;
    private double targetX, targetY, targetZ, speed;

    public EntityAITracePlayer(EntityGuardLoop creature, double speedIn) {
        this.attacker = creature;
        this.worldObj = creature.worldObj;
        this.speed = speedIn;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.attacker.getTraceEntity();

        if (entitylivingbase == null || !entitylivingbase.isEntityAlive()) {
            return false;
        } else {
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
            return this.entityPathEntity != null;
        }
    }

    public boolean continueExecuting() {
        EntityLivingBase entitylivingbase = this.attacker.getTraceEntity();
        if (entitylivingbase == null || !entitylivingbase.isEntityAlive() || attacker.getNavigator().noPath() || attacker.getEntitySenses().canSee(entitylivingbase)) {
            return false;
        } else
            return true;
    }

    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speed);
    }

    public void updateTask() {
        EntityLivingBase entitylivingbase = this.attacker.getTraceEntity();
        if (this.attacker.getEntitySenses().canSee(entitylivingbase) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D)) {
            this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            this.targetX = entitylivingbase.posX;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.posZ;
            this.attacker.getNavigator().tryMoveToXYZ(targetX,targetY,targetZ, this.speed);
        } else if (!attacker.getEntitySenses().canSee(entitylivingbase)) {
            this.attacker.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, this.speed);
            this.attacker.getLookHelper().setLookPosition(targetX, targetY, targetZ, 30F, 30F);
            if(this.attacker.getNavigator().noPath()){
                this.attacker.setTraceEntity(null);
            }
        }

    }

}