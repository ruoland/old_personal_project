package map.escaperoom;

import map.lopre2.EntityPreBlock;
import oneline.api.Direction;
import oneline.api.PosHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityRoomBlockArrow extends EntityPreBlock {
    private static final DataParameter<Integer> THROW_TIME = EntityDataManager.createKey(EntityRoomBlockArrow.class, DataSerializers.VARINT);
    private PosHelper posHelper = new PosHelper(this);
    public EntityRoomBlockArrow(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.DISPENSER);
        setJumpName("퍼즐 블럭");
        setTeleportLock(true);
        this.setRotate(0,0,180);
        isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(THROW_TIME, 0);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            if (player.isSneaking()) {
                setTeleport(true);
            }
        }
        return super.processInteract(player, hand, stack);
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        addThrowTime();
        if(getThrowTime() > 10){
            dataManager.set(THROW_TIME, 0);
            EntityArrow arrow = new EntityTippedArrow(worldObj);
            arrow.setPosition(posHelper.getX(Direction.FORWARD, 1, true),posY+1,posHelper.getZ(Direction.FORWARD, 1, true));
            arrow.setAim(this, rotationPitch, rotationYaw, 0, 1, 1 );
            arrow.setNoGravity(true);
            if(isServerWorld())
            worldObj.spawnEntityInWorld(arrow);
            NBTTagCompound tagCompound = new NBTTagCompound();
            arrow.writeEntityToNBT(tagCompound);
            tagCompound.setShort("life", (short)1100);
            arrow.readEntityFromNBT(tagCompound);
            arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
        }
    }


    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomBlockArrow lavaBlock = new EntityRoomBlockArrow(worldObj);
        dataCopy(lavaBlock, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        lavaBlock.setBlock(getCurrentBlock());
        return lavaBlock;
    }

    public void addThrowTime() {
        dataManager.set(THROW_TIME, dataManager.get(THROW_TIME) + 1);
    }

    public int getThrowTime() {
        return dataManager.get(THROW_TIME);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("delay", getThrowTime());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(THROW_TIME, compound.getInteger("delay"));

    }

}
