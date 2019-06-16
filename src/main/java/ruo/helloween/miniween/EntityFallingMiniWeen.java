package ruo.helloween.miniween;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import olib.map.EntityDefaultNPC;


public class EntityFallingMiniWeen extends EntityDefaultNPC {

    public EntityFallingMiniWeen(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.LIT_PUMPKIN);
        this.setRotate(0, -90, 0);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        this.worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
        this.setDead();
    }
}

