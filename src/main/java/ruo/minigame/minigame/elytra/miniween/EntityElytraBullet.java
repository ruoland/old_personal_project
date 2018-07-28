package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ruo.halloween.miniween.EntityMiniWeen;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraBullet extends EntityDefaultNPC {

    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityElytraBullet.class, DataSerializers.FLOAT);
    private Vec3d targetVec;

    public EntityElytraBullet(World world) {
        super(world);
        this.setBlockMode(Blocks.PUMPKIN);
        this.setScale(0.5F, 0.5F, 0.5F);
        this.setSize(0.5F, 0.5F);
        this.setDeathTimer(200);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DAMAGE, 3F);
    }

    public void setTarget(double x, double y, double z) {
        targetVec = new Vec3d(x - posX, y - posY, z - posZ).normalize().scale(0.5);
    }

    public void setDamage(float damage) {
        dataManager.set(DAMAGE, damage);
    }

    public float getDamage() {
        return dataManager.get(DAMAGE);
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getEntity() instanceof EntityArrow || source.isProjectile() || source.getDamageType().equalsIgnoreCase("arrow"))
            this.setDead();

        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (targetVec != null)
            this.setVelocity(targetVec.xCoord, targetVec.yCoord, targetVec.zCoord);
        motionY = 0;
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith();
    }

    @Override
    protected void collideWithNearbyEntities() {
        super.collideWithNearbyEntities();
    }
    @Override
    protected void collideWithEntity(Entity entityIn) {
        if(!(entityIn instanceof EntityElytraPumpkin)){
            super.collideWithEntity(entityIn);
        }

        if (entityIn instanceof EntityFakePlayer) {
            if(isBurning()){
                entityIn.setFire(2);
            }
            entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), getDamage());
            this.setDead();
        }
    }

}
