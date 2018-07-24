package ruo.minigame;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.action.ActionEffect;

public class MiniGameEvent {

    @SubscribeEvent
    public void waterJump(TickEvent.PlayerTickEvent e) {
        if (e.side == Side.SERVER && e.phase == TickEvent.Phase.END) {
            MiniGame.cooldownTracker.tick();
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
