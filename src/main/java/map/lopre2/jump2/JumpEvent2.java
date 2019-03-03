package map.lopre2.jump2;

import minigameLib.action.ActionEffect;
import minigameLib.api.WorldAPI;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import map.lopre2.CommandJB;
import map.lopre2.ItemSpanner;
import map.lopre2.LoPre2;


public class JumpEvent2 {

    @SubscribeEvent
    public void chatMessage(ClientChatReceivedEvent e) {
        if (LoPre2.checkWorld()) {
            if (e.getType() == 1) {
                if (e.getMessage().getUnformattedComponentText().indexOf("의 리스폰 지점을") != -1)
                    e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void playerTick(CommandEvent e) {
        if (LoPre2.checkWorld()) {
            if (e.getCommand().getCommandName().equalsIgnoreCase("spawnpoint")) {
                WorldAPI.command("/heal");
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e) {
        if (WorldAPI.getCurrentWorldName().equalsIgnoreCase("JumpMap Sea2")) {
            if (e.player.getBedLocation() != null) {
                if(e.player.getBedLocation().getY() - 20 > 0)
                ActionEffect.setYTP(e.player.getBedLocation().getY() - 20, ActionEffect.getPitch(), ActionEffect.getYaw());
            }
        }
    }

}
