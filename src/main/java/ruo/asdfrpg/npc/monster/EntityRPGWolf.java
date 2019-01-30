package ruo.asdfrpg.npc.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.Direction;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;

public class EntityRPGWolf extends EntityDefaultNPC {
    private static final DataParameter<String> DIRECTION = EntityDataManager.createKey(EntityRPGWolf.class, DataSerializers.STRING);

    public EntityRPGWolf(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIGroup(this, 1.0D));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityRPGWolf.class}));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DIRECTION, Direction.FORWARD.name());
    }

    public void setDirection(Direction direction) {
        dataManager.set(DIRECTION, direction.name());
    }

    public BlockPos getDirection(EntityLivingBase livingBase) {
        Direction spawnDirection = Direction.valueOf(dataManager.get(DIRECTION));
        BlockPos pos = new BlockPos(EntityAPI.getX(livingBase, spawnDirection, 3, true),livingBase.posY,EntityAPI.getZ(livingBase, spawnDirection, 3, true));

        return pos;
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        {
            EntityRPGWolf wolf = new EntityRPGWolf(worldObj);
            wolf.copyLocationAndAnglesFrom(this);
            worldObj.spawnEntityInWorld(wolf);
            wolf.setDirection(Direction.LEFT);
        }
        {
            EntityRPGWolf wolf = new EntityRPGWolf(worldObj);
            wolf.copyLocationAndAnglesFrom(this);
            worldObj.spawnEntityInWorld(wolf);
            wolf.setDirection(Direction.RIGHT);
        }
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("direction", dataManager.get(DIRECTION));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(DIRECTION, compound.getString("direction"));
    }
}
