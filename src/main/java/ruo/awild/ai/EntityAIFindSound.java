package ruo.awild.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIFindSound extends net.minecraft.entity.ai.EntityAIBase {
    private EntityLiving theEntity;
    private BlockPos soundPos;
    private Vec3d soundVec3d;
    public EntityAIFindSound(EntityLiving entityIn) {
        theEntity = entityIn;
        this.setMutexBits(3);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public boolean shouldExecute() {
        return theEntity.getAttackTarget() == null && soundPos != null;
    }


    public boolean continueExecuting() {
        return  theEntity.getAttackTarget() == null && soundPos != null;
    }

    public void resetTask() {
        super.resetTask();
        soundPos = null;
    }

    public void setSoundPos(BlockPos soundPos) {
        this.soundPos = soundPos;
        soundVec3d = new Vec3d(soundPos.getX(), soundPos.getY()+1, soundPos.getZ());
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        double distance = theEntity.getDistance(soundPos.getX(), soundPos.getY(), soundPos.getZ());
        this.theEntity.getLookHelper().setLookPosition(soundPos.getX(), soundPos.getY(), soundPos.getZ(), theEntity.getHorizontalFaceSpeed(), this.theEntity.getVerticalFaceSpeed());
        if (distance < 5 && canSee()) {
            soundPos = null;
        } else {
            this.theEntity.getNavigator().tryMoveToXYZ(soundPos.getX(), soundPos.getY(), soundPos.getZ(), 1.0D);
        }
    }

    public boolean canSee(){
        return theEntity.worldObj.rayTraceBlocks(new Vec3d(theEntity.posX, theEntity.posY, theEntity.posZ),soundVec3d, false, true, false) == null;

    }
}