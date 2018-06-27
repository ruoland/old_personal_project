package ruo.minigame.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import ruo.minigame.effect.AbstractTick.Position;

import java.util.ArrayList;

public class TickRegister {

	private static ArrayList<AbstractTick> list = new ArrayList<AbstractTick>();
	private static ArrayList<AbstractTick> removeList = new ArrayList<AbstractTick>();

	public static void register(AbstractTick abs) {
		list.add(abs);
	}

	public static void remove(AbstractTick abs) {
		abs.stopTick();
		removeList.add(abs);
	}
	public static void remove(String n) {
		AbstractTick abs = getAbsTick(n);
		if(abs != null) {
			abs.stopTick();
			removeList.add(abs);
		}else
			throw new NullPointerException();
		
	}
	public static void removeAll() {
		for (AbstractTick abs : list) {
			remove(abs);
		}
	}
	public static void removeAllPosition() {
		for (AbstractTick abs : list) {
			if(abs instanceof Position)
				remove(abs);
		}
	}
	public static boolean isAbsTickRun(String name) {
		AbstractTick abs = getAbsTick(name);
		if(abs == null || removeList.contains(abs)) {
			return false;
		}
		return true;
	}

	/**
	 * AbsTick이 작동을 멈췄는지 확인하기 위해서는 isAbsTickRun 메서드를 사용해야 합니다
	 * 이 코드는 죽지 않은 틱만 반환합니다
	 */
	public static AbstractTick getAbsTick(String name) {
		for (AbstractTick abs : list) {
			if (abs.equals(name) && !abs.isDead()) {
				return abs;
			}
		}
		return null;
	}

	@SubscribeEvent
	public void sub(TickEvent event) {
		if (event.phase == Phase.END && !isGamePaused()) {
			for (int i = 0; i < list.size();i++) {
				AbstractTick abs = list.get(i);
				if (abs == null || abs.isPause() || abs.isDead()) {
					continue;
				}
				if (abs.tickEvent == null)
					abs.tickEvent = event;
				if (abs.subtraction(event.type)) {
					remove(abs);
				}
			}
		}
		if(removeList.size() > 0 && isGamePaused()) {
			list.removeAll(removeList);
			removeList.clear();
		}
	}

	public boolean isGamePaused() {
		Minecraft mc = Minecraft.getMinecraft();
		return (mc.currentScreen instanceof GuiDownloadTerrain)
				|| (mc.currentScreen instanceof GuiScreenWorking);
	}
}
