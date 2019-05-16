package olib.effect;

import cmplus.deb.DebAPI;
import olib.api.WorldAPI;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public abstract class Move {
	public EntityLiving mob;
	private boolean isFly = false;
	protected EntityLivingBase target;
	protected double targetX, targetY, targetZ, speed = 1D, xyzDistance = 0;
	protected int entitydistance = 3, movecount;
	private final Move instance;
	public boolean moveLoop;
	private String customName;
	protected double prevTargetX, prevTargetY, prevTargetZ;

	public Move(EntityLiving mob, double targetX, double targetY, double targetZ, boolean loop, double speed) {
		this.mob = mob;
		this.setPosition(targetX, targetY, targetZ);
		this.instance = this;
		this.targetX = WorldAPI.cut(targetX);
		this.targetY = WorldAPI.cut(targetY);
		this.targetZ = WorldAPI.cut(targetZ);
		this.speed = speed;
        mob.tasks.addTask(4, new EntityAIOpenDoor(mob, true));
	}

	public Move(EntityLiving mob, double targetX, double targetY, double targetZ, boolean loop) {
		this(mob,targetX,targetY,targetZ,loop,1);
	}
	public Move(EntityLiving mob, BlockPos pos, boolean loop) {
		this(mob,pos.getX(), pos.getY(), pos.getZ(),loop,1);
	}
	public Move(EntityLiving mob, EntityLivingBase mob2, boolean loop, int distance) {
		this(mob, mob2.posX, mob2.posY, mob2.posZ, loop, 1);
		this.entitydistance = distance;
		this.target = mob2;
		this.moveLoop = loop;
	}

	private void move(int tickCount, EntityLiving mob, double x, double y, double z) {
		if(isFly()){
			mob.setVelocity((targetX - mob.posX) / 20, (targetY - mob.posY) / 20, (targetZ - mob.posZ) / 20);
		}
		else if (mob.getNavigator().noPath() || tickCount % 20 == 0) {
			PathNavigateGround path = (PathNavigateGround) mob.getNavigator();
			path.setEnterDoors(true);
			path.setBreakDoors(true);
			path.tryMoveToXYZ(x, y, z, speed);
			moveOld();
		}
	}

	public boolean moveToEntity() {
		if (mob == null || mob.isDead || target == null || target.isDead)
			return false;
		mob.getLookHelper().setLookPosition(target.posX, target.posY+mob.height, target.posZ, (float) mob.getHorizontalFaceSpeed(),
				(float) mob.getVerticalFaceSpeed());

		if (mob.getDistance(target.posX, target.posY, target.posZ) > entitydistance) {
			if(isFly()){
				mob.setVelocity((targetX - mob.posX) / 20, (targetY - mob.posY) / 20, (targetZ - mob.posZ) / 20);
			}

			else if (mob.getNavigator().noPath()) {
				PathNavigateGround path = (PathNavigateGround) mob.getNavigator();
				path.setEnterDoors(true);
				path.setBreakDoors(true);
				path.tryMoveToEntityLiving(target, speed);
			}
			//mob.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, speed);
		} else {
			complete();
			movecount++;
			return moveLoop;
		}
		return true;
	}

	public boolean moveToXYZ() {
		if (mob == null || mob.isDead) {
			DebAPI.msgMove(mob + "이 죽었거나("+(mob.isDead)+") 없습니다.");
			return false;
		}
		distanceCheck();
		TickRegister.register(new AbstractTick(getCustomName(), Type.SERVER, 1, true) {
			@Override
			public boolean stopCondition() {
				if (mob == null || mob.isDead) {
					DebAPI.msgMove(mob + "이 죽었거나("+(mob.isDead)+") 없습니다.");
				}
				return mob == null || mob.isDead;
			}
			@Override
			public void run(Type type) {
				if(absRunCount % 40 == 0)
					DebAPI.msgMove(mob.getName() + "이"+" "+targetX+ " "+targetY+ " "+targetZ+"로 이동 중입니다. (이 메세지는 40틱마다 나옵니다)");
				move(absRunCount, mob, targetX, targetY, targetZ);
				if (WorldAPI.checkPos(mob, targetX, targetY, targetZ) || getDistance() < xyzDistance) {
					complete();
					distanceReset();
					if (WorldAPI.checkPos(mob, targetX, targetY, targetZ) || getDistance() < xyzDistance) {// 목표 좌표가 변한 경우를 대비해서 한번더 검사
						moveLoop = false;
						DebAPI.msgMove(mob.getName() + "이 목표 변하지 않고 목표지점에 도달함");
						mob.getNavigator().clearPathEntity();
					} else {
						move(absRunCount, mob, targetX, targetY, targetZ);
						DebAPI.msgMove(mob.getName() + "이 목표가 변했으므로 새로 갱신됐음. 갱신 횟수" + movecount);
						movecount++;
					}
				}
			}
		});
		mob.getLookHelper().setLookPosition(targetX, targetY, targetZ, (float) mob.getHorizontalFaceSpeed(),
				(float) mob.getVerticalFaceSpeed());
		return false;
	}

	double prevDistance;
	int distranceTick = 0;

	/**
	 * 목표 지점이랑 멀리 있는 경우 텔레포트 됨
	 */
	public void distanceCheck() {
		distranceTick++;
		if (prevDistance == 0)
			prevDistance = mob.getDistance(targetX, targetY, targetZ);

		if (distranceTick >= 100) {
			if (doub(mob.getDistance(targetX, targetY, targetZ), prevDistance, 2.0)//전에 저장한 거리와 현재 거리와 별차이가 없는 경우
					|| prevDistance == mob.getDistance(targetX, targetY, targetZ)) {//아니면 똑같은 경우
				mob.setPosition(targetX, targetY, targetZ);
			}
			distanceReset();
		}
	}


	public void moveStart() {
		if (target != null) {
			TickRegister.register(new AbstractTick(mob.getUniqueID().toString(), Type.SERVER,1, true) {
				@Override
				public void run(Type type) {
					if(movecount % 3 == 0)
					{
						prevTargetX = target.posX;
						prevTargetY = target.posY;
						prevTargetZ = target.posZ;
					}
					moveLoop = moveToEntity();

				}
			});
		} else
			moveToXYZ();
		System.out.println("이동 속도 "+speed);
	}
	
	public Move setCustomName(String name) {
		customName = name;
		return this;
	}

	public boolean isFly() {
		return isFly;
	}

	public void setFly(boolean fly) {
		isFly = fly;
	}

	public String getCustomName() {
		if(customName == null) {
			customName = mob.getName() + "-MOVE";
		}
		return customName;
	}
	/**
	 * args1에서 +-minus 한 값에 args2가 있는 경우
	 */
	public static boolean doub(double args1, double args2, double minus) {
		boolean dou = args1 <= args2 + minus && args1 >= args2 - minus;
		return dou;
	}

	public void distanceReset() {
		distranceTick = 0;
		prevDistance = mob.getDistance(targetX, targetY, targetZ);
	}

	public double getDistance() {
		return mob.getDistance(targetX, targetY, targetZ);
	}
	public Move setDistance(double dis) {
		xyzDistance = dis;
		return this;
	}
	public void setTarget(EntityLivingBase target) {
		this.target = target;
	}
	public void setPosition(double targetX, double targetY, double targetZ) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
		target = null;
	}
	public void setPosition(BlockPos pos) {
		this.targetX = pos.getX();
		this.targetY = pos.getY();
		this.targetZ = pos.getZ();
		target = null;
	}
	public Move setSpeed(double speed) {
		this.speed = speed;
		return this;
	}
	public abstract void complete();
	

	public void moveOld() {
		mob.getMoveHelper().setMoveTo(targetX, targetY, targetZ, speed);
		mob.getLookHelper().setLookPosition(targetX, targetY+mob.height, targetZ, (float) mob.getHorizontalFaceSpeed(),
				(float) mob.getVerticalFaceSpeed());
	}
}