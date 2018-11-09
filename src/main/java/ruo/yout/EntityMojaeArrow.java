package ruo.yout;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;

public class EntityMojaeArrow extends EntityTippedArrow {
    public EntityMojaeArrow(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.entityHit instanceof EntityWither) {
            EntityWither wither = (EntityWither) raytraceResultIn.entityHit;
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int dam = MathHelper.ceiling_double_int((double) f * getDamage());

            if (this.getIsCritical()) {
                dam += this.rand.nextInt(dam / 2 + 2);
            }
            wither.attackEntityFrom(DamageSource.causePlayerDamage(WorldAPI.getPlayer()), (float) dam);
        } else
            super.onHit(raytraceResultIn);
        System.out.println(raytraceResultIn.entityHit);
    }
}
