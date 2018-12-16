package ruo.map.lopre2.jump1;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.map.lopre2.jump2.EntityTeleportBlock;

//
public class EntityInvisibleBlock extends EntityPreBlock {
	private static final DataParameter<Boolean> IS_COLLISION = EntityDataManager.createKey(EntityInvisibleBlock.class, DataSerializers.BOOLEAN);
	public EntityInvisibleBlock(World worldIn) {
		super(worldIn);
		this.setCollision(true);
		setBlockMode(Blocks.STONE);
		this.isFly = true;
		defaultDelay = 20;
		setJumpName("투명 블럭");
	}

	@Override
	public String getCustomNameTag() {
		return getJumpName()+" 충돌여부:"+canCollision()+" 난이도:"+getDifficulty();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(IS_COLLISION, false);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setTeleport(true);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public EntityInvisibleBlock spawn(double x, double y, double z) {
		EntityInvisibleBlock lavaBlock = new EntityInvisibleBlock(worldObj);
		lavaBlock.setSpawnXYZ(x, y, z);
		lavaBlock.setTeleport(false);
		lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
		lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ(), 90, 90, 1, false);
		this.copyModel(lavaBlock);
		lavaBlock.prevBlock = prevBlock;
		lavaBlock.setBlockMode(getCurrentBlock());
		worldObj.spawnEntityInWorld(lavaBlock);
		return lavaBlock;
	}
	public int defaultDelay, currentDelay;
	@Override
	public void onLivingUpdate() {
        if(isServerWorld()){
			if(currentDelay >=0 && !isTeleport()) {
				if(isInvisible()){
					currentDelay --;
				}
				else {
					currentDelay--;
				}
			}
		}
        if(currentDelay <= 0){
			setInv(!isInv());
			setInvisible(isInv());
			if(dataManager.get(IS_COLLISION))
			setCollision(!isInv());
        	currentDelay = defaultDelay;
        }
		super.onLivingUpdate();
	}

	@Override
	public boolean handleWaterMovement() {
		return false;
	}
    @Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("IS_COLLISION", dataManager.get(IS_COLLISION));

	}
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		dataManager.set(IS_COLLISION, compound.getBoolean("IS_COLLISION"));
	}


}
