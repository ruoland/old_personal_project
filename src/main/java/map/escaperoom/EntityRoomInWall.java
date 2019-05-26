package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import olib.api.EntityAPI;

import javax.annotation.Nullable;

public class EntityRoomInWall extends EntityRoomBlock {
    public EntityRoomInWall(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        EnumFacing facing = EntityAPI.rotateY(getHorizontalFacing(), 180);
        double x = EntityAPI.lookX(facing, 3);
        double z = EntityAPI.lookZ(facing, 3);

        EntityRoomInWall inWall = new EntityRoomInWall(worldObj);
        inWall.setPosition(posX + -x, posY, posZ + -z);
        worldObj.spawnEntityInWorld(inWall);

        inWall.setTarget(posX, posY, posZ);
        this.setTarget(posX, posY, posZ);
        this.setPosition(posX + x, posY, posZ + z);
        updateSpawnPosition();
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(entityIn instanceof EntityRoomInWall) {
            ((EntityRoomInWall) entityIn).teleportSpawnPos();
            teleportSpawnPos();
        }
        if(entityIn instanceof EntityPlayer){
            entityIn.attackEntityFrom(DamageSource.inWall, 100000000);
        }
    }


    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomInWall movingBlock = new EntityRoomInWall(worldObj);
        dataCopy(movingBlock, x,y,z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(movingBlock);
        }
        return super.spawn(x, y, z);
    }

}
