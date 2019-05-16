package ruo.awild;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.World;
import olib.api.EntityAPI;
import olib.api.WorldAPI;
import ruo.awild.ai.EntityAIFindSound;

import java.util.List;

public class EntityWildEnderman extends EntityEnderman {

    public EntityWildEnderman(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        //this.tasks.addTask(0, new EntityAIAvoidEntityCreeper(this, 10.0F, 3D, 2.4D));
        this.tasks.addTask(5, new EntityAIFindSound(this));
    }

    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            if(passenger instanceof EntityCreeper){
                passenger.setPosition(this.posX+EntityAPI.lookX(this,1), this.posY+1, this.posZ+EntityAPI.lookZ(this, 1));
            }
            else
                passenger.setPosition(this.posX, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return super.attackEntityAsMob(entityIn);
    }

    private int findCreeperDelay = 100, dismountCreeper = 0;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(getAttackTarget() != null){
            if(getPassengers().size() != 0) {
                this.teleportToEntity(getAttackTarget());
                getPassengers().get(0).dismountRidingEntity();;
                dismountCreeper = 20;
            }
        }
        if(dismountCreeper > 0){//크리퍼를 내려놓고 1초뒤에 텔레포트함
            dismountCreeper--;
            if(dismountCreeper == 0){
                for (int i = 0; i < 64; ++i)
                {
                    if (this.teleportRandomly())
                    {
                        break;
                    }
                }
            }
        }
        if(getPassengers().size() == 0) {
            findCreeperDelay--;
            if (findCreeperDelay < 0) {
                List<EntityCreeper> livings = EntityAPI.getEntity(WorldAPI.getWorld(), getEntityBoundingBox().expandXyz(32), EntityCreeper.class);
                for (EntityCreeper creeper : livings)
                {
                    if(!creeper.isRidingSameEntity(this)) {
                        this.teleportToEntity(creeper);
                        creeper.startRiding(this, true);
                        break;
                    }
                }
                findCreeperDelay = 100;
            }
        }
    }
}

