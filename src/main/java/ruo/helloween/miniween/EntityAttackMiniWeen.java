package ruo.helloween.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;
import ruo.helloween.EntityWeen;

import javax.annotation.Nullable;
import java.util.List;

public class EntityAttackMiniWeen extends EntityMiniWeen {
    private boolean isAttackReverse;
    private float explosionStrength = 2F;
    private boolean targetExplosion;
    public boolean targetDead = true;//타겟에 도착하면  죽음
    public EntityAttackMiniWeen(World worldIn) {
        super(worldIn);
        this.setSize(1F, 1F);
        this.setDeathTimer(1000);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        System.out.println("미니윈이 플레이어와 충돌해 폭발함");
        this.worldObj.createExplosion(this, posX, posY, posZ, explosionStrength, false);
        this.setDead();
        setTarget(0, 0, 0, 0);
    }

    /**
     * 목적지에 도달한 경우 폭발함
     */
    public void setTargetExplosion(boolean targetExplosion) {
        this.targetExplosion = targetExplosion;
        targetDead = targetExplosion;
    }


    public void setExplosionStrength(float explosionStrength) {
        this.explosionStrength = explosionStrength;
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityPlayer && (getPattern() == 2 || getPattern() == 4)) {
            System.out.println("미니윈이 반사됨" + source.damageType);
            this.setDistance(8);
            setAttackReverse(true);
        }
        if (source.getEntity() instanceof EntityWeen || source == DamageSource.fall)
            return false;

        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
    }

    public void setAttackReverse(boolean attackReverse) {
        this.setTarget(getSpawnPosVec());
        isAttackReverse = attackReverse;
        if (isAttackReverse)
            setTargetExplosion(true);
    }

    @Override
    public void targetArrive() {
        super.targetArrive();

        if (targetExplosion) {
            List<EntityWeen> list = worldObj.getEntitiesWithinAABB(EntityWeen.class, getCollisionBoundingBox().expand(3, 3, 3));
            Explosion explosion = this.worldObj.createExplosion(this, posX, posY, posZ, explosionStrength, false);
            System.out.println(explosionStrength);
            for (EntityWeen ween : list) {
                ween.attackEntityFrom(DamageSource.causeExplosionDamage(explosion), 5);
            }
        }
        if (targetDead)
            setDead();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
        if (!noTarget()) {
            addRotate(rand.nextInt(10), rand.nextInt(10), rand.nextInt(10));
        }
    }


    public void startTwoPattern() {
        setTarget(posX, posY + 12, posZ);//일단 높이 올림
        setSpawnXYZ(getTargetPosition());
        setTargetExplosion(false);
        setTargetSpeed(1.5);
        isFly = true;
        setDeathTimer(300);
        setDistance(1);
        setPattern(2);
        TickRegister.register(new AbstractTick(40, false) {
            @Override
            public void run(TickEvent.Type type) {
                setTarget(WorldAPI.x() + WorldAPI.rand(3), WorldAPI.y() + WorldAPI.getPlayer().eyeHeight,
                        WorldAPI.z() + WorldAPI.rand(3));
                setTargetExplosion(true);
                setExplosionStrength(3F);
            }
        });

    }

}
