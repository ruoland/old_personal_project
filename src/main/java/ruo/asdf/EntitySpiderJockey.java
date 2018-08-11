package ruo.asdf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
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
            if (getPassengers().size() > 0 && getDistanceToEntity(target) < 5) {
                Entity entity = getPassengers().get(0);
                entity.dismountRidingEntity();
                entity.setVelocity(getLookVec().xCoord * 1.5, target.posY - entity.posY, getLookVec().zCoord * 1.5);
            }
        }
    }

    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() +2;
    }


    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        int randomSpawn = rand.nextInt(4);
        switch (randomSpawn) {
            case 0: {
                int random = rand.nextInt(20);
                for (int i = 0; i < random; i++) {
                    EntityZombie zombie = new EntityZombie(worldObj);
                    zombie.setPosition(posX, posY, posZ);
                    worldObj.spawnEntityInWorld(zombie);
                    zombie.startRiding(getLastPassenger(), true);
                }
            }
            case 1: {
                EntityCreeper creeper = new EntityCreeper(worldObj);
                creeper.setPosition(posX, posY, posZ);
                worldObj.spawnEntityInWorld(creeper);
                creeper.startRiding(getLastPassenger(), true);
            }
            case 2: {
                EntityCreeper creeper = new EntityCreeper(worldObj);
                creeper.setPosition(posX, posY, posZ);
                worldObj.spawnEntityInWorld(creeper);
                getLastPassenger().startRiding(creeper, true);
            }
            case 3: {
                int random = rand.nextInt(20);
                for (int i = 0; i < random; i++) {
                    EntitySkeleton skeleton = new EntitySkeleton(worldObj);
                    skeleton.setPosition(posX, posY, posZ);
                    worldObj.spawnEntityInWorld(skeleton);
                    skeleton.startRiding(getLastPassenger(), true);
                    skeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
                }
            }
        }
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
        }
        else
            return this;
    }

    public boolean passenger(Entity passenger) {
        return passenger.getPassengers().size() > 0;
    }
}
