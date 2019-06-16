package ruo.what;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import olib.api.WorldAPI;

import java.util.Random;

public class WhatEvent {

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
            }

        }
    }


    @SubscribeEvent
    public void event(PlayerInteractEvent.EntityInteract e) {
        if (e.getTarget() instanceof EntityBoat) {
            if (WorldAPI.equalsHeldItem(e.getEntityPlayer(), Items.BOAT)) {
                EntityBoat boat = (EntityBoat) e.getTarget();
                EntityBoat boat1 = new EntityBoat(e.getWorld());
                boat1.setPosition(boat.posX, boat.posY, boat.posZ);
                e.getWorld().spawnEntityInWorld(boat1);
                boat.startRiding(boat1);
            }
        }
    }


    @SubscribeEvent
    public void event(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityEnderman) {
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


    @SubscribeEvent
    public void skeletonTeamKill(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntitySkeleton && event.getSource().getEntity() instanceof EntitySkeleton) {
            event.setCanceled(true);
        }
    }

}
