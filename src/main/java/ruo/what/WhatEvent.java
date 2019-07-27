package ruo.what;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class WhatEvent {
   // @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof IMob) {
            EntityLiving mob = (EntityLiving) e.getEntityLiving();
            Random rand = mob.worldObj.rand;
            int teleportDelay = mob.getEntityData().getInteger("teleportDelay");
            if (mob.getAttackTarget() != null) {
                if (mob.worldObj.isRemote) {
                    for (int i = 0; i < 2; ++i) {
                        mob.worldObj.spawnParticle(EnumParticleTypes.PORTAL, mob.posX + (rand.nextDouble() - 0.5D) * (double) mob.width, mob.posY + rand.nextDouble() * (double) mob.height - 0.25D, mob.posZ + (rand.nextDouble() - 0.5D) * (double) mob.width, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
                    }
                }
                EntityLivingBase target = mob.getAttackTarget();
                if (mob.getDistanceToEntity(target) > 7 && teleportDelay == 0) {
                    for (int i = 0; i < 16; i++)
                        if (mob.attemptTeleport(target.posX + CreeperWorld.rand(5), target.posY + CreeperWorld.rand(5), target.posZ + CreeperWorld.rand(5))) {
                            teleportDelay = 500;
                            mob.getEntityData().setInteger("teleportDelay", 500);
                        }
                }

                if (teleportDelay > 0)
                    mob.getEntityData().setInteger("teleportDelay", --teleportDelay);

            }
        }
    }


    @SubscribeEvent
    public void event(EntityJoinWorldEvent e) {
        if(e.getEntity() instanceof EntityLiving) {
            if(!e.getWorld().isRemote){
                if (e.getEntity() instanceof EntitySpider && !(e.getEntity() instanceof EntitySpiderJockey) && e.getWorld().rand.nextInt(4) == 0) {
                    EntitySpider spider = (EntitySpider) e.getEntity();
                    EntitySpiderJockey spiderJockey = new EntitySpiderJockey(e.getWorld());
                    spiderJockey.setPosition(spider.posX, spider.posY, spider.posZ);
                    e.getWorld().spawnEntityInWorld(spiderJockey);
                    spiderJockey.onInitialSpawn(e.getWorld().getDifficultyForLocation(spider.getPosition()),null);
                }
                if (e.getEntity() instanceof EntityCreeper && !(e.getEntity() instanceof EntityTeleportCreeper)) {
                    EntityCreeper creeper = (EntityCreeper) e.getEntity();
                    EntityTeleportCreeper teleportCreeper = new EntityTeleportCreeper(e.getWorld());
                    teleportCreeper.setPosition(creeper.posX, creeper.posY, creeper.posZ);
                    e.getWorld().spawnEntityInWorld(teleportCreeper);
                    System.out.println("소환됨"+teleportCreeper.posX+ " "+teleportCreeper.posY+ " "+teleportCreeper.posZ);
                }

            }
        }
        if (e.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) e.getEntity();
            if (arrow.shootingEntity instanceof EntitySkeleton) {
                if (e.getWorld().rand.nextInt(10) == 0) {
                    EntityCreeper creeper = new EntityCreeper(e.getWorld());
                    creeper.setPosition(e.getEntity().posX, e.getEntity().posY, e.getEntity().posZ);
                    creeper.setVelocity(e.getEntity().motionX, e.getEntity().motionY, e.getEntity().motionZ);
                    e.getEntity().setDead();
                    e.getWorld().spawnEntityInWorld(creeper);
                    creeper.ignite();
                }
            }
        }

    }


    @SubscribeEvent
    public void skeletonTeamKill(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntitySkeleton && event.getSource().getEntity() instanceof EntitySkeleton) {
            event.setCanceled(true);
        }
    }

}
