package ruo.asdf.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;

public class EntityAIBlockPlace extends net.minecraft.entity.ai.EntityAIBase {
    private EntityLiving theEntity;
    private int blockDelay;

    public EntityAIBlockPlace(EntityLiving entityIn) {
        theEntity = entityIn;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public boolean shouldExecute() {
        return theEntity.getAttackTarget() != null && (theEntity.getAttackTarget().posY - theEntity.posY) > 0;
    }

    public boolean continueExecuting() {
        return shouldExecute();
    }

    public void resetTask() {
        super.resetTask();
        blockDelay = 5;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        double y = theEntity.getAttackTarget().posY - theEntity.posY;
        BlockPos entityPosition = theEntity.getPosition();
        World worldObj = theEntity.worldObj;
        if (blockDelay == 0) {
            theEntity.setJumping(true);
            if(y > 0 && worldObj.isAirBlock(entityPosition.add(0, -1, 0))) {
                worldObj.setBlockState(entityPosition.add(0, -1, 0), Blocks.DIRT.getDefaultState());
                blockDelay = 5;
            }
            if(worldObj.isAirBlock(entityPosition.add(EntityAPI.lookX(theEntity, 1), -1, EntityAPI.lookZ(theEntity, 1)))) {
                worldObj.setBlockState(entityPosition.add(EntityAPI.lookX(theEntity, 1), -1, EntityAPI.lookZ(theEntity, 1)), Blocks.DIRT.getDefaultState());
                blockDelay = 5;
            }
        }
        if (blockDelay > 0)
            blockDelay--;

    }
}