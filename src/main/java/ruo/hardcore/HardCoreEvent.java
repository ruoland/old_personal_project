package ruo.hardcore;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemBow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.asdf.EntitySkelereeper;
import ruo.minigame.api.WorldAPI;

import java.util.Random;

public class HardCoreEvent {

    @SubscribeEvent
    public void livingAttackEvent(EntityJoinWorldEvent event) {
        /*
        스켈레톤이 화살을 더 많이 쏘게 만드는 코드입니다.
        LivingAttackEvent 나 LivingHurtEvent는 스켈레톤이 화살을 쏠 때 반응하지 않아서
        EntityJoinWorldEvent로 화살이 소환됐을 때 그 화살을 스켈레톤이 쏜 경우 작동하도록 했습니다.
         */
        if (event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (arrow.shootingEntity instanceof EntitySkeleton) {
                EntitySkeleton skeleton = (EntitySkeleton) arrow.shootingEntity;
                NBTTagCompound tagCompound = skeleton.getEntityData();
                int attackCount = tagCompound.getInteger("AttackCount");
                tagCompound.setInteger("AttackCount", attackCount + 1);
                System.out.println("공격횟수"+attackCount);
                if (attackCount % 3 == 0) {
                    skeleton.attackEntityWithRangedAttack(skeleton.getAttackTarget(), ItemBow.getArrowVelocity(skeleton.getItemInUseMaxCount()));

                    EntityLivingBase attackTarget = skeleton.getAttackTarget();
                    EntityZombie zombie = new EntityZombie(event.getWorld());
                    zombie.setPosition(attackTarget.posX + HardCore.rand(3), attackTarget.posY, attackTarget.posZ + HardCore.rand(3));
                    skeleton.attackEntityWithRangedAttack(zombie, ItemBow.getArrowVelocity(skeleton.getItemInUseMaxCount()));
                }
            }
        }
    }

    @SubscribeEvent
    public void explosionEvent(ExplosionEvent event) {
        EntityLivingBase livingBase = event.getExplosion().getExplosivePlacedBy();
        Random random = livingBase.getEntityWorld().rand;
        //폭발을 일으킨 엔티티를 가져옵니다. TNT 같은 경우 null을 반환합니다.
        if (livingBase instanceof EntityCreeper && random.nextInt(10) == 0) {
            //크리퍼가 폭발했으니 다시 새로 소환합니다.
            EntityCreeper creeper = new EntityCreeper(event.getWorld());
            creeper.setPosition(livingBase.posX, livingBase.posY, livingBase.posZ);
            event.getWorld().spawnEntityInWorld(creeper);
            creeper.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40));
        }
    }
}
