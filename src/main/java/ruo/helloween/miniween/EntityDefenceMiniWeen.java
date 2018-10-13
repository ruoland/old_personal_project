package ruo.helloween.miniween;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.helloween.EntityWeen;

public class EntityDefenceMiniWeen extends EntityMiniWeen {
    public boolean goWeen;

    public EntityDefenceMiniWeen(World worldIn) {
        super(worldIn);
        this.setDeathTimer(300);
    }

    public EntityDefenceMiniWeen(World worldIn, EntityWeen ween) {
        this(worldIn);
        this.ween = ween;
        if (ween == null)
            this.setDead();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        ((EntityLivingBase) this).knockBack(entityIn, 0.2F, entityIn.posX - this.posX, entityIn.posZ - this.posZ);
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (getTargetPosition() != null) {
            if (goWeen && this.isTargetArrive()) {
                this.setDead();
            }
        }
    }
}
