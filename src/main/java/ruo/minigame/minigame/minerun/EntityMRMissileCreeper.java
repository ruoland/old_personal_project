package ruo.minigame.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

public class EntityMRMissileCreeper extends EntityMR {
    private static final DataParameter<Boolean> RUN_MISSILE = EntityDataManager.createKey(EntityMRMissileCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MISSILE = EntityDataManager.createKey(EntityMRMissileCreeper.class, DataSerializers.BOOLEAN);

    private double targetX, targetY, targetZ;

    public EntityMRMissileCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(RUN_MISSILE, false);
        dataManager.register(END_MISSILE, false);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isRunMissle() && WorldAPI.getPlayer().getDistance(targetX, targetY, targetZ) < 4) {
            setPosition(targetX, targetY + 4, targetZ);
            dataManager.set(END_MISSILE, true);
            this.setRotate(getRotateX(), getRotateY() + 180, getRotateZ());
        }
        if (isRunMissle()) {
            this.setVelocity(0, 0.01, 0);
        } else if(!isEndMissle()){
            if (getDistanceToEntity(WorldAPI.getPlayer()) < 10) {
                dataManager.set(RUN_MISSILE, true);
                targetX = MineRun.xCoord() * 10;
                targetY = posY;
                targetZ = MineRun.zCoord() * 10;
            }
        }
    }

    public boolean isEndMissle() {
        return dataManager.get(END_MISSILE);
    }

    public boolean isRunMissle() {
        return !isEndMissle() && dataManager.get(RUN_MISSILE);
    }

    @Override
    public void collideAttack(Entity entityIn) {
        worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
    }
}
