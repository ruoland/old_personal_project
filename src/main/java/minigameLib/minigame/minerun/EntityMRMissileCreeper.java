package minigameLib.minigame.minerun;

import com.sun.org.apache.regexp.internal.RE;
import minigameLib.MiniGame;
import minigameLib.api.WorldAPI;
import minigameLib.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * 플레이어가 나타나면 90도 회전후 플레이어한테 날아가는 크리퍼
 */
public class EntityMRMissileCreeper extends EntityMR {
    private static final DataParameter<Boolean> RUN_MISSILE = EntityDataManager.createKey(EntityMRMissileCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MISSILE = EntityDataManager.createKey(EntityMRMissileCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RETURN_TIME = EntityDataManager.createKey(EntityMRMissileCreeper.class, DataSerializers.VARINT);

    private double targetX, targetY, targetZ;
    private float lookPitch, lookYaw;
    public EntityMRMissileCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
        this.setElytra(true);
        this.addTraXYZ(0,0,1);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(RUN_MISSILE, false);
        dataManager.register(END_MISSILE, false);
        dataManager.register(RETURN_TIME, 0);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(MiniGame.minerun.isStart() && WorldAPI.getPlayer() != null) {
            if (!isRunMissle() && !isEndMissle()) {//플레이어를 기다리는 상태
                if (getDistanceToEntity(WorldAPI.getPlayer()) < 32) {//플레이어 찾으면
                    dataManager.set(RUN_MISSILE, true);
                    targetX = posX+ (-MineRun.xCoord() * 50);
                    targetY = posY;
                    targetZ = posZ+ (-MineRun.zCoord() * 50);
                    this.addRotate((int)MineRun.xCoord() * 90, 0, (int)MineRun.zCoord() * 90);
                    isLookPlayer = false;
                    lookPitch = rotationPitch;
                    lookYaw = rotationYaw;
                }
            }
        }
        if (isRunMissle()) {
            this.rotationPitch = lookPitch;
            this.rotationYaw = lookYaw;
            this.setVelocity(-MineRun.xCoord(), 0, -MineRun.zCoord());
            //타겟에 가까워진 경우
            if (getDistance(targetX, targetY, targetZ) < 3) {
                dataManager.set(END_MISSILE, true);
            }
        }
        if (isEndMissle()) {
            dataManager.set(RETURN_TIME, getDataManager().get(RETURN_TIME)+1);
            if(getDataManager().get(RETURN_TIME) > 60) {
                dataManager.set(RETURN_TIME, 0);
                System.out.println("원래 장소로 돌아감");
                teleportSpawnPos();
                dataManager.set(END_MISSILE, false);
                dataManager.set(RUN_MISSILE, false);
                this.setRotate(0,0,0);
                isLookPlayer = true;
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
