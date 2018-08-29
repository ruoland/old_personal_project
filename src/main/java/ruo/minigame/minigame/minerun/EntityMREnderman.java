package ruo.minigame.minigame.minerun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.map.lopre2.LoPre2;
import ruo.map.lopre2.jump2.EntityTeleportBlock;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;
import ruo.minigame.minigame.minerun.EntityMR;

public class EntityMREnderman extends EntityMR {
    private int moveDelay;
    private static DataParameter<Integer> MAX_DELAY = EntityDataManager.createKey(EntityMREnderman.class, DataSerializers.VARINT);

    private static DataParameter<Integer> MOVE_XZ = EntityDataManager.createKey(EntityMREnderman.class, DataSerializers.VARINT);
    private static DataParameter<Float> MOVE_X = EntityDataManager.createKey(EntityMREnderman.class, DataSerializers.FLOAT);
    private static DataParameter<Float> MOVE_Y = EntityDataManager.createKey(EntityMREnderman.class, DataSerializers.FLOAT);
    private static DataParameter<Float> MOVE_Z = EntityDataManager.createKey(EntityMREnderman.class, DataSerializers.FLOAT);

    private boolean reverse;

    public EntityMREnderman(World worldObj) {
        super(worldObj);
        this.setModel(TypeModel.ENDERMAN);
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(MAX_DELAY, 30);
        dataManager.register(MOVE_XZ, -1);
        dataManager.register(MOVE_X, 0F);
        dataManager.register(MOVE_Y, 0F);
        dataManager.register(MOVE_Z, 0F);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        PosHelper posHelper = new PosHelper(player);
        setMoveXZ(0);
        float moveY = (float) posHelper.getY(1, false);
        if (moveY != 0) {
            setMoveY(moveY);
            setMaxDelay(40);
        } else {
            setMoveXZ(0);
            setMoveX((float) posHelper.getX(SpawnDirection.FORWARD, 2, false));
            setMoveZ((float) posHelper.getZ(SpawnDirection.FORWARD, 2, false));
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void onLivingUpdate() {

        super.onLivingUpdate();
        if (isServerWorld()) {
            moveDelay++;
            if (getMoveXZ() != -1 && moveDelay >= getMaxDelay()) {
                moveDelay = 0;
                if (!reverse)
                    addMoveXZ(1);
                else if (reverse)
                    addMoveXZ(-1);

                if (getMoveXZ() == 3 || getMoveXZ() == -1) {
                    reverse = !reverse;
                    setMoveXZ(1);
                }

            }
        }

        if (getMoveXZ() == 2)
            setPosition(getSpawnX() + getMoveX(), getSpawnY() + getMoveY(), getSpawnZ() + getMoveZ());
        if (getMoveXZ() == 1)
            setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
        if (getMoveXZ() == 0)
            setPosition(getSpawnX() - getMoveX(), getSpawnY() - getMoveY(), getSpawnZ() - getMoveZ());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("moveX", getMoveX());
        compound.setDouble("moveY", getMoveY());
        compound.setDouble("moveZ", getMoveZ());
        compound.setDouble("moveXZ", getMoveXZ());
        compound.setInteger("maxDelay", getMaxDelay());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setMaxDelay(compound.getInteger("maxDelay"));
        if (getMaxDelay() == 20)
            setMaxDelay(30);
        setMoveX(compound.getFloat("moveX"));
        setMoveY(compound.getFloat("moveY"));
        setMoveZ(compound.getFloat("moveZ"));
        setMoveXZ(compound.getInteger("moveXZ"));
    }

    public void addMaxDelay(int xz) {
        dataManager.set(MAX_DELAY, getMaxDelay() + xz);
    }

    public void setMaxDelay(int xz) {
        dataManager.set(MAX_DELAY, xz);
    }

    public int getMaxDelay() {
        return dataManager.get(MAX_DELAY);
    }

    public void addMoveXZ(int xz) {
        dataManager.set(MOVE_XZ, getMoveXZ() + xz);
    }

    public void setMoveXZ(int xz) {
        dataManager.set(MOVE_XZ, xz);
    }

    public int getMoveXZ() {
        return dataManager.get(MOVE_XZ);
    }

    public void setMoveX(float xz) {
        dataManager.set(MOVE_X, xz);
    }

    public void setMoveY(float xz) {
        dataManager.set(MOVE_Y, xz);
    }

    public void setMoveZ(float xz) {
        dataManager.set(MOVE_Z, xz);
    }

    public float getMoveX() {
        return dataManager.get(MOVE_X);
    }

    public float getMoveY() {
        return dataManager.get(MOVE_Y);
    }

    public float getMoveZ() {
        return dataManager.get(MOVE_Z);
    }

}
