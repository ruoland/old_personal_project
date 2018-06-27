package ruo.awild.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.Collections;

public class EntityAIFindSound extends net.minecraft.entity.ai.EntityAIBase {
    private EntityLiving theEntity;
    private BlockPos soundPos;

    public EntityAIFindSound(EntityLiving entityIn) {
        theEntity = entityIn;
        this.setMutexBits(3);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        System.out.println(soundPos+"추적중");
    }

    @Override
    public boolean shouldExecute() {
        return soundPos != null;
    }


    public boolean continueExecuting() {
        return soundPos != null;
    }

    public void resetTask() {
        super.resetTask();
        soundPos = null;
    }

    public void setSoundPos(BlockPos soundPos) {
        this.soundPos = soundPos;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        double distance = theEntity.getDistance(soundPos.getX(), soundPos.getY(), soundPos.getZ());
        this.theEntity.getLookHelper().setLookPosition(soundPos.getX(), soundPos.getY(), soundPos.getZ(), theEntity.getHorizontalFaceSpeed(), this.theEntity.getVerticalFaceSpeed());
        if (distance < 1.5) {
            soundPos = null;
        } else {
            this.theEntity.getNavigator().tryMoveToXYZ(soundPos.getX(), soundPos.getY(), soundPos.getZ(), 1.0D);
        }
    }
}