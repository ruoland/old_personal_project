package ruo.map.lopre2;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.minigame.action.ActionEvent;
import ruo.minigame.action.GrabHelper;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

import java.util.List;

//물에 흘러가는 블럭
public class EntityWaterFlowBlock extends EntityPreBlock {
	private EnumFacing facing;//물에 흘러가는 방향. 수동으로 설정해야 함
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
		facing = player.getHorizontalFacing();
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
	boolean isPlayer;//플레이어가 처음 들어가면 초기화함, isPlayer는 플레이어가 있을 때

	@Override
	public void onLivingUpdate() {
		if(isServerWorld() && delay > 0){
			delay--;
			waterDelay--;
		}
		if(WorldAPI.getPlayer() != null) {
			if (!inWater && waterDelay == 0) {
				waterDelay = 40;
			}
			if ((!inWater && waterDelay == 1) || (posX == prevX && posZ == prevZ))
				this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
			if (!isTeleport() && delay == 0) {
				prevX = posX;
				prevZ = posZ;
				delay = 20;
			}
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
					this.posX - 1D, this.posY, this.posZ - 1D, this.posX + 1D, this.posY + 1.8, this.posZ + 1D));
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if (facing != null && (entity instanceof EntityPlayer) && !entity.noClip && (GrabHelper.wallGrab || entity.posY > posY + 0.2)) {
						if (GrabHelper.wallGrab || entity.onGround) {
							double px = EntityAPI.lookX(facing, 0.051), py = motionY / 10, pz =EntityAPI.lookZ(facing, 0.051);
							if (!GrabHelper.wallGrab && !isPlayer) {
								entity.setVelocity(0, 0, 0);
								WorldAPI.getPlayerSP().setVelocity(0, 0, 0);
							}
							if (WorldAPI.getPlayerSP().movementInput.jump) {
								WorldAPI.getPlayerSP().jump();
								WorldAPI.getPlayerMP().jump();
								ActionEvent.forceJump = true;
							}
							WorldAPI.getPlayerSP().moveEntity(px, py, pz);
							WorldAPI.getPlayerMP().moveEntity(px, py, pz);
							isPlayer = true;
							break;
						}
					} else
						isPlayer = false;
				}
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
		if(facing != null)
		compound.setString("facing", facing.getName());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setSpawnXYZ(compound.getDouble("spawnX"), compound.getDouble("spawnY"), compound.getDouble("spawnZ"));
		facing = EnumFacing.byName(compound.getString("facing"));

	}


}
