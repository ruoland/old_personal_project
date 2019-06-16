package ruo.what;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import olib.map.EntityDefaultNPC;

import java.util.List;

public class EntityEnderBlock extends EntityDefaultNPC {
    public EntityEnderBlock(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.GRASS);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        List entityList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(0.5,0.5,0.5));
        if(!entityList.isEmpty()){
            if(getCurrentBlock() == Blocks.TNT){
                this.worldObj.createExplosion(this, posX, posY, posZ, 3, false);
            }
            this.setDead();
            this.setHealth(0);
        }
    }
}
