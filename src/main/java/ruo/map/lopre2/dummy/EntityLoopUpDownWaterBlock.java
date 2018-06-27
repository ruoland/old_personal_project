package ruo.map.lopre2.dummy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.map.lopre2.EntityLavaBlock;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.WorldAPI;

import java.util.List;


//물 속에서 위 아래로 움직이는 블럭
public class EntityLoopUpDownWaterBlock extends EntityPreBlock {
	public static double ax = EntityLavaBlock.ax;

	public EntityLoopUpDownWaterBlock(World worldIn) {
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
	public EntityLoopUpDownWaterBlock spawn(double x, double y, double z) {
		EntityLoopUpDownWaterBlock lavaBlock = new EntityLoopUpDownWaterBlock(worldObj);
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
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if (Keyboard.isKeyDown(Keyboard.KEY_L) && hand == EnumHand.MAIN_HAND && isServerWorld()) {
			this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());

		}
		return super.processInteract(player, hand, stack);
	}
	private int delay = 20;
	@Override
	public void onLivingUpdate() {
		if(isServerWorld() && delay > 0){
			delay--;
		}
		if(inWater && WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 40) {
			ax = EntityLavaBlock.ax;
			if(delay == 0 && posY < getSpawnY() + 1.5) {
				this.addVelocity(0,0.03,0);
			}
			else if(posY >= getSpawnY()){
				this.addVelocity(0,-0.02,0);
				delay = 5;
			}
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
					this.posX - 0.6D, this.posY, this.posZ - 0.6D, this.posX + 0.6D, this.posY + 4, this.posZ + 0.6D));
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if ((entity instanceof EntityPlayer) && !entity.noClip) {
						motionY = 0;
					}
				}
			}
		}
		else
			motionY = 0;

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
