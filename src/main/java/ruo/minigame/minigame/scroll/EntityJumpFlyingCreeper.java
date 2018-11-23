package ruo.minigame.minigame.scroll;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.map.EntityFlyNPC;
import ruo.minigame.map.TypeModel;

public class EntityJumpFlyingCreeper extends EntityFlyNPC {
    private EntityLivingBase attackTarget;
    public int arriveCooltime;

    public EntityJumpFlyingCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
        this.setElytra(true);
        this.setRotate(0, 0, 0);
        isFly = true;
        setSpeed(5);
        setSize(1,0.4F);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.isFireDamage())
            return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
    }

    protected void initEntityAI() {
        //this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        //this.tasks.addTask(1, new EntityAIFlyingMove(this));
        //this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        //his.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (arriveCooltime > 0)
            arriveCooltime--;

        if (getAttackTarget() != null && attackTarget == null && noTarget()) {
            if(getAttackTarget() instanceof EntityWither)
            {
                EntityWither wither = (EntityWither) getAttackTarget();
                if(wither.getInvulTime() > 0)
                    return;
            }
            attackTarget = getAttackTarget();
            setTarget(attackTarget, 1);
            System.out.println("타겟 설정함");
        }
    }

    @Override
    public void targetMove() {
        super.targetMove();
        if(attackTarget != null && attackTarget.isDead){
            this.targetRemove();
        }
        for(int i=0;i<10;i++){
            BlockPos pos = getPosition().add(0,-i,0);
            Block block = worldObj.getBlockState(pos).getBlock();
            if(block == Blocks.LAVA || block == Blocks.FLOWING_LAVA){
                worldObj.setBlockToAir(pos);
            }
        }
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        if (attackTarget != null) {
            //this.worldObj.createExplosion(this, posX, posY, posZ, 5, true);
            attackTarget = null;
            this.setDead();
        } else
            arriveCooltime = 60;
    }
}
