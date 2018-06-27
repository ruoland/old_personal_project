package ruo.map.lopre2;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.minigame.api.WorldAPI;

//물에 흘러가는 블럭
public class EntityWaterFlowBlock extends EntityPreBlock {
	public static double ax = EntityLavaBlock.ax;

	public EntityWaterFlowBlock(World worldIn) {
		super(worldIn);
		this.setCollision(true);
		setBlockMode(Blocks.STONE);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		setSpawnXYZ(posX, posY, posZ);
		this.setTeleport(true);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if (Keyboard.isKeyDown(Keyboard.KEY_L) && hand == EnumHand.MAIN_HAND && isServerWorld()) {//우클릭시 원래 위치로 되돌림 6월 21일자
			this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
		}
		return super.processInteract(player, hand, stack);
	}
	private double prevX, prevZ;
	private int delay = 20, waterDelay = 0;
	@Override
	public EntityWaterFlowBlock spawn(double x, double y, double z) {
		EntityWaterFlowBlock lavaBlock = new EntityWaterFlowBlock(worldObj);
		lavaBlock.setCanFalling(canFalling());
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
	@Override
	public void onLivingUpdate() {
		if(isServerWorld() && delay > 0){
			delay--;
			waterDelay--;
		}
		if(WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 30) {
			if(!inWater && waterDelay == 0) {
				waterDelay = 40;
			}
			if ((!inWater && waterDelay == 1) || (posX == prevX && posZ == prevZ))
				this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
			if(!isTeleport() && delay == 0){
				prevX = posX;
				prevZ = posZ;
				delay = 20;
			}
			this.motionY = -0.008;
		}
		super.onLivingUpdate();
	}

	@Override
	public boolean canTeleportLock() {
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
