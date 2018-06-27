package ruo.map.lopre2.dummy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.Move;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import java.util.ArrayList;
import java.util.List;

public class EntityDummyGuardLoop extends EntityDefaultNPC {
	public static ArrayList<EntityDummyGuardLoop> guardList = new ArrayList<EntityDummyGuardLoop>();
	private static final DataParameter<Boolean> ISROTATE = EntityDataManager.<Boolean>createKey(EntityDummyGuardLoop.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> ISCHASE = EntityDataManager.<Boolean>createKey(EntityDummyGuardLoop.class,
			DataSerializers.BOOLEAN);
	protected Vec3d[] pos;
	protected AxisAlignedBB aabb;
	protected float guardTick, prevYaw;// guardYaw는
	private BlockPos startPos, endPos;

	public EntityDummyGuardLoop(World worldIn) {
		super(worldIn);
		this.setHealth(10000);
		this.setAlwaysRenderNameTag(true);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISCHASE, false);
		this.dataManager.register(ISROTATE, true);
	}


	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (isServerWorld() && WorldAPI.getPlayer() != null) {
			getRotationYaw(false);
			if (canChase()) {
				if (!isChase() && !isSleep()) {
					find();
				}
				if (isChase()) {
					if (WorldAPI.getPlayerMP().interactionManager.getGameType() == GameType.SPECTATOR)
						this.stopChase();
					if (getEntitySenses().canSee(WorldAPI.getPlayer())) {
						findPlayerTick = 0;
						prevX = WorldAPI.x();
						prevY = WorldAPI.y();
						prevZ = WorldAPI.z();
					} else if (findPlayerTick == 0) {
						System.out.println("플레이어가 보이지 않아 마지막으로 보였던 장소로 이동중.");
						EntityAPI.move(new Move(this, prevX, prevY, prevZ, false) {
							public void complete() {
								getRotationYaw(true);
								find();
							};
						}.setDistance(1));
						findPlayerTick++;
					}
					if (EntityAPI.getDistance(this) > 360 || findPlayerTick == 60) {
						findPlayerTick = 0;
						stopChase();
					}
					if (findPlayerTick >= 1)
						findPlayerTick++;
					return;
				}
			}
			if (!isChase() && !EntityAPI.isMove(this)) {
				rotation();
			}

		}
		if (!this.guardList.contains(this)) {
			guardList.add(this);
		}

	}

	public void rotation() {
		if (isRotate()) {
			guardTick++;
			if (guardTick == 60) {
				prevYaw = rotationYawHead + 90;
				rotationYawHead = prevYaw;
				renderYawOffset = prevYaw;
				guardTick = 0;
			}
		}
	}
	public void find() {
		if (aabb == null)
			return;
		List<EntityPlayer> pigList = EntityAPI.getEntity(worldObj, aabb, EntityPlayer.class);
		for (EntityPlayer pig : pigList) {
			if (getEntitySenses().canSee(pig)) {
				startChase(pig);
			}
		}
	}

	public void startChase(EntityPlayer pig) {
		if (defaultRotate || isRotate())
			this.setRotate(false);
		EntityAPI.move(new Move(this, pig, true, 1) {
			public void complete() {
				pig.attackEntityFrom(DamageSource.generic, 5);
				if (pig.isDead) {
					stopChase();
					moveLoop = false;
				}
			};
		}.setSpeed(1.5));
		setChase(true);

	}

	public void stopChase() {
		if (EntityAPI.isMove(this))
			EntityAPI.removeMove(this);
		EntityAPI.removeLook(this);
		if (defaultRotate)
			setRotate(true);
		setChase(false);
		findPlayerTick = 0;
		System.out.println("추적 중단함");
		if (startPos == null) {
			EntityAPI.move(new Move(this, getSpawnPos(), false) {
				public void complete() {
					System.out.println("추적 중단후 원래 위치로 돌아옴");
				};
			});
		} else
			startExplorePos();

	}

	public void setExplorePos(BlockPos startPos, BlockPos endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
	}

	public void startExplorePos() {
		EntityAPI.move(new Move(this, startPos, false) {
			@Override
			public void complete() {
				if (movecount % 2 == 0) {
					setPosition(startPos);
				} else
					setPosition(endPos);
			}
		});
	}

	protected Vec3d[] getRotationYaw(boolean forceUpdate) {
		if ((!isChase() && prevYaw != rotationYawHead) || pos == null || aabb == null || forceUpdate) {
			prevYaw = rotationYawHead;
			pos = new Vec3d[] { getVectorForRotation(0, (float) (rotationYawHead - 70), 8),
					getVectorForRotation2(0, (float) (rotationYawHead + 50), 23) };
			aabb = new AxisAlignedBB(pos[0], pos[1]);
		}
		return pos;
	}
	
	public boolean canExplore() {
		return startPos != null;
	}

	private double prevX, prevY, prevZ;// 마지막으로 플레이어를 본 좌표
	protected int findPlayerTick = 0, playerRun = 0, sleepCount;
	private boolean sleepPlayerJump;
	protected boolean defaultRotate;

	public boolean isDefaultRotate() {
		return defaultRotate;
	}

	public void setDefaultRotate(boolean defaultRotate) {
		this.defaultRotate = defaultRotate;
	}

	public void setSleepCount(int sleepCount) {
		this.sleepCount = sleepCount;
		if (sleepCount == 0)
			playerRun = 0;
	}


	public boolean canChase() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (isSleep()) {
			playerRun += 1000;
			System.out.println("플레이어가 공격함" + playerRun);
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("isRotate", isRotate());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setRotate(compound.getBoolean("isRotate"));
	}

	/**
	 * Creates a Vec3 using the pitch and yaw of the entities rotation.
	 */
	protected final Vec3d getVectorForRotation(float pitch, float yaw, int distance) {
		double f = Math.cos(-yaw * 0.017453292F - (float) Math.PI);
		double f1 = Math.sin(-yaw * 0.017453292F - (float) Math.PI);
		double f2 = -Math.cos(-pitch * 0.017453292F);
		double f3 = Math.sin(-pitch * 0.017453292F);
		double lookX = (double) (f1 * f2);
		double lookY = (double) f3;
		double lookZ = (double) (f * f2);
		double xCoord = lookX * distance + posX;
		double yCoord = lookY * 1 + posY;
		double zCoord = lookZ * distance + posZ;
		return new Vec3d(xCoord, yCoord, zCoord);
	}

	protected final Vec3d getVectorForRotation2(float pitch, float yaw, int distance) {
		double f = Math.cos(-yaw * 0.017453292F - (float) Math.PI);
		double f1 = Math.sin(-yaw * 0.017453292F - (float) Math.PI);
		double f2 = -Math.cos(-pitch * 0.017453292F);
		double f3 = Math.sin(-pitch * 0.017453292F);
		double lookX = (double) (f1 * f2);
		double lookY = (double) f3;
		double lookZ = (double) (f * f2);
		double xCoord = lookX * distance + posX + getLookVec().xCoord * 5;
		double yCoord = lookY * 1 + posY + 3;
		double zCoord = lookZ * distance + posZ + getLookVec().zCoord * 5;
		return new Vec3d(xCoord, yCoord, zCoord);
	}

	/**
	 * Gets the distance to the position.
	 */
	public double getDistance(double x, double y, double z) {
		double d0 = this.posX - x;
		double d1 = this.posY - y;
		double d2 = this.posZ - z;
		return (double) MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}

	public void setRotate(boolean b) {
		getDataManager().set(ISROTATE, b);
	}

	@Override
	public void setSleep(boolean is) {
		super.setSleep(is);
		if (is)
			setRotate(false);
	}

	public void setChase(boolean b) {
		getDataManager().set(ISCHASE, b);
	}

	public boolean isChase() {
		return getDataManager().get(ISCHASE);
	}

	public boolean isRotate() {
		return getDataManager().get(ISROTATE);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
			this.setSleepRotate(getSleepRotate() + 90F);
			System.out.println(getSleepRotate());
			if (getModel() == TypeModel.ZOMBIE) {
				setModel(TypeModel.CREEPER);
			}
			if (getModel() == TypeModel.CREEPER) {
				setModel(TypeModel.ENDERMAN);
			}
			if (getModel() == TypeModel.ENDERMAN) {
				setModel(TypeModel.ZOMBIE);
			}
		}
		return super.processInteract(player, hand, stack);
	}

}
