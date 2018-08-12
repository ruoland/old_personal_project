package ruo.map.lopre2.dummy;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import ruo.map.lopre2.jump2.EntityBigBlock;
import ruo.minigame.api.WorldAPI;

import java.util.List;

public class EntityBigBlockRe extends EntityPreBlock {
    private static final DataParameter<Boolean> ISFALLING = EntityDataManager.<Boolean>createKey(EntityBigBlock.class,
            DataSerializers.BOOLEAN);

    public EntityBigBlockRe(World world) {
        super(world);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
        this.setScale(3, 1, 3);
        this.setSize(3, 1);
        this.setLock(true);
        this.noClip = !noClip;

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ISFALLING, false);
    }

    public void setFalling(boolean is) {
        dataManager.set(ISFALLING, is);
    }

    public boolean isFalling() {
        return dataManager.get(ISFALLING).booleanValue();
    }

    @Override
    public String getCustomNameTag() {
        return this.getClass().getSimpleName().replace("Entity", "") + " 다운상태:" + isLock();
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {

        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    private double downSpeed = 0;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(WorldAPI.equalsHeldItem(Items.APPLE))
            System.out.println(isServerWorld()+ " - " +width+ " - " +Float.compare(width, 3F)+" - "+getRotateX()+ " - "+ Float.compare(getRotateX(), 90));
        if (!isServerWorld() && Float.compare(width, 3F) == 0 && (Float.compare(getRotateX(), 90) != 0 || Float.compare(getRotateY(), 90) != 0
                || Float.compare(getRotateZ(), 90) != 0)) {//0이 동일함, -1은 첫번째 인자가 작음 width 는 서버월드에서 0을 반환하니 주의
            this.setLock(false);
            this.setSize(1, 1);
            this.setFalling(false);
            System.out.println("설정");
        }
        if (!isLock()) {
            setVelocity(0, 0, 0);
            return;
        } else {
            List<EntityPlayer> list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - 2.5, posY, posZ - 2.5, posX + 2.5, posY + 1.5, posZ + 2.5));
            for (EntityPlayer player : list) {
                this.setFalling(true);
            }
            if (isFalling() && isServerWorld()) {
                delay++;
                if(delay > 40) {
                    if (downSpeed == 0)
                        downSpeed = -0.001;
                    downSpeed *= 1.05;
                    if (posY < getSpawnY() - 30) {
                        setFalling(false);
                        setPosition(posX, getSpawnY(), posZ);
                        downSpeed = 0;
                        delay = 0;
                    }
                }
            }
            this.setVelocity(0, downSpeed, 0);
        }
    }

    private int delay = 0;

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isFall", isFalling());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setFalling(compound.getBoolean("isFall"));
    }
}
