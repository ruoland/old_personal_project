package rmap.lopre2.jump2;

import minigameLib.action.ActionEffect;
import minigameLib.api.WorldAPI;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import rmap.lopre2.CommandJB;
import rmap.lopre2.ItemSpanner;
import rmap.lopre2.LoPre2;


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
        if (LoPre2.checkWorld()) {
            if (!CommandJB.isDebMode && e.side == Side.SERVER && e.phase == TickEvent.Phase.END) {
                for (ItemStack stack : e.player.inventory.mainInventory) {
                    if (stack != null && stack.getItem() instanceof ItemSpanner) {
                        CommandJB.isDebMode = true;
                    }
                }
                if(e.player.bedLocation != null)
                ActionEffect.setYTP(e.player.bedLocation.getY() - 20, ActionEffect.getPitch(), ActionEffect.getYaw());

            }

        }
    }

}
