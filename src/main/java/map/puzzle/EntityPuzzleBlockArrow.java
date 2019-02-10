package map.puzzle;

import cmplus.cm.v17.CommandClip;
import map.lopre2.EntityPreBlock;
import minigameLib.api.Direction;
import minigameLib.api.PosHelper;
import minigameLib.api.RenderAPI;
import minigameLib.api.WorldAPI;
import minigameLib.map.TypeModel;
import minigameLib.minigame.elytra.ElytraEvent;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityPuzzleBlockArrow extends EntityPreBlock {
    private static final DataParameter<Integer> THROW_TIME = EntityDataManager.createKey(EntityPuzzleBlockArrow.class, DataSerializers.VARINT);
    private PosHelper posHelper = new PosHelper(this);
    public EntityPuzzleBlockArrow(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.DISPENSER);
        setJumpName("퍼즐 블럭");
        setTeleportLock(true);
        this.setRotate(0,0,180);
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
        if(getThrowTime() > 30){
            dataManager.set(THROW_TIME, 0);
            EntityArrow arrow = new EntityTippedArrow(worldObj);
            arrow.setPosition(posHelper.getX(Direction.FORWARD, 1, true),posY,posHelper.getZ(Direction.FORWARD, 1, true));
            arrow.setAim(this, rotationPitch, rotationYaw, 0, 1, 1 );
            if(isServerWorld())
            worldObj.spawnEntityInWorld(arrow);
            arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
        }
    }


    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityPuzzleBlockArrow lavaBlock = new EntityPuzzleBlockArrow(worldObj);
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
