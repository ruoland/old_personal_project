package ruo.minigame.minigame.elytra_scroll;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityElytraArrow extends EntityTippedArrow {
    public EntityElytraArrow(World worldIn) {
        super(worldIn);
    }

    public EntityElytraArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        this.setDead();
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        this.setDead();
    }

    private int count = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote) {
            count++;
            if (count == 40) {
                setDead();
            }
        }
    }
}
