package map.lopre2.jump3;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import olib.api.WorldAPI;


public class JumpThreeEvent {
    private static String pressUsername;
    private static int inputDelay;

    @SubscribeEvent
    public void minecart(PlaySoundAtEntityEvent event) {
        if(event.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE){
            event.setVolume(event.getDefaultVolume() / 5F);
        }
    }

    @SubscribeEvent
    public void minecart(ExplosionEvent event) {
        event.getExplosion().clearAffectedBlockPositions();
    }

    @SubscribeEvent
    public void minecart(PlayerTickEvent event) {
        if (WorldAPI.getCurrentWorldName().equalsIgnoreCase("JumpThree") && event.player.getLowestRidingEntity() instanceof EntityMinecart) {
            inputDelay--;
            EntityMinecart entityMinecart = (EntityMinecart) event.player.getRidingEntity();
            if (event.player.getLowestRidingEntity().motionY > -0.5)
                entityMinecart.setCanUseRail(true);
            else
                entityMinecart.setCanUseRail(false);
            entityMinecart.setCurrentCartSpeedCapOnRail(0.8F);
            if (event.side == Side.CLIENT) {
                if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
                    pressUsername = event.player.getName();
                    System.out.println("키 클릭");
                    inputDelay = 10;
                } else if (inputDelay < 0) {
                    pressUsername = null;
                }
            } else {
                if (pressUsername != null && inputDelay > 0 && entityMinecart.canUseRail()) {
                    entityMinecart.setCanUseRail(false);
                    System.out.println(Minecraft.getMinecraft().thePlayer.rotationYaw);
                    inputDelay = 0;
                }
            }

        }
    }
}
