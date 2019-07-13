package map.escaperoom;

import map.lopre2.EntityPreBlock;
import olib.api.Direction;
import olib.api.PosHelper;
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
import olib.api.WorldAPI;

public class EntityRoomBlockArrow extends EntityPreBlock {
    private static final DataParameter<Integer> THROW_TIME = EntityDataManager.createKey(EntityRoomBlockArrow.class, DataSerializers.VARINT);
    private PosHelper posHelper = new PosHelper(this);

    public EntityRoomBlockArrow(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.DISPENSER);
        setJumpName("퍼즐 블럭");
        setTeleportLock(true);
        this.setRotate(0, 0, 180);
        isFly = false;
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
            } else
                isFly = !isFly;
            System.out.println(getRotateX() + " - "+getRotateY() + " - "+getRotateZ());
            System.out.println("플라이 " + isFly);
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        addThrowTime();
        if (getThrowTime() > 10 && WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getDistanceToEntity(this) < 10) {
            dataManager.set(THROW_TIME, 0);
            EntityArrow arrow = new EntityTippedArrow(worldObj);

            if (rotationPitch == 90)
                arrow.setPosition(posX, posY + 1, posZ);
            else if (getRotateX() >= 80 && getRotateX() <= 100) {
                arrow.setPosition(posX, posY - 1, posZ);
                rotationPitch = -90;
            }
            else
                arrow.setPosition(posHelper.getX(Direction.FORWARD, 1, true), posY + 0.5, posHelper.getZ(Direction.FORWARD, 1, true));
            arrow.setAim(this, rotationPitch, rotationYaw, 0, 1, 1);
            arrow.setNoGravity(true);
            arrow.setDamage(arrow.getDamage() * 200);
            if (isServerWorld())
                worldObj.spawnEntityInWorld(arrow);
            NBTTagCompound tagCompound = new NBTTagCompound();
            arrow.writeEntityToNBT(tagCompound);
            tagCompound.setShort("life", (short) 1124);
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

    @Override
    public String getText() {
        return "화살을 발사하는 디스펜서 블럭입니다.";
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
        compound.setFloat("pitch", rotationPitch);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(THROW_TIME, compound.getInteger("delay"));
        rotationPitch = compound.getFloat("pitch");
    }

}
