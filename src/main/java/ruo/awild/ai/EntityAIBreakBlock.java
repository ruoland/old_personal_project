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

public class EntityAIBreakBlock extends net.minecraft.entity.ai.EntityAIBase {
    private EntityLiving theEntity;
    private int breakTime;
    private int findDelay = 20;
    private BlockPos breakPos;
    private ArrayList<BlockPos> blockPosList = new ArrayList<>();
    public EntityAIBreakBlock(EntityLiving entityIn) {
        theEntity = entityIn;
        this.setMutexBits(3);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        breakTime = 0;
    }

    @Override
    public boolean shouldExecute() {
        if(findDelay == 0){
            findDelay = 80;
            findBlock();
            return breakPos != null && theEntity.getDistance(breakPos.getX(), breakPos.getY(), breakPos.getZ()) < 16;

        }else {
            findDelay--;
            return false;
        }
    }

    public boolean canSeeBlockPos(BlockPos pos)
    {
        return theEntity.worldObj.rayTraceBlocks(new Vec3d(this.theEntity.posX, this.theEntity.posY + theEntity.getEyeHeight(), theEntity.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
    }
    public void findBlock(){
        BlockAPI blockAPI = WorldAPI.getBlock(theEntity.worldObj, theEntity.posX - 8, theEntity.posX+8,theEntity.posY - 2,theEntity.posY+2, theEntity.posZ - 8,theEntity.posZ+8);
        for(int i = 0; i< blockAPI.size();i++){
            Block block = blockAPI.getBlock(i);
            BlockPos blockPos = blockAPI.getPosList().get(i);
            if(canSeeBlockPos(blockPos)) {
                if (block instanceof BlockFarmland && !theEntity.worldObj.isAirBlock(blockPos.add(0, 1, 0))) {
                    blockPosList.add(blockPos.add(0, 1, 0));
                }
                if (block instanceof BlockChest || block instanceof BlockTorch) {
                    blockPosList.add(blockPos);
                }
            }
        }
        if(blockPosList.size() > 0) {
            for(BlockPos pos : blockPosList)
            {
                System.out.println("전 "+theEntity.getPositionVector().distanceTo(new Vec3d(pos)));
            }
            Collections.sort(blockPosList, new FindBlockComparator(theEntity));
            breakPos = blockPosList.get(0);
            for(BlockPos pos : blockPosList)
            {
                System.out.println("후 "+theEntity.getPositionVector().distanceTo(new Vec3d(pos)));
            }
        }
    }
    public boolean continueExecuting() {
        return breakPos != null && !theEntity.getEntityWorld().isAirBlock(breakPos)&& theEntity.getDistance(breakPos.getX(), breakPos.getY(), breakPos.getZ()) < 16;
    }

    public void resetTask() {
        super.resetTask();
        this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), breakPos, -1);
        breakPos = null;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        double distance = theEntity.getDistance(breakPos.getX(), breakPos.getY(), breakPos.getZ());
        this.theEntity.getLookHelper().setLookPosition(breakPos.getX(), breakPos.getY(), breakPos.getZ(), theEntity.getHorizontalFaceSpeed(), this.theEntity.getVerticalFaceSpeed());
        if (distance < 1.5) {
            if (this.theEntity.getRNG().nextInt(20) == 0) {
                this.theEntity.worldObj.playEvent(1019, breakPos, 0);
            }
            ++this.breakTime;
            Block breakBlock = theEntity.worldObj.getBlockState(breakPos).getBlock();
            if(breakBlock instanceof BlockChest) {
                int breakProgress = (int) ((float) this.breakTime / 240.0F * 10.0F);
                this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), breakPos, breakProgress);
                theEntity.swingArm(EnumHand.MAIN_HAND);
                if (this.breakTime == 240) {
                    breakBlock();
                }
            }else{
                breakBlock();
            }
        } else {
            this.theEntity.getNavigator().tryMoveToXYZ(breakPos.getX(), breakPos.getY(), breakPos.getZ(), 1.0D);
            System.out.println(breakPos+"로 이동중");
        }

    }

    public void breakBlock(){
        this.theEntity.worldObj.destroyBlock(breakPos, true);
        this.theEntity.worldObj.playEvent(1021, breakPos, 0);
        blockPosList.remove(0);
        Collections.sort(blockPosList, new FindBlockComparator(theEntity));
        if(blockPosList.size() > 0)
        breakPos = blockPosList.get(0);
        else
            findBlock();
    }
}