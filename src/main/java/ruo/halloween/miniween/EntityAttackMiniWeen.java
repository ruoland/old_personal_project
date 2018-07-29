package ruo.halloween.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import ruo.halloween.EntityWeen;
import ruo.halloween.WeenEffect;
import ruo.halloween.WeenEvent;
import ruo.minigame.api.WorldAPI;

import java.util.List;

public class EntityAttackMiniWeen extends EntityMiniWeen {
    private boolean isAttackReverse;
    private float explosionStrength = 1.5F;
    private boolean targetExplosion;
    private boolean targetStop;//타겟에 도착하면 멈춤

    public EntityAttackMiniWeen(World worldIn) {
        super(worldIn);
        this.setSize(1F, 1F);
        this.setDeathTimer(1000);
    }

    public EntityAttackMiniWeen(World worldIn, EntityWeen ween) {
        this(worldIn);
        this.ween = ween;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        System.out.println("미니윈이 플레이어와 충돌해 폭발함");
        this.worldObj.createExplosion(this, posX, posY, posZ, explosionStrength, false);
        this.setDead();
        targetVec = new Vec3d(0, 0, 0);
    }

    /**
     * 목적지에 도달한 경우 폭발함
     */
    public void setTargetExplosion(boolean targetExplosion) {
        this.targetExplosion = targetExplosion;
    }

    public void setTargetStop(boolean targetStop) {
        this.targetStop = targetStop;
    }

    public void setExplosionStrength(float explosionStrength) {
        this.explosionStrength = explosionStrength;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityPlayer) {
            System.out.println("미니윈이 반사됨" + source.damageType);
            if (ween != null) {
                targetVec = new Vec3d(ween.posX, ween.posY + 5, ween.posZ);
                isAttackReverse = true;
            }
        }
        if (source.getEntity() instanceof EntityWeen || source == DamageSource.fall)
            return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
        if (targetVec != null && target != null) {
            addRotate(rand.nextInt(10), rand.nextInt(10), rand.nextInt(10));
            this.setVelocity(targetVec.xCoord, targetVec.yCoord, targetVec.zCoord);
            if (isAttackReverse && this.getDistance(target.xCoord, target.yCoord, target.zCoord) < 10) {
                this.setDead();
                target = new Vec3d(0, 0, 0);
                System.out.println("반사된 미니윈이 윈 근처에 도달해 터짐");
                explosion();
            }
            if (targetExplosion && this.getDistance(target.xCoord, target.yCoord, target.zCoord) < 0.8) {
                this.setDead();
                explosion();
            }

        }
    }

    /**
     * 윈이 폭발 데미지를 받지 않아서 만든 메서드
     */
    public void explosion(){
        List<EntityWeen> list = worldObj.getEntitiesWithinAABB(EntityWeen.class, getCollisionBoundingBox().expand(3,3,3));
        System.out.println(list);
        Explosion explosion = this.worldObj.createExplosion(this, posX, posY, posZ, explosionStrength, false);

        for(EntityWeen ween : list){
            ween.attackEntityFrom(DamageSource.causeExplosionDamage(explosion), 5);
        }


    }
}
