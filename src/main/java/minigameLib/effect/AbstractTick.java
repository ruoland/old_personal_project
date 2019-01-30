package minigameLib.effect;

import minigameLib.api.WorldAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public abstract class AbstractTick {
	protected String absName;
	protected int absDefTick;// 초기값
	protected int absTick;
	protected int absRunCount;// 런 메서드 실행 횟수
	public boolean absLoop;
	private boolean absPause, isAbsDead;
	public transient Type tickType;
	public transient TickEvent tickEvent;

	public AbstractTick(String name, Type type, int tick, boolean loop) {
		this.absName = name;
		this.tickType = type;
		this.absDefTick = tick;
		this.absTick = tick;
		this.absLoop = loop;
	}

	public AbstractTick(Type type, int tick, boolean loop) {
		this(null, type, tick,loop);
	}

	public AbstractTick(int tick, boolean loop) {
		this(null, Type.SERVER, tick,loop);
	}

	public AbstractTick() {
	}

	/**
	 * 목적 완수까지 남은 틱
	 */
	public int getCurrentTick(){
		return absTick;
	}

	public void stopTick() {
		isAbsDead = true;
	}

	public void pauseTick(boolean pause) {
		this.absPause = pause;
	}

	public boolean stopCondition() {
		return false;
	}

	public boolean equals(String name) {
		return this.absName != null && this.absName.equals(name);
	}

	public boolean isPause() {
		return absPause;
	}

	public boolean isDead() {
		return isAbsDead;
	}

	public boolean subtraction(Type argType) {
		if ((tickType != null && argType != this.tickType)) {
			return false;
		}
		if (stopCondition())
			return true;
		if (absTick > 0) {
			absTick--;
			if (absTick <= 0) {
				run(argType);
				if (tickType != null)
					absRunCount++;
				if (absLoop) {
					absTick = absDefTick;
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public abstract void run(Type type);

	public void run() {
		run(null);
	}

	public static abstract class Position extends AbstractTick {
		public double posX, posY, posZ, distance;
		public EntityLivingBase mob;
		private double[] xArray = new double[0], yArray = new double[0], zArray = new double[0];//인식하는 XYZ를 추가할 수 있음
		private BlockPos startPos, endPos;

		public Position() {
			super(Type.SERVER, 1, true);
		}

		public Position setX(double... x) {
			this.xArray = x;
			return this;
		}

		public Position setY(double... y) {
			this.yArray = y;
			return this;
		}

		public Position setZ(double... z) {
			this.zArray = z;
			return this;
		}

		public Position setPos(BlockPos spos, BlockPos epos) {
			startPos = spos;
			endPos = epos;
			return this;
		}
		public Position setPos(double x, double y, double z, double x2, double y2, double z2) {
			startPos = new BlockPos(x,y,z);
			endPos = new BlockPos(x2,y2,z2);
			return this;
		}
		
		public Position setDistance(double d) {
			this.distance = d;
			return this;
		}
		@Override
		public void run(Type type) {
			if (runCondition()) {
				runPosition();
				absLoop = false;
			}
		}

		@Override
		public boolean stopCondition() {
			if(mob.isDead) {
				if(mob instanceof EntityPlayer) {
					mob = WorldAPI.getPlayer();
					return false;
				}
				System.out.println("몬스터가 죽어 포지션이 취소됨.");
			}
			return super.stopCondition();
		}
		public abstract void runPosition();

		public boolean runCondition() {
			if(startPos != null) {
				return WorldAPI.checkPos(mob,startPos, endPos) || (checkX() && checkY() && checkZ()) || mob.getDistance(posX, posY, posZ) <= distance || (xArray.length > 0 && distance > 0 && mob.getDistance(xArray[0], yArray[0], zArray[0]) <= distance);
			}
			else
				return (checkX() && checkY() && checkZ()) || mob.getDistance(posX, posY, posZ) <= distance || (xArray.length > 0 && distance > 0 && mob.getDistance(xArray[0], yArray[0], zArray[0]) <= distance);
		}

		private boolean checkX() {
			double x2 = mob.posX;
			for (double x : xArray) {
				if(x == x2 || x == -12345 || (int) x == (int) x2 || WorldAPI.round(x) == WorldAPI.round(x2)){
					return true;
				}
			}
			return posX == x2 || posX == -12345 || (int) posX == (int) x2 || WorldAPI.round(posX) == WorldAPI.round(x2);
		}

		private boolean checkY() {
			double y2 = mob.posY;
			for (double y : yArray) {
				if(y == y2 || y == -12345 || (int) y == (int) y2 || WorldAPI.round(y) == WorldAPI.round(y2))
					return true;
			}
			return posY == y2 || posY == -12345 || (int) posY == (int) y2 || WorldAPI.round(posY) == WorldAPI.round(y2);
		}

		private boolean checkZ() {
			double z2 = mob.posZ;
			for (double z : yArray) {
				if(z == z2 || z == -12345 || (int) z == (int) z2 || WorldAPI.round(z) == WorldAPI.round(z2)) {
					return true;
				}
			}
			return posZ == z2 || posZ == -12345 || (int) posZ == (int) z2 || WorldAPI.round(posZ) == WorldAPI.round(z2);
		}
	}

	public static class Command extends AbstractTick.Position {
		String com;
		MinecraftServer server;

		public Command(String command) {
			com = command;
			this.server = FMLCommonHandler.instance().getMinecraftServerInstance();
		}

		@Override
		public void run(Type type) {
			if (com != null)
				server.getCommandManager().executeCommand(server, com);
		}

		@Override
		public void runPosition() {
		}
	}

	public static abstract class BlockXYZ extends AbstractTick {
		public int x, y, z;

		public BlockXYZ() {
		}

		@Override
		public abstract void run(Type type);

		public BlockPos getPos() {
			return new BlockPos(x, y, z);
		}
		public Block getBlock(){
		    return WorldAPI.getWorld().getBlockState(getPos()).getBlock();
        }
	}
}
