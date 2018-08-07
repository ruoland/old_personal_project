package ruo.asdf;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntityFlyingZombie extends EntityDefaultNPC {

    public EntityFlyingZombie(World worldIn) {
        super(worldIn);
        this.setElytra(true);
        this.setModel(TypeModel.ZOMBIE);
        experienceValue = 30;
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setPosition(getPosition().add(0,10,0));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        motionX+=this.getX(SpawnDirection.FORWARD, 0.01, false);
        motionZ+=this.getZ(SpawnDirection.FORWARD, 0.01, false);
        motionY = 0;
        if(isAttackTargetPlayer()){
            if(getAttackTarget().getDistance(posX,getAttackTarget().posY, posZ) > 5) {
                motionX = getAttackTarget().posX - posX;
                motionZ = getAttackTarget().posZ - posZ;
            }else{
                motionZ = 0;
                motionX = 0;
            }
        }
    }
}
