package ruo.minigame.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

public class EntityMRRocketCreeper extends EntityMR {
    private static final DataParameter<Boolean> RUN_MISSILE = EntityDataManager.createKey(EntityMRRocketCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MISSILE = EntityDataManager.createKey(EntityMRRocketCreeper.class, DataSerializers.BOOLEAN);

    private double targetX, targetY, targetZ;

    public EntityMRRocketCreeper(World worldIn) {
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
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        if(dataManager.get(RUN_MISSILE)){
            collideAttack(this);
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isRunMissle() && WorldAPI.getPlayer().getDistance(targetX, targetY, targetZ) < 4) {
            setPosition(targetX, targetY + 4, targetZ);
            dataManager.set(END_MISSILE, true);
            this.setRotate(getRotateX(), getRotateY() + 180, getRotateZ());

        }

        if(!isRunMissle() && !isEndMissle()){//플레이어를 기다리는 상태
            if (getDistanceToEntity(WorldAPI.getPlayer()) < 10) {
                dataManager.set(RUN_MISSILE, true);
                targetX = MineRun.xCoord() * 10;
                targetY = posY;
                targetZ = MineRun.zCoord() * 10;
            }
        }
        if (isRunMissle()) {
            this.setVelocity(0, 0.01, 0);
            if(isEndMissle()){
                setPosition(getSpawnPosVec());
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
        dataManager.set(RUN_MISSILE, false);
        dataManager.set(END_MISSILE, true);
    }
}
