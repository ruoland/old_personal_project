package map.lopre2.jump3;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMoCreeper extends EntityCreeper {
    private static final DataParameter<Integer> START_CHASE = EntityDataManager.createKey(EntityMoSkeleton.class, DataSerializers.VARINT);

    public EntityMoCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(START_CHASE, 0);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 2, true);
        setDead();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        onGround = true;
        motionY = 0;
        if(isServerWorld() && motionX == 0 && motionZ == 0) {
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 2, true);
            setDead();
        }

        if(!worldObj.isRemote && getAttackTarget() != null)
            dataManager.set(START_CHASE, dataManager.get(START_CHASE) + 1);
        if(getAttackTarget() != null && dataManager.get(START_CHASE) > 200){
            moveEntity((getAttackTarget().posX - posX) / 50, 0, (getAttackTarget().posZ - posZ) / 50);
            getLookHelper().setLookPositionWithEntity(getAttackTarget(), 360,360);
        }
        if(getAttackTarget() != null && getAttackTarget().isDead) {
            setDead();
        }
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    public void respawn(){
        EntityMoCreeper moCreeper = new EntityMoCreeper(worldObj);
        moCreeper.setPosition(-248.6, 103.0, 263.3);
        if(!worldObj.isRemote)
        worldObj.spawnEntityInWorld(moCreeper);
    }

}
