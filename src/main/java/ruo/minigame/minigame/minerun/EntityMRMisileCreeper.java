package ruo.minigame.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

public class EntityMRMisileCreeper extends EntityMR {
    private static final DataParameter<Boolean> RUN_MISSILE = EntityDataManager.createKey(EntityMRMisileCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MISSILE = EntityDataManager.createKey(EntityMRMisileCreeper.class, DataSerializers.BOOLEAN);

    private double targetX, targetY, targetZ;

    public EntityMRMisileCreeper(World worldIn) {
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
        if(!isRunMissle() && !isEndMissle()){//플레이어를 기다리는 상태
            if (getDistanceToEntity(WorldAPI.getPlayer()) < 16) {//플레이어 찾으면
                dataManager.set(RUN_MISSILE, true);
                targetX = MineRun.xCoord() * 10;
                targetY = posY;
                targetZ = MineRun.zCoord() * 10;
                this.addRotate(MineRun.xCoord() * 90, 0, MineRun.zCoord() * 90);

            }
        }
        if (isRunMissle()) {
            this.setVelocity(-MineRun.xCoord(), 0, -MineRun.zCoord());
            //플레이어와 거리가 멀어진 경우(플레이어가 크리퍼를 지나친 경우)
            if (WorldAPI.getPlayer().getDistance(targetX, targetY, targetZ) < 20) {
                dataManager.set(END_MISSILE, true);
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
