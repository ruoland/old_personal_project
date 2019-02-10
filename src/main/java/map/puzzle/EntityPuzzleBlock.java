package map.puzzle;

import map.lopre2.EntityPreBlock;
import map.lopre2.LoPre2;
import map.lopre2.jump2.EntityKnockbackBlock;
import minigameLib.api.RenderAPI;
import minigameLib.api.WorldAPI;
import minigameLib.map.EntityDefaultBlock;
import minigameLib.map.TypeModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import java.util.List;

public class EntityPuzzleBlock extends EntityPreBlock {
    private static final DataParameter<Boolean> THROW_STATE = EntityDataManager.createKey(EntityPuzzleBlock.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> THROW_TIME = EntityDataManager.createKey(EntityPuzzleBlock.class, DataSerializers.VARINT);

    public EntityPuzzleBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.QUARTZ_BLOCK);
        setJumpName("퍼즐 블럭");
        setTeleportLock(true);
        this.setModel(TypeModel.SHAPE_BLOCK);

    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        if (entityIn instanceof EntityPreBlock)
            setVelocity(0, 0, 0);
        if (entityIn instanceof EntityPuzzleBlockButton) {
            EntityPuzzleBlockButton puzzleBlock = (EntityPuzzleBlockButton) entityIn;
            WorldAPI.command(puzzleBlock.getCommand());
            playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 1F, 1);

        }
        return canCollision() ? getEntityBoundingBox() : super.getCollisionBox(entityIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(THROW_STATE, false);
        dataManager.register(THROW_TIME, 0);
    }


    @Override
    protected void collideWithEntity(Entity entityIn) {
        entityIn.applyEntityCollision(this);
        if (entityIn instanceof EntityPreBlock)
            setVelocity(0, 0, 0);
        super.collideWithEntity(entityIn);

    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            if (player.isSneaking()) {
                setTeleport(true);
                this.setTransparency(0.5F);
            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void teleportEnd() {
        super.teleportEnd();
        setTransparency(1F);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isTeleport() && source.getEntity() != null && !source.getEntity().isSneaking()) {
            this.setTeleport(false);
            this.addVelocity(source.getEntity().getLookVec().xCoord * 3, source.getEntity().getLookVec().yCoord * 6, source.getEntity().getLookVec().zCoord * 3);
            dataManager.set(THROW_STATE, true);
            setTeleportLock(false);
            this.setTransparency(1F);
        }
        return super.attackEntityFrom(source, amount);
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        //나무버튼이거나 텔포 중에는 떨어지지 않게 함
        isFly = isTeleport();
        if (dataManager.get(THROW_STATE)) {
            addThrowTime();
            if (getThrowTime() > 100) {
                teleportSpawnPos();
                setTeleportLock(true);
                isFly = true;
                dataManager.set(THROW_STATE, false);
                dataManager.set(THROW_TIME, 0);
                System.out.println(isServerWorld() + "원래자리로 돌아옴");
            }
        }

        if (getCurrentBlock() == Blocks.EMERALD_BLOCK) {
            double knockbackSize = 0.9;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - knockbackSize, this.posY, this.posZ - knockbackSize, this.posX + knockbackSize, this.posY + 1.5, this.posZ + knockbackSize));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if ((entity instanceof EntityPuzzleBlock) && !entity.noClip) {
                        ((EntityLivingBase) entity).knockBack(this, 0.4F, this.posX - entity.posX, this.posZ - entity.posZ);

                    }
                }
            }
        }
    }

    @Override
    public void setBlock(ItemStack stack) {
        super.setBlock(stack);
        setTexture(RenderAPI.getBlockTexture(((ItemBlock)stack.getItem()).block));
    }


    public void addThrowTime() {
        dataManager.set(THROW_TIME, dataManager.get(THROW_TIME) + 1);
    }

    public int getThrowTime() {
        return dataManager.get(THROW_TIME);
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
        lavaBlock.setBlock(getCurrentBlock());
        return lavaBlock;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("delay", getThrowTime());
        compound.setBoolean("ThrowState", dataManager.get(THROW_STATE));
        System.out.println("블럭" + getCurrentBlock() + typeModel + getTexture());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(THROW_TIME, compound.getInteger("delay"));
        dataManager.set(THROW_STATE, compound.getBoolean("ThrowState"));
        System.out.println("RR  블럭" + getCurrentBlock() + typeModel + getTexture());
    }

}
