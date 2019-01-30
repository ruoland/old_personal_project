package rmap.lopre2;

import minigameLib.MiniGame;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;


public class LooPreThreeEvent {
    private static String pressUsername;
    private static int inputDelay;
    @SubscribeEvent
    public  void minecart(PlayerTickEvent event) {
        if (event.player.getLowestRidingEntity() instanceof EntityMinecart) {
            inputDelay--;
            EntityMinecart entityMinecart = (EntityMinecart) event.player.getRidingEntity();
            if(event.player.getLowestRidingEntity().motionY > -0.5)
                entityMinecart.setCanUseRail(true);
            else
                entityMinecart.setCanUseRail(false);
            entityMinecart.setCurrentCartSpeedCapOnRail(0.8F);
            if (event.side == Side.CLIENT) {
                if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
                    pressUsername = event.player.getName();
                    System.out.println("키 클릭");
                    inputDelay = 10;
                }
                else if(inputDelay < 0){
                    pressUsername = null;
                }
            }
            else{
                if(pressUsername != null && inputDelay > 0 && entityMinecart.canUseRail()) {
                    entityMinecart.setCanUseRail(false);
                    System.out.println(MiniGame.scroll.getForwardXZ());
                    entityMinecart.moveEntity(0,2.3, MiniGame.scroll.getForwardXZ() * 5);
                    System.out.println(Minecraft.getMinecraft().thePlayer.rotationYaw);
                    inputDelay = 0;
                }
            }

        }
    }
}
