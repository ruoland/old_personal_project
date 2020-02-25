package ruo.yout.mojaelab;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import olib.api.WorldAPI;
import ruo.yout.Mojae;

public class DogPanEvent {

    @SubscribeEvent
    public void event(LivingExperienceDropEvent event) {
        if(Mojae.morespawn && Mojae.dog_pan)
            event.setCanceled(true);

    }

    @SubscribeEvent
    public void event(LivingDropsEvent event) {
        if(Mojae.morespawn && Mojae.dog_pan)//개판 상태일 떄는 렉 방지를 위해
            event.setCanceled(true);
    }

    /**
     * 몬스터 더많이 스폰하게 하는 이벤트
     */

    @SubscribeEvent
    public void event(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) event.getEntity();
            String monsterName = EntityList.getEntityString(living);
            if (Mojae.dog_pan) {
                if (living instanceof EntityMob) {
                    EntityMob mob = (EntityMob) living;
                    mob.targetTasks.addTask(2, new EntityAINearestAttackableTarget(mob, EntityLiving.class, false));
                }
            }
        }
    }
    @SubscribeEvent
    public void event(LivingSpawnEvent event) {
        if( Mojae.morespawn ) {
            EntityLivingBase livingBase = event.getEntityLiving();
            if (!(livingBase instanceof EntityAnimal)) {
                if (livingBase instanceof EntityMob && !livingBase.getEntityData().hasKey("mojae")) {
                    if(livingBase instanceof EntityCreeper) {
                        livingBase.setDead();
                    }
                    if (livingBase instanceof EntityEnderman) {
                        livingBase.setHealth(20);
                        livingBase.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
                    }
                    for (int i = 0; i < 7; i++) {
                        double x = event.getX() + WorldAPI.rand(30);
                        double z = event.getZ() + WorldAPI.rand(30);
                        EntityLiving creeper = null;
                        switch (event.getWorld().rand.nextInt(5)) {
                            case 0: {
                                //creeper = new EntityCreeper(event.getWorld());
                                break;
                            }
                            case 1: {
                                creeper = new EntityZombie(event.getWorld());
                                break;
                            }
                            case 2: {
                                creeper = new EntitySkeleton(event.getWorld());
                                break;
                            }
                            case 3: {
                                creeper = new EntitySpider(event.getWorld());
                                break;
                            }
                            case 4: {
                                creeper = new EntityEnderman(event.getWorld());
                                break;
                            }
                        }
                        if (creeper != null) {
                            creeper.setPosition(x, event.getY(), z);
                            event.getWorld().spawnEntityInWorld(creeper);
                            creeper.onInitialSpawn(event.getWorld().getDifficultyForLocation(creeper.getPosition()), null);
                            creeper.getEntityData().setBoolean("mojae", true);
                        }

                    }
                }
            }
        }
    }
}
