package ruo.map.lopre2.jump2;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import ruo.cmplus.cm.v18.CommandHeal;
import ruo.map.lopre2.CommandJB;
import ruo.map.lopre2.ItemSpanner;
import ruo.map.lopre2.LoPre2;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.action.ActionEffect;


public class JumpEvent2 {
    public static boolean food;

    @SubscribeEvent
    public void chatMessage(ClientChatReceivedEvent e) {
        if (LoPre2.chackWorld()) {
            if (e.getType() == 1) {
                if (e.getMessage().getUnformattedComponentText().indexOf("의 리스폰 지점을") != -1)
                    e.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void playerTick(PlayerEvent.PlayerLoggedOutEvent e) {
    }

    @SubscribeEvent
    public void playerTick(CommandEvent e) {
        if (LoPre2.chackWorld()) {
            if (e.getCommand().getCommandName().equalsIgnoreCase("spawnpoint")) {
                ActionEffect.setYTP(WorldAPI.getPlayer().posY - 20, WorldAPI.getPlayer().rotationPitch, WorldAPI.getPlayer().rotationYaw);
                WorldAPI.command("/heal");
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e) {
        if (LoPre2.chackWorld()) {
            if (!CommandJB.isDebMode && e.side == Side.SERVER && e.phase == TickEvent.Phase.END) {
                for (ItemStack stack : e.player.inventory.mainInventory) {
                    if (stack != null && stack.getItem() instanceof ItemSpanner) {
                        CommandJB.isDebMode = true;
                    }

                }
            }

        }
    }

}
