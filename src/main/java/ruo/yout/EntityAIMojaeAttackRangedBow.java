package ruo.yout;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import org.lwjgl.Sys;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EntityAIMojaeAttackRangedBow extends EntityAIAttackRangedBow {
    private EntitySkeleton skeleton;
    private int attackTime = 20;
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
                int i = skeleton.getItemInUseMaxCount();
                this.skeleton.setActiveHand(EnumHand.MAIN_HAND);
                this.skeleton.attackEntityWithRangedAttack(skeleton.getAttackTarget(), ItemBow.getArrowVelocity(i));
                attackTime = Mojae.skelDelay;
            }
        }
    }
}
