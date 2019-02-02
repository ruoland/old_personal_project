package map.mafence.tower;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import map.mafence.EntityTower;

public class EntityZombieTower extends EntityTower {

    public EntityZombieTower(World worldIn) {
        super(worldIn);
        maxAttackDelay = 100;
    }

    @Override
    public void towerAttack(EntityLivingBase target) {
        super.towerAttack(target);
        for(int i=0;i<3;i++){
            EntityMiniZombie miniZombie = new EntityMiniZombie(worldObj);
            miniZombie.setPosition(getPosition());
            miniZombie.setAttackTarget(getAttackTarget());
            worldObj.spawnEntityInWorld(miniZombie);
        }
    }
}
