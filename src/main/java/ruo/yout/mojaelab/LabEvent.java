package ruo.yout.mojaelab;

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
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.hardcore.HardCore;
import ruo.yout.Mojae;

public class LabEvent {

    @SubscribeEvent
    public void livingAttackEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (Mojae.skelreeper && arrow.shootingEntity instanceof EntitySkeleton) {
                arrowRiding(arrow);
            }
            if (Mojae.arrow_reeper && arrow.shootingEntity instanceof EntityPlayer) {
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
                    for(int i = 0; i < Mojae.arrow_count;i++) {
                        EntityLivingBase attackTarget = skeleton.getAttackTarget();
                        EntityZombie zombie = new EntityZombie(event.getWorld());
                        zombie.setPosition(attackTarget.posX + HardCore.rand(3), attackTarget.posY, attackTarget.posZ + HardCore.rand(3));
                        skeleton.attackEntityWithRangedAttack(zombie, ItemBow.getArrowVelocity(skeleton.getItemInUseMaxCount()));
                    }
                }
            }
        }
    }

    public void arrowRiding(EntityArrow arrow) {
        EntityCreeper creeper = new EntityCreeper(arrow.worldObj);
        creeper.setPosition(arrow.posX, arrow.posY, arrow.posZ);
        creeper.setVelocity(arrow.motionX, arrow.motionY, arrow.motionZ);
        arrow.worldObj.spawnEntityInWorld(creeper);
        creeper.ignite();
        creeper.startRiding(arrow);
    }


    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof EntitySkeleton){
            EntitySkeleton skeleton = (EntitySkeleton) event.getEntityLiving();
            NBTTagCompound tagCompound = skeleton.getEntityData();
            if(tagCompound.hasKey("AttackDelay") && tagCompound.getInteger("AttackDelay") > 0){
                tagCompound.setInteger("AttackDelay", tagCompound.getInteger("AttackDelay")-1);
            }
        }
    }
    @SubscribeEvent
    public void event(LivingSpawnEvent event) {
        if (Mojae.dog_pan) {
            if (event.getEntityLiving() instanceof EntityMob) {
                EntityMob mob = (EntityMob) event.getEntityLiving();
                mob.targetTasks.addTask(1, new EntityAIHurtByTarget(mob, true, new Class[]{EntityPigZombie.class}));
                mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, EntityDragon.class, false));
                mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget(mob, EntityWither.class, false));
                mob.targetTasks.addTask(2, new EntityAINearestAttackableTarget(mob, EntityLiving.class, false));
            }
        }
    }
}
