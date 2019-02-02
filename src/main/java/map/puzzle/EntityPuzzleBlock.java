package map.puzzle;

import map.lopre2.EntityPreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import java.util.List;

public class EntityPuzzleBlock extends EntityPreBlock {
    private static final DataParameter<Boolean> THROW_STATE = EntityDataManager.createKey(EntityPuzzleBlock.class, DataSerializers.BOOLEAN);

    public EntityPuzzleBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
        setJumpName("퍼즐 블럭");
        setTeleportLock(true);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(THROW_STATE, false);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if (entityIn instanceof EntityPuzzleBlock) {
            EntityPuzzleBlock puzzleBlock = (EntityPuzzleBlock) entityIn;
            if (puzzleBlock.getCurrentBlock() == Blocks.WOODEN_BUTTON) {
                System.out.println("나무 버튼과 충돌함");
            }
            if (getCurrentBlock() == Blocks.WOODEN_BUTTON) {
                System.out.println("나무 버튼과 충돌함"+entityIn);
            }
        }
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if(player.isSneaking()){
            setTeleport(true);
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isTeleport() && source.getEntity() != null &&  !source.getEntity().isSneaking()) {
            this.setTeleport(false);
            this.addVelocity(source.getEntity().getLookVec().xCoord * 4, source.getEntity().getLookVec().yCoord * 7, source.getEntity().getLookVec().zCoord * 4);
            dataManager.set(THROW_STATE, true);
            setTeleportLock(false);
        }
        return super.attackEntityFrom(source, amount);
    }

    private int throwTime;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isTeleport()){
            isFly = true;
        }else
            isFly = false;
        if (dataManager.get(THROW_STATE)) {

            throwTime++;
            if (throwTime > 100) {
                teleportSpawnPos();
                throwTime = 0;
                setTeleportLock(true);
                isFly =true;
            }
        }
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
        EntityPuzzleBlock lavaBlock = new EntityPuzzleBlock(worldObj);
        dataCopy(lavaBlock, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        return lavaBlock;
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("delay",throwTime);
        compound.setBoolean("ThrowState", dataManager.get(THROW_STATE));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        throwTime = compound.getInteger("delay");
        dataManager.set(THROW_STATE, compound.getBoolean("ThrowState"));

    }
}
