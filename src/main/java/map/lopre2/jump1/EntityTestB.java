package map.lopre2.jump1;

import map.lopre2.EntityPreBlock;
import map.lopre2.LoPre2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import oneline.map.EntityDefaultNPC;

import java.util.List;

public class EntityTestB extends EntityPreBlock {
    private static final DataParameter<Float> WIDTH = EntityDataManager.createKey(EntityTestB.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> HEIGHT = EntityDataManager.createKey(EntityTestB.class, DataSerializers.FLOAT);
    public EntityTestB(World worldIn) {
        super(worldIn);
        setSize(1, 1);
        this.setBlockMode(Blocks.STONE);
        setCollision(true);
        isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(WIDTH, 0F);
        dataManager.register(HEIGHT, 0F);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
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
                    System.out.println("올라가는 중"+(posY - getSpawnY()));
                    motionZ= 0;
                    motionY = 0.02;
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
                motionY = -0.02;
                System.out.println("내려가는 중");
                motionX = 0;
            }
    }


    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityTestB lavaBlock = new EntityTestB(worldObj);
        dataCopy(lavaBlock, x,y,z);
        if(isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        lavaBlock.setWidth(getWidth());
        lavaBlock.setHeight(getHeight());
        lavaBlock.updateSize();
        return lavaBlock;
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
        if(getWidth() != 0 && getHeight() != 0 && getWidth() != 1 && getHeight() != 1 && (width != getWidth() || height != getHeight())){
            this.setSize(getWidth(), getHeight());
            System.out.println("사이즈 업데이트 됨"+getWidth() + " - "+getHeight());
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("widthl", getWidth());
        System.out.println("저장한 값"+isServerWorld()+compound.getFloat("widthl"));

        compound.setFloat("heightl", getHeight());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        System.out.println("읽은 값"+isServerWorld()+compound.getFloat("widthl"));
        setWidth(compound.getFloat("widthl"));
        setHeight(compound.getFloat("heightl"));
        updateSize();
    }
}
