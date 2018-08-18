package ruo.map.lopre2.jump2;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityMoveBlock;

public class EntityBigBlockMove extends EntityMoveBlock {
    public EntityBigBlockMove(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
        this.noClip = !noClip;
        isFly = true;
        this.setScale(3, 1, 3);
        this.setSize(3, 1);
        this.speed = 0.08;
        this.setBlockMove(true);
    }
    @Override
    public String getCustomNameTag() {
        return "BigBlockMove " +getFacing().getName();
    }


    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public EntityBigBlockMove spawn(double x, double y, double z) {
        EntityBigBlockMove lavaBlock = new EntityBigBlockMove(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
        lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ(), 90, 90, 1, false);
        lavaBlock.setBlockMode(getCurrentStack());
        worldObj.spawnEntityInWorld(lavaBlock);
        this.copyModel(lavaBlock);
        return lavaBlock;
    }

    @Override
    public void setDead() {
        super.setDead();
        try{
            new NullPointerException();
        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
