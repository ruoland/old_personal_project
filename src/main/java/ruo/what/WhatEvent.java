package ruo.what;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class WhatEvent {
    @SubscribeEvent
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
                        if (mob.attemptTeleport(target.posX + What.rand(5), target.posY + What.rand(5), target.posZ + What.rand(5))) {
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
    public void event(LivingAttackEvent e) {
        if (e.getEntityLiving() instanceof EntityEnderman) {
            EntityEnderman enderman = (EntityEnderman) e.getEntityLiving();
            Random rand = enderman.worldObj.rand;
            if (enderman.getHeldBlockState() == Blocks.TNT.getDefaultState()) {
                if (enderman.getAttackTarget() != null) {
                    EntityLivingBase livingBase = enderman.getAttackTarget();
                    if (livingBase.getDistanceToEntity(enderman) < 5) {
                        World worldIn = e.getEntityLiving().worldObj;
                        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, e.getEntityLiving().posX, e.getEntityLiving().posY, e.getEntityLiving().posZ, e.getEntityLiving());
                        worldIn.spawnEntityInWorld(entitytntprimed);
                        worldIn.playSound((EntityPlayer) null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);

                        for (int i = 0; i < 64; ++i) {
                            if (enderman.attemptTeleport(enderman.posX + (rand.nextDouble() - 0.5) * 32, enderman.posY + (rand.nextInt(16) - 8), enderman.posZ + (rand.nextDouble() - 0.5))) {
                                enderman.setHeldBlockState(Blocks.AIR.getDefaultState());
                                break;
                            }
                        }
                    }
                }
            } else if (e.getEntityLiving() instanceof IMob) {
                EntityLiving mob = (EntityLiving) e.getEntityLiving();
                for (int i = 0; i < 16; i++)
                    if (mob.attemptTeleport(mob.posX + What.rand(5), mob.posY + What.rand(5), mob.posZ + What.rand(5))) {
                    }
            }

        }
    }

    @SubscribeEvent
    public void event(LivingSpawnEvent e) {

    }

    @SubscribeEvent
    public void event(EntityJoinWorldEvent e) {
        if(e.getEntity() instanceof EntityLiving) {
            System.out.println(e.getEntity().ticksExisted + " _ " + ((EntityLiving) e.getEntity()).getAge());
            if(((EntityLiving) e.getEntity()).getAge() == 0 && !e.getWorld().isRemote){
                if (e.getEntity() instanceof EntitySpider && !e.getEntity().getEntityData().getBoolean("isJockey")) {
                    EntitySpider spider = (EntitySpider) e.getEntity();
                    spider.getEntityData().setBoolean("isJockey", true);
                    EntitySkeleton skeleton = new EntitySkeleton(e.getWorld());
                    skeleton.setLocationAndAngles(spider.posX, spider.posY, spider.posZ, spider.rotationYaw, 0F);
                    skeleton.onInitialSpawn(e.getWorld().getDifficultyForLocation(spider.getPosition()), (IEntityLivingData) null);
                    e.getWorld().spawnEntityInWorld(skeleton);
                    skeleton.startRiding(spider);
                }
                if (e.getEntity() instanceof EntityEnderman && !e.getEntity().getEntityData().getBoolean("isTNT")) {
                    e.getEntity().getEntityData().setBoolean("isTNT", true);
                    EntityEnderman enderman = (EntityEnderman) e.getEntity();
                    if (e.getWorld().rand.nextInt(5) == 0)
                        enderman.setHeldBlockState(Blocks.TNT.getDefaultState());
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
        }


    }


    @SubscribeEvent
    public void skeletonTeamKill(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntitySkeleton && event.getSource().getEntity() instanceof EntitySkeleton) {
            event.setCanceled(true);
        }
    }

}
