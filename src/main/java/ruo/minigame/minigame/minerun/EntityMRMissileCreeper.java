package ruo.minigame.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.RenderDefaultNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

/**
 * 플레이어가 나타나면 90도 회전후 플레이어한테 날아가는 크리퍼
 */
public class EntityMRMissileCreeper extends EntityMR {
    private static final DataParameter<Boolean> RUN_MISSILE = EntityDataManager.createKey(EntityMRMissileCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MISSILE = EntityDataManager.createKey(EntityMRMissileCreeper.class, DataSerializers.BOOLEAN);

    private double targetX, targetY, targetZ, returnTime;
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
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(MiniGame.minerun.isStart() && WorldAPI.getPlayer() != null) {
            if (!isRunMissle() && !isEndMissle()) {//플레이어를 기다리는 상태
                if (getDistanceToEntity(WorldAPI.getPlayer()) < 16) {//플레이어 찾으면
                    dataManager.set(RUN_MISSILE, true);
                    targetX = MineRun.xCoord() * 10;
                    targetY = posY;
                    targetZ = MineRun.zCoord() * 10;
                    this.addRotate((int)MineRun.xCoord() * 90, 0, (int)MineRun.zCoord() * 90);
                    isLookPlayer = false;
                    lookPitch = rotationPitch;
                    lookYaw = rotationYaw;
                }
            }
            if (isRunMissle()) {
                this.rotationPitch = lookPitch;
                this.rotationYaw = lookYaw;
                this.setVelocity(-MineRun.xCoord(), 0, -MineRun.zCoord());
                //플레이어와 거리가 멀어진 경우(플레이어가 크리퍼를 지나친 경우)
                if (WorldAPI.getPlayer().getDistance(targetX, targetY, targetZ) < 20) {
                    dataManager.set(END_MISSILE, true);
                }
            }
            if (isEndMissle()) {
                returnTime++;
                if(returnTime > 60) {
                    returnTime = 0;
                    setPosition(getSpawnPosVec());
                    dataManager.set(END_MISSILE, false);
                    dataManager.set(RUN_MISSILE, false);
                    this.setRotate(0,0,0);
                    isLookPlayer = true;

                }
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
