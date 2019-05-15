package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * 반복해서 떨어지는 블럭
 */
public class EntityRoomFallingBlock extends EntityPreBlock {

    public EntityRoomFallingBlock(World worldObj) {
        super(worldObj);
        isFly = false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        spawnReturn();
        entityIn.attackEntityFrom(DamageSource.fallingBlock, 4);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        spawnReturn();
    }

    public void spawnReturn(){
        teleportSpawnPos();
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomFallingBlock roomFallingBlock = new EntityRoomFallingBlock(worldObj);
        dataCopy(roomFallingBlock, x,y,z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(roomFallingBlock);
        }
        return roomFallingBlock;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }
}
