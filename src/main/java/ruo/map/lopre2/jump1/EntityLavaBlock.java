package ruo.map.lopre2.jump1;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.WorldAPI;

import java.util.List;

//앉은채 우클릭 하면 위치를 바꿀 수 있음 K를누르면 그위치에 고정됨
//L키를 누른채 우클릭 하면 떨어지지 않음
//용암에 있으면 올라오는 블럭
public class EntityLavaBlock extends EntityPreBlock {
    private static final DataParameter<Float> WIDTH = EntityDataManager.createKey(EntityLavaBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> HEIGHT = EntityDataManager.createKey(EntityLavaBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> DEB_MOVE = EntityDataManager.createKey(EntityLavaBlock.class, DataSerializers.BOOLEAN);

    protected double downSpeed = 0.001;
    public EntityLavaBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
        this.noClip = !noClip;
        isFly = true;
    }


    @Override
    public String getCustomNameTag() {
        return "LavaBlock 고정:"+canTeleportLock()+"난이도:"+getDifficulty()+" 속도:"+downSpeed+" RoX:"+getRotateX()+" RoY:"+getRotateY()+" RoZ:"+getRotateZ();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(WIDTH, 0F);
        dataManager.register(HEIGHT, 0F);
        dataManager.register(DEB_MOVE, false);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityLavaBlock lavaBlock = new EntityLavaBlock(worldObj);
        lavaBlock.setTeleportLock(canTeleportLock());
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());

        lavaBlock.setBlockMode(getCurrentBlock());
        this.copyModel(lavaBlock);
        lavaBlock.setRotate(getRotateX(), getRotateY(), getRotateZ());

        lavaBlock.setBlockMetadata(getBlockMetadata());
        lavaBlock.setInv(isInv());
        lavaBlock.setInvisible(isInvisible());
        if(isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        lavaBlock.setWidth(getWidth());
        lavaBlock.setHeight(getHeight());
        lavaBlock.setSize(getWidth(), getHeight());
        return lavaBlock;
    }

    @Override
    public void teleportEnd() {
        super.teleportEnd();
        if(getScaleX() != 1 || getScaleZ() != 1){
            this.setHeight(1);
            this.setWidth(0.5F);
            this.setSize(getWidth(), getHeight());
        }
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (canTeleportLock()) {
            motionY = 0;
            motionX = 0;
            motionZ = 0;
        }
        if (!canTeleportLock()) {
            boolean isFly = true;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 2, this.posZ + 0.5D));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if ((entity instanceof EntityPlayer) && !entity.noClip) {
                        isFly = false;
                    }
                }
            }else
                isFly = true;
            if (isFly) {
                if(posY < getSpawnY()) {
                    motionZ= 0;
                    motionY = downSpeed;
                    motionX = 0;
                }
                else {
                    motionZ= 0;
                    motionY = 0;
                    motionX = 0;
                }
            }
            if (!isFly) {
                motionZ= 0;
                motionY = -downSpeed;
                motionX = 0;
            }
        }

        if(getWidth() != 0 && getHeight() != 0 && (width != getWidth() || height != getHeight())){
            setSize(getWidth(),getHeight());
        }

    }
    public float getWidth() {
        return dataManager.get(WIDTH);
    }

    public float getHeight() {
        return dataManager.get(HEIGHT);
    }

    public void setWidth(float width){
        dataManager.set(WIDTH, width);
        if(getWidth() != 0 && getHeight() != 0){
            this.setSize(getWidth(), getHeight());
        }
        System.out.println("라바블럭 사이즈 width 설정함 "+getWidth() + " - "+width);

    }

    public void setHeight(float height){
        dataManager.set(HEIGHT, height);
        if(getWidth() != 0 && getHeight() != 0){
            this.setSize(getWidth(), getHeight());
        }
        System.out.println("라바블럭 사이즈 height 설정함 "+getHeight() + " - "+height);

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("downSpeed",downSpeed);
        compound.setFloat("widthl", getWidth());
        compound.setFloat("heightl", getHeight());
        compound.setBoolean("deb", dataManager.get(DEB_MOVE));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(WIDTH, compound.getFloat("widthl"));
        dataManager.set(HEIGHT, compound.getFloat("heightl"));
        setSize(getWidth(), getHeight());
        System.out.println("라바블럭 사이즈 NBT에서 읽음 "+getWidth()+ " - "+getHeight());
        downSpeed = 0.005;
        dataManager.set(DEB_MOVE, compound.getBoolean("deb"));
    }

}
