package tnttool;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TNTEvent {

    @SubscribeEvent
    public void tnt(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof EntityTNTPrimed){
            EntityTNTPrimed tntPrimed = (EntityTNTPrimed) event.getEntity();
            tntPrimed.setVelocity(0,0,0);
        }


    }
}
