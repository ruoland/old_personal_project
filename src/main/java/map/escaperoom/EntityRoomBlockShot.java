package map.escaperoom;

import map.lopre2.EntityPreBlock;
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
import olib.api.Direction;
import olib.api.PosHelper;
import olib.api.WorldAPI;

public class EntityRoomBlockShot extends EntityPreBlock {
    private static final DataParameter<Integer> THROW_TIME = EntityDataManager.createKey(EntityRoomBlockShot.class, DataSerializers.VARINT);
    private PosHelper posHelper = new PosHelper(this);
    public EntityRoomBlockShot(World worldIn) {
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
        if(getThrowTime() > 60 && WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getDistanceToEntity(this) < 5){
            dataManager.set(THROW_TIME, 0);
            EntityRoomBlock arrow = new EntityRoomBlock(worldObj);
            arrow.setPosition(posHelper.getX(Direction.FORWARD, 1, true),posY+1,posHelper.getZ(Direction.FORWARD, 1, true));
            if(isServerWorld())
            worldObj.spawnEntityInWorld(arrow);
            arrow.setBlock(Blocks.EMERALD_BLOCK);
        }
    }


    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomBlockShot lavaBlock = new EntityRoomBlockShot(worldObj);
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
