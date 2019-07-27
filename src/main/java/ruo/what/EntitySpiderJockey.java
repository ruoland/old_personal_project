package ruo.what;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntitySpiderJockey extends EntitySpider {

    public EntitySpiderJockey(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (getAttackTarget() != null) {
            EntityLivingBase target = getAttackTarget();
            if (getPassengers().size() > 0 && getDistanceToEntity(target) < 10) {
                Entity entity = getPassengers().get(0);
                entity.dismountRidingEntity();
                entity.setVelocity(getLookVec().xCoord * 1.5, (target.posY - entity.posY) * 1.5, getLookVec().zCoord * 1.5);
            }
        }
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        int randomSpawn = rand.nextInt(4);
        EntityCreeper creeper = new EntityCreeper(worldObj);
        creeper.setPosition(posX, posY, posZ);
        worldObj.spawnEntityInWorld(creeper);
        creeper.startRiding(getLastPassenger(), true);
        System.out.println("스폰됨" + randomSpawn + " - " + posX + " - " + posY + " - " + posZ);

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
        } else
            return this;
    }

    public boolean passenger(Entity passenger) {
        return passenger.getPassengers().size() > 0;
    }
}
