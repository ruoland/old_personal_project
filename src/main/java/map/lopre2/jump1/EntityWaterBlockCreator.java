package map.lopre2.jump1;

import oneline.api.EntityAPI;
import oneline.api.WorldAPI;
import net.minecraft.block.material.Material;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import map.lopre2.CommandJB;
import map.lopre2.EntityPreBlock;

public class EntityWaterBlockCreator extends EntityPreBlock {
	public static boolean downLock, downReset;
	private static final DataParameter<Boolean> IS_WATER_BLOCK = EntityDataManager
			.<Boolean>createKey(EntityWaterBlockCreator.class, DataSerializers.BOOLEAN);

	public EntityWaterBlockCreator(World worldObj) {
		super(worldObj);
		this.setBlockMode(Blocks.STONE);
		this.setCollision(true);
		this.isFly = true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(IS_WATER_BLOCK, false);
	}

	@Override
	public EntityWaterBlockCreator spawn(double x, double y, double z) {
		EntityWaterBlockCreator lavaBlock = new EntityWaterBlockCreator(worldObj);
		lavaBlock.setSpawnXYZ(x, y, z);
		lavaBlock.setTeleport(false);
		lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
		this.copyModel(lavaBlock);
		lavaBlock.prevBlock = prevBlock;
		lavaBlock.setBlockMode(getCurrentBlock());
		worldObj.spawnEntityInWorld(lavaBlock);
		lavaBlock.setIsWaterBlock(false);
		lavaBlock.defaultDelay = defaultDelay;
		return lavaBlock;

	}
	public void spawn2(double x, double y, double z) {
		EntityWaterBlockCreator lavaBlock = new EntityWaterBlockCreator(worldObj);
		lavaBlock.setSpawnXYZ(x, y, z);
		lavaBlock.setTeleport(false);
		lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
		this.copyModel(lavaBlock);
		lavaBlock.prevBlock = prevBlock;
		lavaBlock.setBlockMode(getCurrentBlock());
		worldObj.spawnEntityInWorld(lavaBlock);
	}
	public void setIsWaterBlock(boolean isDown) {
		this.getDataManager().set(IS_WATER_BLOCK, isDown);
	}

	public boolean isWaterBlock() {
		return getDataManager().get(IS_WATER_BLOCK);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setTeleport(true);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public String getCustomNameTag() {
		return "LoopDownBlock 기본 딜레이:"+ defaultDelay;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if(CommandJB.isDebMode) {
			if (hand == EnumHand.MAIN_HAND && isServerWorld() && stack.getItem() == Items.APPLE) {
				double x = posX + EntityAPI.lookX(player, ++xx);
				double z = posZ + EntityAPI.lookZ(player, ++zz);
				spawn2(x, posY, z);
			}
		}
		return super.processInteract(player, hand, stack);
	}
	
	private double prevX, prevY, prevZ;
	private int defaultDelay = 70, delay = 70, posCheckDelay = 40;
	private int xx = 0, zz = 0;

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (WorldAPI.getPlayer() != null && !isTeleport() && WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 90) {
			if (isServerWorld() && delay > 0)
				delay--;
			if(downReset)
				delay = defaultDelay;
			if (!isWaterBlock()) {
				if (delay == 0) {
					spawn(posX, posY - 1.3, posZ).setIsWaterBlock(true);
					delay = defaultDelay;
					System.out.println(delay+ " - "+defaultDelay);
				}
			}
			if (isWaterBlock()) {
				if(posCheckDelay > 0)
					posCheckDelay --;
				
				if (!downLock) {// /jb downlock 명령어 입력시 블럭을 멈춤
					if (!this.worldObj.handleMaterialAcceleration(
							this.getEntityBoundingBox().expand(0.5D, -0.4000000059604645D, 0.5D), Material.WATER,
							this) || (posCheckDelay == 0 && WorldAPI.checkPos(this, prevX, prevY, prevZ))) {
						this.setDead();
					} else {
						motionX = 0;
						motionY = -0.07;
						motionZ = 0;
					}
				} else
					motionY = 0;
				if(posCheckDelay == 0) {
					prevX = posX;
					prevY = posY;
					prevZ = posZ;
					posCheckDelay = 40;
				}
			}
		}
	}

	public void setDefaultDelay(int defaultDelay) {
		this.defaultDelay = defaultDelay;
	}

	public int getDefaultDelay() {
		return defaultDelay;
	}

	@Override
	public boolean handleWaterMovement() {
		return false;
	}

	public void writeEntityToNBT(net.minecraft.nbt.NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("noClip", noClip);
		compound.setBoolean("ISDOWN", isWaterBlock());
		compound.setInteger("defaultDelay", defaultDelay);
		compound.setInteger("delay", delay);
	};

	public void readEntityFromNBT(net.minecraft.nbt.NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		noClip = compound.getBoolean("noClip");
		setIsWaterBlock(compound.getBoolean("ISDOWN"));
		defaultDelay = compound.getInteger("defaultDelay");
		delay = compound.getInteger("delay");
		if(defaultDelay == 0)
			defaultDelay = 70;
	};
}
