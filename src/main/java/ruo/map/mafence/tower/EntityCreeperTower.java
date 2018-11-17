package ruo.map.mafence.tower;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import ruo.map.mafence.EntityTower;

public class EntityCreeperTower extends EntityTower {
    public EntityCreeperTower(World worldIn) {
        super(worldIn);
        this.maxAttackDelay = 60;
    }

    @Override
    public void towerAttack(EntityLivingBase target) {
        super.towerAttack(target);
        EntityCreeperTower creeperTower = new EntityCreeperTower(worldObj);
        creeperTower.setPosition(getAttackTarget().getPositionVector().addVector(0,4,0));
        worldObj.spawnEntityInWorld(creeperTower);
        creeperTower.addRotate(0,90,0);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        if(getRotateY() == 90){
            worldObj.createExplosion(this, posX, posY, posZ, 2.5F * getTowerLevel(), false);
        }

    }
}
