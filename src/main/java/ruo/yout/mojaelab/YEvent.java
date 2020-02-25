package ruo.yout.mojaelab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.WorldAPI;

public class YEvent {
    public static double speed = 0;

    @SubscribeEvent
    public void playerTick(PlayerInteractEvent.EntityInteract event) {
        if (WorldAPI.equalsItemName(event.getItemStack(), "모리 라이딩")) {
            event.getTarget().startRiding(event.getEntityPlayer());
        }
    }

    @SubscribeEvent
    public void playerTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            if (WorldAPI.equalsHeldItem(Items.STICK))
                if (event.getEntityLiving().getRidingEntity() != null)
                    event.getEntityLiving().getRidingEntity().motionY -= speed;
                else
                    event.getEntityLiving().motionY -= speed;
        }
    }

    @SubscribeEvent
    public void playerTick(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntityPlayer().getRidingEntity() != null) {
            event.getEntityPlayer().getRidingEntity().motionY -= speed;
        }

        event.getEntityPlayer().motionY -= speed;
    }

    @SubscribeEvent
    public void yDown(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().getName().equalsIgnoreCase("모리")) {
            if (WorldAPI.getPlayer() != null) {
                EntityPlayer player = WorldAPI.getPlayer();
                event.getEntityLiving().setPosition(player.posX, player.posY, player.posZ);
            }
        }
    }

}
