package map.puzzle;

import map.lopre2.EntityPreBlock;
import minigameLib.api.WorldAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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
    private static final DataParameter<String> RUN_COMMAND = EntityDataManager.createKey(EntityPuzzleBlock.class, DataSerializers.STRING);
    private static final DataParameter<Integer> THROW_TIME = EntityDataManager.createKey(EntityPuzzleBlock.class, DataSerializers.VARINT);

    public EntityPuzzleBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
        setJumpName("퍼즐 블럭");
        setTeleportLock(true);
    }

    @Override
    public String getCustomNameTag() {
        return getJumpName()+" "+dataManager.get(RUN_COMMAND);
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {

        if(entityIn instanceof EntityPreBlock)
            setVelocity(0,0,0);
        if (entityIn instanceof EntityPuzzleBlock) {
            EntityPuzzleBlock puzzleBlock = (EntityPuzzleBlock) entityIn;
            System.out.println(isServerWorld()+puzzleBlock.getCommand());

            if (puzzleBlock.getCurrentBlock() == Blocks.WOODEN_BUTTON) {
                System.out.println("나무 버튼과 충돌함"+puzzleBlock.getCommand());
                WorldAPI.command(puzzleBlock.getCommand());
            }
            if (getCurrentBlock() == Blocks.WOODEN_BUTTON) {
                System.out.println("나무 버튼과 충돌함" + entityIn);
            }
            playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 1F, 1);

        }
        return canCollision() ? getEntityBoundingBox() : super.getCollisionBox(entityIn);
    }

    public String getCommand(){
        return dataManager.get(RUN_COMMAND);
    }
    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(THROW_STATE, false);
        dataManager.register(RUN_COMMAND, "");
        dataManager.register(THROW_TIME, 0);
    }


    @Override
    protected void collideWithEntity(Entity entityIn) {
        entityIn.applyEntityCollision(this);
        super.collideWithEntity(entityIn);

    }

    public void setCommand(String com){
        dataManager.set(RUN_COMMAND, com);
    }
    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (player.isSneaking()) {
            setTeleport(true);
        }
        System.out.println(isServerWorld()+dataManager.get(RUN_COMMAND));
        return super.processInteract(player, hand, stack);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isTeleport() && source.getEntity() != null && !source.getEntity().isSneaking()) {
            this.setTeleport(false);
            this.addVelocity(source.getEntity().getLookVec().xCoord * 3, source.getEntity().getLookVec().yCoord * 6, source.getEntity().getLookVec().zCoord * 3);
            dataManager.set(THROW_STATE, true);
            setTeleportLock(false);
        }
        return super.attackEntityFrom(source, amount);
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isTeleport() || getCurrentBlock() == Blocks.WOODEN_BUTTON) {
            isFly = true;
        } else
            isFly = false;
        if (dataManager.get(THROW_STATE)) {
            addThrowTime();
            if (getThrowTime() > 100) {
                teleportSpawnPos();
                setTeleportLock(true);
                isFly = true;
                dataManager.set(THROW_STATE, false);
                dataManager.set(THROW_TIME, 0);
                System.out.println(isServerWorld()+"원래자리로 돌아옴");
            }
        }
    }

    public void addThrowTime(){
        dataManager.set(THROW_TIME, dataManager.get(THROW_TIME)+1);
    }
    public int getThrowTime(){
        return dataManager.get(THROW_TIME);
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
        compound.setInteger("delay", getThrowTime());
        compound.setBoolean("ThrowState", dataManager.get(THROW_STATE));
        compound.setString("Command", dataManager.get(RUN_COMMAND));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(THROW_TIME,compound.getInteger("delay"));
        dataManager.set(THROW_STATE, compound.getBoolean("ThrowState"));
        dataManager.set(RUN_COMMAND, compound.getString("Command"));

    }
}
