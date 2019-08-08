package map.lopre2.jump3;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import olib.api.WorldAPI;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;

import javax.annotation.Nullable;

public class EntityMoSkeleton extends EntitySkeleton {
    private PosHelper posHelper = new PosHelper(this);
    private static final DataParameter<Integer> DEFAULT_ARROW_TICK = EntityDataManager.createKey(EntityMoSkeleton.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SHOT_ARROW_COUNT = EntityDataManager.createKey(EntityMoSkeleton.class, DataSerializers.VARINT);

    private static final DataParameter<Integer> ARROW_TICK = EntityDataManager.createKey(EntityMoSkeleton.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FACING = EntityDataManager.createKey(EntityMoSkeleton.class, DataSerializers.VARINT);

    public EntityMoSkeleton(World worldIn) {
        super(worldIn);
        setNoAI(true);

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SHOT_ARROW_COUNT, 1);
        dataManager.register(DEFAULT_ARROW_TICK, 40);
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
            if(WorldAPI.equalsItem(stack, Items.ARROW)){
                setShotArrowCount(dataManager.get(SHOT_ARROW_COUNT)+1);
            }else {
                dataManager.set(FACING, dataManager.get(FACING) < 4 ? dataManager.get(FACING) + 1 : 0);
                System.out.println(dataManager.get(FACING));
            }
        }
        return super.processInteract(player, hand, stack);
    }

    public int getArrowTick() {
        return dataManager.get(ARROW_TICK);
    }

    public void setArrowTick(int arrowTick) {
        dataManager.set(ARROW_TICK, arrowTick);
    }

    public void setDefaultArrowTick(int arrowTick) {
        dataManager.set(DEFAULT_ARROW_TICK, arrowTick);
    }
    public void setShotArrowCount(int arrowTick) {
        dataManager.set(SHOT_ARROW_COUNT, arrowTick);
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        setPositionAndRotation(posX, posY, posZ, dataManager.get(FACING) * 90, rotationPitch);
        renderYawOffset = dataManager.get(FACING) * 90;
        rotationYawHead = dataManager.get(FACING) * 90;
        setPositionAndUpdate(posX, posY, posZ);

        setRotation(dataManager.get(FACING) * 90, 0);
        if (getArrowTick() >= 0 && !worldObj.isRemote) {
            setArrowTick(getArrowTick() - 1);
            if (getArrowTick() <= 0) {
                for (int i = 0; i < dataManager.get(SHOT_ARROW_COUNT); i++) {
                    arrowShot(i);
                }
                setArrowTick(dataManager.get(DEFAULT_ARROW_TICK));
            }
        }
    }

    public void arrowShot(int i) {
        setSwingingArms(true);
        EntityArrow arrow = new EntityTippedArrow(worldObj);
        arrow.setPosition(posHelper.getX(Direction.FORWARD, i+1, true), posY + 1.2, posHelper.getZ(Direction.FORWARD, i+1, true));
        arrow.setAim(this, rotationPitch, rotationYaw, 0, 1, 1);
        arrow.setNoGravity(true);
        arrow.setDamage(arrow.getDamage());
        worldObj.spawnEntityInWorld(arrow);
        NBTTagCompound tagCompound = new NBTTagCompound();
        arrow.writeEntityToNBT(tagCompound);
        tagCompound.setShort("life", (short) 1124);
        arrow.readEntityFromNBT(tagCompound);
        arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("arrowTick", getArrowTick());
        compound.setInteger("defaultArrowTick", dataManager.get(DEFAULT_ARROW_TICK));
        compound.setInteger("shotArrowCount", dataManager.get(SHOT_ARROW_COUNT));
        compound.setInteger("facing", dataManager.get(FACING));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setArrowTick(compound.getInteger("arrowTick"));
        dataManager.set(DEFAULT_ARROW_TICK, compound.getInteger("defaultArrowTick"));
        dataManager.set(SHOT_ARROW_COUNT, compound.getInteger("shotArrowCount"));
        if(dataManager.get(SHOT_ARROW_COUNT) == 0)
            dataManager.set(SHOT_ARROW_COUNT, 1);

        if(dataManager.get(DEFAULT_ARROW_TICK) == 0)
            dataManager.set(DEFAULT_ARROW_TICK, 40);
        dataManager.set(FACING, compound.getInteger("facing"));
    }
}
