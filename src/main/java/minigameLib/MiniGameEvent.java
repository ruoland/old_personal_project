package minigameLib;

import cmplus.deb.DebAPI;
import minigameLib.api.LoginEvent;
import minigameLib.api.WorldAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class MiniGameEvent {
    @SubscribeEvent
    public void gameoverlae(WorldEvent.Load e) {
        WorldAPI.reloadWorldName();
    }

    @SubscribeEvent
    public void gameoverlay(TickEvent.PlayerTickEvent e) {
        if (DebAPI.debAPI.size() > 0 && e.phase == TickEvent.Phase.END && e.side == Side.SERVER)
            DebAPI.deb();
    }

    @SubscribeEvent
    public void gameoverlay(ServerChatEvent e) {
        if (DebAPI.debAPI.size() > 0) {
            if (e.getMessage().startsWith("s:")) {
                DebAPI.activeName = e.getMessage().replace("s:", "");
            }
            String[] split = e.getMessage().split(",");
            if (split.length > 2) {
                float x = Float.valueOf(split[0]);
                float y = Float.valueOf(split[1]);
                float z = Float.valueOf(split[2]);
                DebAPI.debAPI.get(DebAPI.activeName).x = x;
                DebAPI.debAPI.get(DebAPI.activeName).y = y;
                DebAPI.debAPI.get(DebAPI.activeName).z = z;
            }
        }
    }

    //Login 이벤트
    @SubscribeEvent
    public void event(EntityJoinWorldEvent event2) {
        if (event2.getEntity() instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new LoginEvent((EntityPlayer) event2.getEntity(), event2.getWorld()));
        }
    }

}
