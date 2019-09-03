package ruo.yout.y;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.WorldAPI;

public class YEvent
{
    int speed = 10;
    @SubscribeEvent
    public void playerTick(ServerChatEvent event){
        speed = Integer.valueOf(event.getMessage());

    }
    @SubscribeEvent
    public void playerTick(PlayerInteractEvent.EntityInteract event){
        if(WorldAPI.equalsItemName(event.getItemStack(), "모리 라이딩")){
            event.getTarget().startRiding(event.getEntityPlayer());
        }
    }
    @SubscribeEvent
    public void playerTick(PlayerInteractEvent.RightClickItem event){
        if(WorldAPI.equalsItemName(event.getItemStack(), "내려가기")){
            event.getEntityPlayer().motionY -= speed;
        }
    }
    @SubscribeEvent
    public void yDown(LivingEvent.LivingUpdateEvent event){
        if(event.getEntityLiving().getName().equalsIgnoreCase("모리")){
            if(WorldAPI.getPlayer() != null) {
                EntityPlayer player = WorldAPI.getPlayer();
                event.getEntityLiving().setPosition(player.posX, player.posY, player.posZ);
            }
        }
    }

    @SubscribeEvent
    public void yDown(LivingHurtEvent event){
        event.setAmount(0);
    }
}
