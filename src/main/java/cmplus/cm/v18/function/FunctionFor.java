package cmplus.cm.v18.function;

import oneline.api.WorldAPI;
import oneline.effect.AbstractTick;
import oneline.effect.TickRegister;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.util.ArrayList;

public class FunctionFor {
	public static ArrayList<FunctionFor> fifList = new ArrayList<FunctionFor>();
	private boolean isStop, isStart;
	private int tick, maxCount;
	private String command;
	private FunctionFor(int tick, String command, int count) {
		this.tick = tick;
		this.command = command;
		maxCount = count;
		runFor();
	}

	public static FunctionFor addFor(int tick, String command, int maxcount) {
		fifList.add(new FunctionFor(tick, command, maxcount));
		return currentFor();
	}
	private void runFor() {
		if(isStart)
			return;
		isStart = true;
		TickRegister.register(new AbstractTick(tick, true) {
			@Override
			public void run(Type type) {
				absLoop = !isStop;
				if(absLoop)
					WorldAPI.command(command.replace("@카운트", ""+absRunCount));
				if(absRunCount+1 >= maxCount){
					absLoop = false;
				}
				System.out.println("현재 카운트"+absRunCount);
			}
		});
	}

	public static boolean isFor() {
		return fifList.size() > 0;
	}
	public static FunctionFor currentFor() {
		return fifList.get(fifList.size() - 1);
	}

	public static void removeFor(FunctionFor fif) {
		fifList.remove(fif);
	}

	public static void removeFor() {
		currentFor().isStop = true;
		fifList.remove(currentFor());
	}
}
