package ruo.asdf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.EntityFlyNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntityMissileCreeper extends EntityFlyNPC {
    private EntityLivingBase attackTarget;

    public EntityMissileCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
        this.setRotate(0, 0, 0);

    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (getAttackTarget() != null && attackTarget == null) {
            attackTarget = getAttackTarget();
            if (!isArrive()) {
                setTarget(posX, posY + 16, posZ);
                System.out.println("타겟 설정함");
            }
        }
        if(!noTarget()){
            this.addSpeed(getSpeed() * 0.07F);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isArrive()) {
            this.worldObj.createExplosion(this, posX, posY, posZ, 5, true);
            this.setDead();
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        this.setPosition(attackTarget.posX, attackTarget.posY + 16, attackTarget.posZ);
        this.setRotate(0, 180, 0);
    }
}
