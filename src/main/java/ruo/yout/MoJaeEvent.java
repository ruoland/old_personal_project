package ruo.yout;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.map.mafence.tower.EntityCreeperTower;

public class MoJaeEvent {
    @SubscribeEvent
    public void event(LivingAttackEvent event) {
        String name = event.getEntityLiving().getCustomNameTag();
        if (event.getEntityLiving().isPotionActive(MoJaYo.lockPotion) || name.startsWith("잠금")) {
            event.getEntityLiving().setVelocity(0, 0, 0);
            NBTTagCompound tag = event.getEntityLiving().getEntityData();
            event.getEntityLiving().setPosition(tag.getDouble("LPX"), tag.getDouble("LPY")
                    , tag.getDouble("LPZ"));

        }
    }

    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        String name = event.getEntityLiving().getCustomNameTag();
        if (event.getEntityLiving().isPotionActive(MoJaYo.lockPotion) || name.startsWith("잠금")) {
            event.getEntityLiving().setVelocity(0, 0, 0);
            NBTTagCompound tag = event.getEntityLiving().getEntityData();
            event.getEntityLiving().setPosition(tag.getDouble("LPX"), tag.getDouble("LPY")
                    , tag.getDouble("LPZ"));

        }
    }

    @SubscribeEvent
    public void event(LivingSpawnEvent event) {
        if (event.getEntityLiving() instanceof EntityMob) {
            EntityMob mob = (EntityMob) event.getEntityLiving();
            mob.targetTasks.addTask(1, new EntityAIHurtByTarget(mob, true, new Class[]{EntityPigZombie.class}));
            mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, EntityDragon.class, false));
            mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, EntityWither.class, false));
            mob.targetTasks.addTask(2, new EntityAINearestAttackableTarget(mob, EntityLiving.class, false));
        }

    }
}
