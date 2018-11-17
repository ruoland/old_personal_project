package ruo.yout;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;
import org.lwjgl.Sys;

public class EntityMoJaeCreeper extends EntityCreeper {
    public EntityMoJaeCreeper(World worldIn) {
        super(worldIn);
    }

    public boolean isImmuneToExplosions()
    {
        return true;
    }
    @Override
    public void applyEntityCollision(Entity entityIn) {
        //super.applyEntityCollision(entityIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
        if(!(entityIn instanceof EntitySkeleton) && !(entityIn instanceof EntityMoJaeCreeper)) {
            this.setVelocity(0, 0, 0);
            this.worldObj.createExplosion(this, posX, posY, posZ, 3F, false);
            this.setDead();
        }
    }
}
