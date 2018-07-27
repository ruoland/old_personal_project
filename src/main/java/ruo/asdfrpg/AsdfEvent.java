package ruo.asdfrpg;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.minigame.api.LoginEvent;

public class AsdfEvent {
    @SubscribeEvent
    public void playerTick(LoginEvent e){
        SkillHelper.init(e.player);
    }
    @SubscribeEvent
    public void playerTick(GuiOpenEvent e){
        if(e.getGui() instanceof GuiGameOver){
            e.setGui(new GuiAsdfGameOver());
        }
    }
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e){
        if(!e.player.isCreative()) {
            if (e.player.isPotionActive(AsdfRPG.flyPotion) && e.player.capabilities.isFlying) {
                e.player.capabilities.isFlying = true;
                e.player.sendPlayerAbilities();
            } else {
                e.player.capabilities.isFlying = false;
                e.player.sendPlayerAbilities();
            }
        }

    }
}
