package ruo.map.lopre2.jump1;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;

//
public class EntityInvisibleBlock extends EntityPreBlock {

	public EntityInvisibleBlock(World worldIn) {
		super(worldIn);
		this.setCollision(true);
		setBlockMode(Blocks.STONE);
		this.isFly = true;
		defaultDelay = 20;
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
					currentDelay -= 1;
				}
				else
					currentDelay--;
			}
		}
        if(currentDelay <= 0){
        	this.setInvisible(!isInvisible());
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
		compound.setDouble("spawnX", getSpawnX());
		compound.setDouble("spawnY", getSpawnY());
		compound.setDouble("spawnZ", getSpawnZ());

	}
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setSpawnXYZ(compound.getDouble("spawnX"), compound.getDouble("spawnY"), compound.getDouble("spawnZ"));

	}


}
