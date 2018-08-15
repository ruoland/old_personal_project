package ruo.map.lopre2.jump2;

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
import ruo.map.lopre2.EntityPreBlock;

import java.util.List;

public class EntityBigBlock extends EntityPreBlock {
    private static final DataParameter<Boolean> ISFALLING = EntityDataManager.<Boolean>createKey(EntityBigBlock.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DEFAULT_DELAY = EntityDataManager.<Integer>createKey(EntityBigBlock.class,
            DataSerializers.VARINT);

    public EntityBigBlock(World world) {
        super(world);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
        this.setScale(3, 1, 3);
        this.setSize(3, 1);
        this.setLock(false);
        this.noClip = !noClip;
    }

    @Override
    public EntityBigBlock spawn(double x, double y, double z) {
        EntityBigBlock lavaBlock = new EntityBigBlock(worldObj);
        lavaBlock.setLock(isLock());
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
        lavaBlock.setBlockMode(getCurrentBlock());
        this.copyModel(lavaBlock);
        lavaBlock.setRotate(getRotateX(), getRotateY(), getRotateZ());
        lavaBlock.setSize(width,height);
        worldObj.spawnEntityInWorld(lavaBlock);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
        if(getBlockMetadata() != 0)
            lavaBlock.setBlockMetadata(getBlockMetadata());
        System.out.println("스폰 좌표 " + lavaBlock.getSpawnX() + ", " + lavaBlock.getSpawnY() + ", " + lavaBlock.getSpawnZ());
        return lavaBlock;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        System.out.println(getRotateX()+ " - "+getRotateY()+" - "+getRotateZ());

        return super.processInteract(player, hand, stack);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ISFALLING, false);
        dataManager.register(DEFAULT_DELAY, 40);
    }
    public void setDefaultDelay(int defaultDelay) {
        dataManager.set(DEFAULT_DELAY, defaultDelay);
    }

    public int getDefaultDelay() {
        return dataManager.get(DEFAULT_DELAY).intValue();
    }

    public void setFalling(boolean is) {
        dataManager.set(ISFALLING, is);
    }

    public boolean isFalling() {
        return dataManager.get(ISFALLING).booleanValue();
    }

    @Override
    public String getCustomNameTag() {
        return "BigBlock " + " 잠금:" + isLock()+ " 딜레이:"+getDefaultDelay();
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

    private double downSpeed = 0;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if ((getCurrentBlock() == Blocks.WOOL && (getBlockMetadata() == 11 || getBlockMetadata() == 5))) {
            this.setLock(true);
            setBlockMetadata(5);
        }else if(getRotateX() == 0 && getRotateY() == 0 && getRotateZ() == 0){
            setLock(false);
        }
        if (getCustomNameTag().indexOf("SmallBlock") != -1) {
            if (getScaleX() == 3) {
                this.setSize(1, 1);
                this.setScale(1, 1, 1);
                this.setBlock(new ItemStack(Blocks.WOOL, 1, 11));
            }
            setLock(false);
        }
        if(getRotateZ() == 0 && getRotateY() == 0 && getRotateX() == 0){
            this.setRotate(0,0,0);
        }
        if(getRotateZ() == 90 && getRotateY() == 90 && getRotateX() == 90){
            this.setRotate(0,0,0);
        }
        if(getRotateZ() == 180 && getRotateY() == 0 && getRotateX() == 0){
            this.setRotate(0,0,0);
        }
        if (!isServerWorld() && Float.compare(width, 3F) == 0 && (Float.compare(getRotateX(), 0) != 0 || Float.compare(getRotateY(), 0) != 0
                || Float.compare(getRotateZ(), 0) != 0)) {//0이 동일함, -1은 첫번째 인자가 작음 width 는 서버월드에서 0을 반환하니 주의
            this.setLock(true);
            this.setSize(1, 1);
            this.setFalling(false);
            System.out.println(getRotateX()+ " - "+getRotateY()+" - "+getRotateZ());
        }
        if (!isServerWorld() && Float.compare(width, 1F) == 0 && (Float.compare(getRotateX(), 0) == 0 && Float.compare(getRotateY(), 0) == 0
                && Float.compare(getRotateZ(), 0) == 0)) {//0이 동일함, -1은 첫번째 인자가 작음 width 는 서버월드에서 0을 반환하니 주의
            this.setLock(false);
            this.setSize(3, 1);
            System.out.println("Rotate가 0임 "+getRotateX()+ " - "+getRotateY()+" - "+getRotateZ());
        }
        if(isLock() && Float.compare(getRotateX(), 0) == 0 && Float.compare(getRotateY(), 0) == 0
                && Float.compare(getRotateZ(), 0) == 0){
            setLock(false);
            System.out.println(isLock()+ " 222- "+getRotateX()+ " - "+getRotateY()+" - "+getRotateZ());
        }
        if ((Float.compare(getRotateX(), 0) != 0 || Float.compare(getRotateY(), 0) != 0
                || Float.compare(getRotateZ(), 0) != 0)) {//0이 동일함, -1은 첫번째 인자가 작음 width 는 서버월드에서 0을 반환하니 주의
            this.setLock(true);
            this.setFalling(false);
        }

        if (isLock()) {
            setVelocity(0, 0, 0);

        } else {
            float size = 2.5F;
            if (getCustomNameTag().indexOf("BigBlock") != -1)
                size = 2.5F;
            if (getCustomNameTag().indexOf("SmallBlock") != -1)
                size = 1F;
            List<EntityPlayer> list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - size, posY + 0.9, posZ - size, posX + size, posY + 1.9, posZ + size));
            for (EntityPlayer player : list) {
                if (player.onGround)
                    this.setFalling(true);
            }
            if (isFalling() && isServerWorld()) {
                delay -= 1;

                if (delay <= 0) {
                    if (downSpeed == 0)
                        downSpeed = -0.005;
                    downSpeed *= 1.2;
                    if (posY < getSpawnY() - 30) {
                        setFalling(false);
                        setPosition(posX, getSpawnY(), posZ);
                        downSpeed  =0;
                        delay = getDefaultDelay();
                    }
                }
            }
            if(downSpeed != 0)
                System.out.println(isLock()+ " - "+getRotateX()+ " - "+getRotateY()+" - "+getRotateZ());

            this.setVelocity(0, downSpeed, 0);
        }
    }

    @Override
    public void teleportEnd() {
        setPosition(posX, getSpawnY(), posZ);
    }
    private int delay = 4;

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isFall", isFalling());
        compound.setInteger("playerDelay", delay);
        compound.setInteger("defaultDelay", getDefaultDelay());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setFalling(compound.getBoolean("isFall"));
        delay = compound.getInteger("playerDelay");
        setDefaultDelay(compound.getInteger("defaultDelay"));
        if(getDefaultDelay() == 30 || getDefaultDelay() == 0)
            setDefaultDelay(40);
    }
}
