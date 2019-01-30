package customclient;

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
}
