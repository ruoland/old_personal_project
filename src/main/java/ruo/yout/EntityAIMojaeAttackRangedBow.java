package ruo.yout;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import org.lwjgl.Sys;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EntityAIMojaeAttackRangedBow extends EntityAIAttackRangedBow {
    private EntitySkeleton skeleton;
    private int attackTime;
    public EntityAIMojaeAttackRangedBow(EntitySkeleton skeleton, double speedAmplifier, int delay, float maxDistance) {
        super(skeleton, speedAmplifier, delay, maxDistance);
        this.skeleton = skeleton;
        attackTime = Mojae.skelDelay;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        attackTime --;
        if(attackTime  <= 0) {
            setAttackCooldown(Mojae.skelDelay);
            if (skeleton.getAttackTarget() != null) {
                if(skeleton.getAttackTarget() instanceof EntityWither){
                    EntityWither wither = (EntityWither) skeleton.getAttackTarget();
                    if(wither.getInvulTime() > 0){
                        return;
                    }
                }
                int i = skeleton.getItemInUseMaxCount();
                this.skeleton.setActiveHand(EnumHand.MAIN_HAND);
                this.skeleton.attackEntityWithRangedAttack(skeleton.getAttackTarget(), ItemBow.getArrowVelocity(i));
                attackTime = Mojae.skelDelay;
            }
        }
    }
}
