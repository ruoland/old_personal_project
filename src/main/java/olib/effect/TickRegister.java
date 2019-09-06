package olib.effect;

import minigameLib.minigame.GuiUnder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

public class TickRegister {

    private static ArrayList<TickTask> tickTasks = new ArrayList<>();
    private static ArrayList<TickTask> removeList = new ArrayList<>();

    public static void register(TickTask abs) {
        tickTasks.add(abs);
    }

    public static void remove(TickTask abs) {
        abs.stopTick();
        removeList.add(abs);
    }
    public static void remove(String n) {
        TickTask abs = getTickTask(n);
        if(abs != null) {
            abs.stopTick();
            removeList.add(abs);
        }else
            throw new NullPointerException();

    }
    public static void removeAll() {
        for (TickTask abs : tickTasks) {
            remove(abs);
        }
    }
    public static void removeAllPosition() {
        for (TickTask abs : tickTasks) {
            if(abs instanceof TickTask.Position)
                remove(abs);
        }
    }
    public static boolean isTickTaskRun(String name) {
        TickTask abs = getTickTask(name);
        return abs != null && !removeList.contains(abs);
    }

    /**
     * AbsTick이 작동을 멈췄는지 확인하기 위해서는 isTickTaskRun 메서드를 사용해야 합니다
     * 이 코드는 죽지 않은 틱만 반환합니다
     */
    public static TickTask getTickTask(String name) {
        for (TickTask abs : tickTasks) {
            if (abs.equals(name) && !abs.isDead()) {
                return abs;
            }
        }
        return null;
    }



    public static boolean isGamePaused() {
        if(FMLCommonHandler.instance().getSide() == Side.SERVER)
            return false;
        Minecraft mc = Minecraft.getMinecraft();
        return (mc.currentScreen instanceof GuiDownloadTerrain)
                || (mc.currentScreen instanceof GuiScreenWorking);
    }

    public static class TickRegisterEvent{
        @SubscribeEvent

        public void asf(PlayerInteractEvent event){
            Minecraft.getMinecraft().displayGuiScreen(new GuiUnder());
            Minecraft.getMinecraft().currentScreen = new GuiUnder();
        }
        @SubscribeEvent
        public void sub(TickEvent event) {
            if (event.phase == Phase.END && !isGamePaused()) {
                for (int i = 0;i < tickTasks.size();i++) {
                    TickTask abs = tickTasks.get(i);
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
                tickTasks.removeAll(removeList);
                removeList.clear();
            }
        }
    }
}
