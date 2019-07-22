package map.lopre2.jump3;

import map.lopre2.EntityPreBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import olib.api.WorldAPI;

import java.util.List;

public class EntityFlowBlock extends EntityPreBlock {
    private static final DataParameter<Float> MO_X = EntityDataManager.createKey(EntityFlowBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> MO_Z = EntityDataManager.createKey(EntityFlowBlock.class, DataSerializers.FLOAT);

    public EntityFlowBlock(World worldObj) {
        super(worldObj);
        isFly = true;
        setBlockMode(Blocks.STONE);
        setCollision(true);
        setScale(1.3F,1,1.3F);
        setSize(1.3F,1F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(MO_X, 0F);
        dataManager.register(MO_Z, 0F);
    }

    public void setMoX(float x){
        dataManager.set(MO_X, x);
    }
    public void setMoZ(float z){
        dataManager.set(MO_Z, z);
    }

    public float getMoX(){
        return dataManager.get(MO_X);
    }

    public float getMoZ(){
        return dataManager.get(MO_Z);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityFlowBlock lavaInvisible = new EntityFlowBlock(worldObj);
        dataCopy(lavaInvisible, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaInvisible);
        }
        if (rand.nextBoolean()) {
            lavaInvisible.setMoX((float) WorldAPI.rand(3) / 50F);
            while(lavaInvisible.getMoX() ==0){
                lavaInvisible.setMoX((float) WorldAPI.rand(3) / 50F);
            }
        }
        else {
            lavaInvisible.setMoZ((float) WorldAPI.rand(3) / 50F);
            while(lavaInvisible.getMoZ() ==0){
                lavaInvisible.setMoZ((float) WorldAPI.rand(3) / 50F);
            }
        }
        System.out.println(lavaInvisible.getMoX() + " - "+lavaInvisible.getMoZ());
        return lavaInvisible;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!isTeleport()) {
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 2, this.posZ + 0.5D));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
                        entity.motionY = Minecraft.getMinecraft().thePlayer.jumpMovementFactor;
                    } else if ((entity instanceof EntityPlayer) && !entity.noClip && entity.onGround) {
                        entity.moveEntity(getMoX(), 0, getMoZ());
                    }
                }
            }
        }
        moveEntity(getMoX(), 0, getMoZ());
        if(getDistance(getSpawnPosVec()) >= 1.5){
            setMoX(-getMoX());
            setMoZ(-getMoZ());
        }
    }

    @Override
    public String getText() {
        return "용암위에 있으면 아무 방향으로 이동하는 블럭입니다";
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("mox", getMoX());
        compound.setDouble("moz", getMoZ());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setMoX((float) compound.getDouble("mox"));
        setMoZ((float) compound.getDouble("moz"));
    }
}
