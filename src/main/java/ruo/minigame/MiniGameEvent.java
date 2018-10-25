package ruo.minigame;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.ArrayUtils;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

import java.lang.reflect.Field;

public class MiniGameEvent {

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
