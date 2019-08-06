package ruo.yout.mojaelab;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.yout.EntityFlyingCreeperLab;
import ruo.yout.EntityMoJaeCreeper;
import ruo.yout.Mojae;

public class StatEvent {
    @SubscribeEvent
    public void livingDeath(LivingDeathEvent event) {
        if(event.getSource().getEntity() != null) {
            String n = event.getSource().getEntity().getName();
            if(Mojae.statStart) {
                if (Mojae.killMap.containsKey(n))
                    Mojae.killMap.put(n, Mojae.killMap.get(n) + 1);
                else
                    Mojae.killMap.put(n, 1F);
            }
        }
    }


    @SubscribeEvent
    public void livingAttackEvent(LivingHurtEvent event) {
        if(event.getSource().getEntity() != null) {
            String n = event.getSource().getEntity().getName();
            if(Mojae.statStart) {
                if (Mojae.damageMap.containsKey(n))
                    Mojae.damageMap.put(n, Mojae.damageMap.get(n) + event.getAmount());
                else
                    Mojae.damageMap.put(n, event.getAmount());
            }
        }

    }
}
