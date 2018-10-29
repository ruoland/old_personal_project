package ruo.yout;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;

public class EntityMoJaeCreeper extends EntityCreeper {
    public EntityMoJaeCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(!(entityIn instanceof EntitySkeleton)) {
            this.setVelocity(0, 0, 0);
            this.worldObj.createExplosion(this, posX, posY, posZ, 3F, false);
            this.setDead();
        }
    }
}
