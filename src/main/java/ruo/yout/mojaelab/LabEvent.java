package ruo.yout.mojaelab;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.hardcore.HardCore;
import ruo.yout.EntityMoJaeCreeper;
import ruo.yout.Mojae;

public class LabEvent {

    @SubscribeEvent
    public void livingAttackEvent(LivingHurtEvent event) {
        if(Mojae.skelreeper && event.getEntityLiving() instanceof EntitySkeleton){
            if(event.getSource().getEntity() instanceof EntityMoJaeCreeper){
                event.setCanceled(true);
                event.setAmount(0);
                event.getEntityLiving().setVelocity(0,0,0);
            }
        }
        System.out.println("타입 " + event.getSource().damageType);
        System.out.println("소스오브 " + event.getSource().getSourceOfDamage());
        System.out.println("엔티티 " + event.getSource().getEntity());
        System.out.println("이벤트"+event.getEntityLiving());
    }

    @SubscribeEvent
    public void livingAttackEvent(LivingAttackEvent event) {
        if (Mojae.arrow_riding && event.getSource() instanceof EntityDamageSourceIndirect) {
            EntityDamageSourceIndirect sourceIndirect = (EntityDamageSourceIndirect) event.getSource();
            if (sourceIndirect.getSourceOfDamage() instanceof EntityArrow) {
                event.setCanceled(true);
            }
            System.out.println("타입 " + sourceIndirect.damageType);
            System.out.println("소스오브 " + sourceIndirect.getSourceOfDamage());
            System.out.println("엔티티 " + sourceIndirect.getEntity());
        }
        if (Mojae.skelreeper && event.getSource() instanceof EntityDamageSourceIndirect) {
            EntityDamageSourceIndirect sourceIndirect = (EntityDamageSourceIndirect) event.getSource();
            if (event.getEntityLiving().getEntityData().getBoolean("isArrowreper") && sourceIndirect.getSourceOfDamage() instanceof EntityArrow) {
                event.setCanceled(true);
            }
            System.out.println("타입 " + sourceIndirect.damageType);
            System.out.println("소스오브 " + sourceIndirect.getSourceOfDamage());
            System.out.println("엔티티 " + sourceIndirect.getEntity());
            System.out.println("엔티티2 " + event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (Mojae.skelreeper && arrow.shootingEntity instanceof EntitySkeleton) {
                arrowReeper(arrow);
            }
            if (Mojae.arrow_reeper && arrow.shootingEntity instanceof EntityPlayer) {
                arrowReeper(arrow);
            }
            if (Mojae.arrow_riding && arrow.shootingEntity instanceof EntitySkeleton) {
                arrowRiding(arrow);
            }
            if (Mojae.arrow_riding && arrow.shootingEntity instanceof EntityPlayer) {
                arrowRiding(arrow);
            }
        }

        if (Mojae.arrow_count > 1 && event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (arrow.shootingEntity instanceof EntitySkeleton) {
                EntitySkeleton skeleton = (EntitySkeleton) arrow.shootingEntity;
                NBTTagCompound tagCompound = skeleton.getEntityData();
                int attackDelay = tagCompound.getInteger("AttackDelay");
                tagCompound.setInteger("AttackDelay", 20);
                if (attackDelay == 0) {
                    for (int i = 0; i < Mojae.arrow_count; i++) {
                        EntityLivingBase attackTarget = skeleton.getAttackTarget();
                        EntityZombie zombie = new EntityZombie(event.getWorld());
                        zombie.setPosition(attackTarget.posX + HardCore.rand(3), attackTarget.posY, attackTarget.posZ + HardCore.rand(3));
                        skeleton.attackEntityWithRangedAttack(zombie, ItemBow.getArrowVelocity(skeleton.getItemInUseMaxCount()));
                    }
                }
            }
        }
    }

    public void arrowReeper(EntityArrow arrow) {
        EntityMoJaeCreeper creeper = new EntityMoJaeCreeper(arrow.worldObj);
        creeper.setPosition(arrow.posX, arrow.posY, arrow.posZ);
        creeper.setVelocity(arrow.motionX, arrow.motionY, arrow.motionZ);
        arrow.setDead();
        arrow.worldObj.spawnEntityInWorld(creeper);
        creeper.ignite();
        creeper.getEntityData().setBoolean("isArrowreper", true);
    }

    public void arrowRiding(EntityArrow arrow) {
        arrow.shootingEntity.startRiding(arrow);
    }

    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntitySkeleton) {
            EntitySkeleton skeleton = (EntitySkeleton) event.getEntityLiving();
            NBTTagCompound tagCompound = skeleton.getEntityData();
            if (tagCompound.hasKey("AttackDelay") && tagCompound.getInteger("AttackDelay") > 0) {
                tagCompound.setInteger("AttackDelay", tagCompound.getInteger("AttackDelay") - 1);
            }
        }
    }

    @SubscribeEvent
    public void event(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) event.getEntity();
            String monsterName = EntityList.getEntityString(living);
            if (Mojae.dog_pan) {
                if (living instanceof EntityMob) {
                    EntityMob mob = (EntityMob) living;
                    mob.targetTasks.addTask(1, new EntityAIHurtByTarget(mob, true, new Class[]{EntityPigZombie.class}));
                    mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, EntityDragon.class, false));
                    mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, EntityWither.class, false));
                    mob.targetTasks.addTask(2, new EntityAINearestAttackableTarget(mob, EntityLiving.class, false));
                }
            }
            if (Mojae.monterAttack.containsKey(monsterName)) {
                String attackKey = Mojae.monterAttack.get(monsterName);
                Class entityClass = EntityList.NAME_TO_CLASS.get(attackKey);

                if (living instanceof EntityMob) {
                    EntityMob mob = (EntityMob) living;
                    mob.targetTasks.addTask(1, new EntityAIHurtByTarget(mob, true, new Class[]{EntityPigZombie.class}));
                    mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, entityClass, false));
                    mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, entityClass, false));
                    mob.targetTasks.addTask(2, new EntityAINearestAttackableTarget(mob, entityClass, false));
                }
            }
        }
    }
}
