package minigameLib.minigame.minerun;

import minigameLib.MiniGame;
import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
import minigameLib.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMRRocketCreeper extends EntityMR {
    private static final DataParameter<Boolean> RUN_MISSILE = EntityDataManager.createKey(EntityMRRocketCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MISSILE = EntityDataManager.createKey(EntityMRRocketCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RETURN_TIME = EntityDataManager.createKey(EntityMRRocketCreeper.class, DataSerializers.VARINT);

    private double targetX, targetY, targetZ, returnTime;
    private EntityWarningBlock[] warningBlocks = new EntityWarningBlock[2];
    public EntityMRRocketCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(RUN_MISSILE, false);
        dataManager.register(END_MISSILE, false);
        dataManager.register(RETURN_TIME, 0);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        if (dataManager.get(RUN_MISSILE)) {
            collideAttack(this);
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (MiniGame.minerun.isStart() && WorldAPI.getPlayer() != null) {

            if (!isRunMissle() && !isEndMissle()) {//플레이어를 기다리는 상태
                if (getDistanceToEntity(WorldAPI.getPlayer()) < 10) {
                    dataManager.set(RUN_MISSILE, true);
                    targetX = posX + EntityAPI.lookX(WorldAPI.getPlayer(), 25);
                    targetY = posY;
                    targetZ = posZ + EntityAPI.lookZ(WorldAPI.getPlayer(), 25);
                    System.out.println("타겟을 설정함 " + targetX + " - " + targetY + " - " + targetZ + " - " + MineRun.xCoord() + " - " + MineRun.zCoord());
                    for (int i = 0; i < 2; i++) {
                        warningBlocks[i] = new EntityWarningBlock(worldObj);
                        warningBlocks[i].setPosition(targetX, targetY + i, targetZ);
                        warningBlocks[i].setSpawnXYZ(targetX, targetY + i, targetZ);
                        if(isServerWorld())
                        worldObj.spawnEntityInWorld(warningBlocks[i]);
                        warningBlocks[i].setDeathTimer(100);
                    }
                }
            }
            if (isRunMissle()) {
                this.setVelocity(0, 0.2, 0);
            }
        }
        if (isRunMissle() && posY - WorldAPI.getPlayer().posY >= 16) {
            setPosition(targetX, targetY + 5, targetZ);
            dataManager.set(END_MISSILE, true);
            this.setRotate(180, 0, 0);
            this.setTra(0, 1.4F, 0);
            isFly = false;
            System.out.println("플레이어 찾음 " + targetX + " - " + targetY + " - " + targetZ);
        }

        if (isEndMissle()) {
            this.setVelocity(0, -0.2, 0);

            dataManager.set(RETURN_TIME, getDataManager().get(RETURN_TIME)+1);
            if (dataManager.get(RETURN_TIME) > 100) {
                dataManager.set(RETURN_TIME, 0);
                setInvisible(false);
                setPosition(getSpawnPosVec());
                this.setRotate(180, 0, 0);
                this.setTra(0,0,0);
                isFly = true;
                dataManager.set(END_MISSILE, false);
                dataManager.set(RUN_MISSILE, false);
                this.setRotate(0, 0, 0);
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
        worldObj.createExplosion(this, posX, posY+1, posZ, 1.5F, false);
        dataManager.set(RUN_MISSILE, false);
        dataManager.set(END_MISSILE, true);
        setInvisible(true);
        if(warningBlocks[0] != null) {
            warningBlocks[0].setDead();
            warningBlocks[1].setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("returnTime", returnTime);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        returnTime = compound.getDouble("returnTime");
    }
}
