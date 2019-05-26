package map.escaperoom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityRoomMonsterTower extends EntityRoomMonster {
    public EntityRoomMonsterTower(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(entityIn instanceof EntityRoomBlock){
            while(getLastPassenger() != this) {
                getLastPassenger().dismountRidingEntity();
            }
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        setHealth(0);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        for (int i = 0; i < 20; i++) {
            EntityZombie zombie = new EntityZombie(worldObj);
            zombie.setPosition(posX, posY, posZ);
            worldObj.spawnEntityInWorld(zombie);
            zombie.startRiding(getLastPassenger(), true);
        }
        return super.onInitialSpawn(difficulty, livingdata);

    }

    public Entity getLastPassenger() {
        if (getPassengers().size() > 0) {
            Entity entity = getPassengers().get(0);
            for (int i = 0; i < 20; i++) {
                if (entity.getPassengers().size() > 0)
                    entity = entity.getPassengers().get(0);
            }
            return entity;
        }
        else
            return this;
    }
}
