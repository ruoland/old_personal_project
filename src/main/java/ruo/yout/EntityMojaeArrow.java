package ruo.yout;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import olib.api.WorldAPI;

public class EntityMojaeArrow extends EntityTippedArrow {
    public EntityMojaeArrow(World worldIn) {
        super(worldIn);
    }
    int xTile, yTile, zTile;
    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if(raytraceResultIn.entityHit == null)
            setDead();
        if (raytraceResultIn.entityHit instanceof EntityEnderman || raytraceResultIn.entityHit instanceof EntityWither || raytraceResultIn.entityHit instanceof EntityDragon || raytraceResultIn.entityHit instanceof EntityDragonPart) {

            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int dam = MathHelper.ceiling_double_int((double) f * getDamage());

            if (this.getIsCritical()) {
                dam += this.rand.nextInt(dam / 2);
            }
            raytraceResultIn.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(WorldAPI.getPlayer()), (float) dam);
        } else
            super.onHit(raytraceResultIn);
    }
}
