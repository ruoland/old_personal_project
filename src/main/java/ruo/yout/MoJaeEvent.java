package ruo.yout;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.cmplus.cm.v18.function.FunctionFor;

public class MoJaeEvent {
    public static double attackDelay = -1;
    @SubscribeEvent
    public void event(LivingAttackEvent event) {
        String name = event.getEntityLiving().getCustomNameTag();
        if (event.getEntityLiving().isPotionActive(Mojae.godPotion) || name.startsWith("무적 상태")) {
           event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        String name = event.getEntityLiving().getCustomNameTag();
        EntityLivingBase livingBase = event.getEntityLiving();
        if(attackDelay  != -1 && livingBase instanceof EntityMob &&!livingBase.getCustomNameTag().equalsIgnoreCase("잠금")){
            livingBase.hurtResistantTime = (int) attackDelay;
        }
        if(name.startsWith("잠금해제")){
            livingBase.removePotionEffect(Mojae.lockPotion);
            livingBase.setCustomNameTag("");
        }
        if (name.startsWith("잠금")) {
            livingBase.setVelocity(0, 0, 0);
            NBTTagCompound tag = livingBase.getEntityData();
            if(tag.getDouble("LPX") == 0 || tag.getDouble("LPY") == 0 || tag.getDouble("LPZ") == 0)
            {
                return;
            }
            livingBase.setPosition(tag.getDouble("LPX"), tag.getDouble("LPY")
                    , tag.getDouble("LPZ"));
        }
    }

}
