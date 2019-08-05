package map.lopre2.jump3;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import olib.api.Direction;
import olib.api.PosHelper;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;

import javax.annotation.Nullable;

public class EntityMoSkeleton extends EntitySkeleton {
    private PosHelper posHelper = new PosHelper(this);
    private static final DataParameter<Integer> ARROW_TICK = EntityDataManager.createKey(EntityMoSkeleton.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FACING = EntityDataManager.createKey(EntityMoSkeleton.class, DataSerializers.VARINT);

    public EntityMoSkeleton(World worldIn) {
        super(worldIn);
        setNoAI(true);

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ARROW_TICK, 40);
        dataManager.register(FACING, 0);
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, @Nullable ItemStack stack, EnumHand hand) {
        return super.applyPlayerInteraction(player, vec, stack, hand);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        System.out.println(dataManager.get(FACING));
        if (hand == EnumHand.MAIN_HAND) {
            dataManager.set(FACING, dataManager.get(FACING) < 4 ? dataManager.get(FACING) + 1 : 0);
            System.out.println(dataManager.get(FACING));
        }
        return super.processInteract(player, hand, stack);
    }

    public int getArrowTick() {
        return dataManager.get(ARROW_TICK);
    }

    public void setArrowTick(int arrowTick) {
        dataManager.set(ARROW_TICK, arrowTick);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        setPositionAndRotation(posX, posY, posZ, dataManager.get(FACING) * 90, rotationPitch);
        renderYawOffset = dataManager.get(FACING) * 90;
        rotationYawHead = dataManager.get(FACING) * 90;
        ;
        setPositionAndUpdate(posX, posY, posZ);

        setRotation(dataManager.get(FACING) * 90, 0);
        if (getArrowTick() >= 0) {
            setArrowTick(getArrowTick() - 1);
            if (getArrowTick() <= 0) {
                setSwingingArms(true);
                EntityArrow arrow = new EntityTippedArrow(worldObj);
                arrow.setPosition(posHelper.getX(Direction.FORWARD, 1, true), posY + 1.2, posHelper.getZ(Direction.FORWARD, 1, true));
                arrow.setAim(this, rotationPitch, rotationYaw, 0, 1, 1);
                arrow.setNoGravity(true);
                arrow.setDamage(arrow.getDamage());
                worldObj.spawnEntityInWorld(arrow);
                NBTTagCompound tagCompound = new NBTTagCompound();
                arrow.writeEntityToNBT(tagCompound);
                tagCompound.setShort("life", (short) 1124);
                arrow.readEntityFromNBT(tagCompound);
                arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                setArrowTick(40);
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("arrowTick", getArrowTick());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setArrowTick(compound.getInteger("arrowTick"));
    }
}
