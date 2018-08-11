package ruo.minigame.minigame.scroll;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;

public class EntityMushroom extends EntityDefaultNPC{
    private Vec3d target;
    private Vec3d firstVec, secondVec;
    private double prevX, prevY, prevZ;
    private int delay;

    public EntityMushroom(World world){
        super(world);
        this.setBlockMode(Blocks.BROWN_MUSHROOM_BLOCK);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        target = new Vec3d(posX - EntityAPI.lookPlayerX(5), 0,posZ - EntityAPI.lookPlayerZ(5)).normalize();
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public void setPos(double x, double y, double z) {
        firstVec = getPositionVector().subtract(x,y,z);
        secondVec = new Vec3d(x,y,z).subtract(getPositionVector());
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if(target != null)
            this.setVelocity(target.xCoord, target.yCoord,target.zCoord);
        if (getDistance(secondVec) < 0.5) {
            this.setPos(firstVec.xCoord, firstVec.yCoord, firstVec.zCoord);
        }
        if ((Double.compare(posX, prevX) == 0 && Double.compare(posY, prevY) == 0 && Double.compare(posZ, prevZ) == 0)) {
            delay++;
            if (delay == 20) {
                delay = 0;
                this.setPos(firstVec.xCoord, firstVec.yCoord, firstVec.zCoord);
            }
        }
        prevX = posX;
        prevY = posY;
        prevZ = posZ;
        setVelocity(firstVec.xCoord, firstVec.yCoord, firstVec.zCoord);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
    }
}
