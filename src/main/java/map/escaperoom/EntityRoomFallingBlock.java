package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
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

    private static final DataParameter<Integer> RETURN_DELAY = EntityDataManager.createKey(EntityRoomFallingBlock.class, DataSerializers.VARINT);

    public EntityRoomFallingBlock(World worldObj) {
        super(worldObj);
        setBlockMode(Blocks.STONE);
        isFly = false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(RETURN_DELAY, 0);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        spawnReturn();
        setReturnDelay(30);
        entityIn.attackEntityFrom(DamageSource.fallingBlock, 4);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        spawnReturn();
    }

    public void spawnReturn() {
        setReturnDelay(1);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isServerWorld() && getReturnDelay() > 0) {
            setReturnDelay(getReturnDelay() + 1);
            if (getReturnDelay() > 30) {
                teleportSpawnPos();
                setReturnDelay(0);
            }
        }
    }

    public int getReturnDelay() {
        return dataManager.get(RETURN_DELAY);
    }

    public void setReturnDelay(int delay) {
        dataManager.set(RETURN_DELAY, delay);
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomFallingBlock roomFallingBlock = new EntityRoomFallingBlock(worldObj);
        dataCopy(roomFallingBlock, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(roomFallingBlock);
        }
        return roomFallingBlock;
    }

    @Override
    public String getText() {
        return "떨어지고나서 1.5초 후 다시 스폰 장소로 돌아오는 블럭입니다.";
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("RETURN", getReturnDelay());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setReturnDelay(compound.getInteger("RETURN"));
    }
}
