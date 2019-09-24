package map.lopre2.jump1;

import map.lopre2.EntityPreBlock;
import map.lopre2.LoPre2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import java.util.List;

//앉은채 우클릭 하면 위치를 바꿀 수 있음 K를누르면 그위치에 고정됨
//L키를 누른채 우클릭 하면 떨어지지 않음
//용암에 있으면 올라오는 블럭
public class EntityLavaBlock extends EntityPreBlock {
    private static final DataParameter<Float> WIDTH = EntityDataManager.createKey(EntityLavaBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> HEIGHT = EntityDataManager.createKey(EntityLavaBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> DEB_MOVE = EntityDataManager.createKey(EntityLavaBlock.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_BOAT = EntityDataManager.createKey(EntityLavaBlock.class, DataSerializers.BOOLEAN);

    protected double downSpeed = 0.001;
    public EntityLavaBlock(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.STONE);
        this.setCollision(true);
        isFly = true;
        noClip = !noClip;
        setTeleportLock(true);
        setJumpName("일반 블럭");
    }

    @Override
    public String getCustomNameTag() {
        return getJumpName()+" 고정:"+canTeleportLock()+"난이도:"+getDifficulty();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(WIDTH, 0F);
        dataManager.register(HEIGHT, 0F);
        dataManager.register(DEB_MOVE, false);
        dataManager.register(IS_BOAT, false);
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
        dataCopy(lavaBlock, x,y,z);
        if(isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        lavaBlock.setWidth(getWidth());
        lavaBlock.setHeight(getHeight());
        lavaBlock.updateSize();
        return lavaBlock;
    }

    @Override
    public String getText() {
        return "일반 블럭입니다. 스패너를 들고 우클릭 하면 블럭을 밟았을 때 블럭이 천천히 내려가게 할 수 있습니다.";
    }

    @Override
    public void teleportEnd() {
        super.teleportEnd();
        if(getScaleX() != 1 || getScaleZ() != 1){
            this.setHeight(1);
            this.setWidth(0.5F);
            updateSize();
        }
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!canTeleportLock() && !dataManager.get(IS_BOAT)) {
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
                if(LoPre2.compare(posY, getSpawnY()) == -1) {
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
            updateSize();
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
    }

    public void setHeight(float height){
        dataManager.set(HEIGHT, height);
    }

    //width 랑 height 가 0이 아닐 때만 사이즈가 설정되게 하기 위해서 있음
    public void updateSize(){
        if(getWidth() != 0 && getHeight() != 0 && (width != getWidth() || height != getHeight())){
            this.setSize(getWidth(), getHeight());
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("downSpeed",downSpeed);
        compound.setFloat("widthl", getWidth());
        compound.setFloat("heightl", getHeight());
        compound.setBoolean("deb", dataManager.get(DEB_MOVE));
        compound.setBoolean("is boat",dataManager.get(IS_BOAT));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setWidth(compound.getFloat("widthl"));
        setHeight(compound.getFloat("heightl"));
        updateSize();
        downSpeed = 0.005;
        dataManager.set(DEB_MOVE, compound.getBoolean("deb"));
        dataManager.set(IS_BOAT, compound.getBoolean("is boat"));
    }

    public void setBoatBlock(){
        dataManager.set(IS_BOAT, true);
    }
    public boolean isBoatBlock(){
        return dataManager.get(IS_BOAT);
    }
}
