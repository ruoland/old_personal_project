package cmplus.cm.v18.function;

import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.util.ArrayList;

public class FunctionWhile {
	public static ArrayList<FunctionWhile> fifList = new ArrayList<FunctionWhile>();
	private boolean isStart;
	private int tick;
	private String command;
	private FunctionIF fif;
	public FunctionWhile(FunctionIF fif, int tick, String command) {
		this.tick = tick;
		this.command = command;
		this.fif = fif;
		runFor();
	}

	public static FunctionWhile addFor(FunctionIF fif, int tick, String command) {
		fifList.add(new FunctionWhile(fif, tick, command));
		return currentFor();
	}
	private void runFor() {
		if(isStart)
			return;
		isStart = true;
		TickRegister.register(new AbstractTick(tick, true) {
			@Override
			public void run(Type type) {
				absLoop = fif.check();
				if(absLoop)
					WorldAPI.command(command);
			}
		});
	}
	public static boolean isFor() {
		return fifList.size() > 0;
	}
	public static FunctionWhile currentFor() {
		return fifList.get(fifList.size() - 1);
	}

	public static void removeFor(FunctionWhile fif) {
		fifList.remove(fif);
	}

	public static void removeFor() {
		currentFor().fif = FunctionIF.create("1", "==", "0");
		fifList.remove(currentFor());
	}
}
