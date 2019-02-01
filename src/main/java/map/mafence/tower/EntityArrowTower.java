package map.mafence.tower;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import map.mafence.EntityTower;

public class EntityArrowTower extends EntityTower {
    public EntityArrowTower(World worldIn) {
        super(worldIn);
    }

    @Override
    public void towerAttack(EntityLivingBase target)
    {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.worldObj, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entitytippedarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        entitytippedarrow.setThrowableHeading(d0, d1 + d3 * 0.2, d2, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
        entitytippedarrow.setDamage(getTowerDamage());

        boolean flag = this.isBurning();

        if (flag)
        {
            entitytippedarrow.setFire(100);
        }

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F);
        this.worldObj.spawnEntityInWorld(entitytippedarrow);
    }
}
