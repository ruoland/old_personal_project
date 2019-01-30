package rmap.lopre2;

import minigameLib.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;


public class LooPreClientEvent {
    @SubscribeEvent
    public void client(TickEvent.ClientTickEvent event){
        if(LoPre2.checkWorld() && Minecraft.getMinecraft().currentScreen == null && Keyboard.isKeyDown(Keyboard.KEY_R) && WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getBedLocation() != null){
            WorldAPI.teleport(WorldAPI.getPlayerMP().getBedLocation());
            WorldAPI.getPlayerMP().heal(20);
            WorldAPI.getPlayer().fallDistance = 0;
            WorldAPI.getPlayer().getFoodStats().setFoodLevel(20);
        }
    }
}
