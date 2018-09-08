package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.MiniGame;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraBullet extends EntityElytraPumpkinAttack {

    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityElytraBullet.class, DataSerializers.FLOAT);

    public EntityElytraBullet(World world) {
        super(world);
        this.setBlockMode(Blocks.PUMPKIN);
        this.setScale(0.5F, 0.5F, 0.5F);
        this.setSize(0.5F, 0.5F);
        this.setDeathTimer(60);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DAMAGE, 3F);
    }

    @Override
    public void targetArrive() {
        if(isTNTMode()){
            worldObj.createExplosion(this, posX, posY, posZ, 1.5F, true);
            setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityArrow || source.isProjectile() || source.getDamageType().equalsIgnoreCase("arrow")) {
            this.setDead();
            System.out.println("화살에 맞아 죽음");
        }

        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        motionY = 0;
        if(isWaterMode()) {
            this.worldObj.setBlockState(this.getPosition(), Blocks.WATER.getDefaultState());
            TickRegister.register(new AbstractTick(20, false) {
                @Override
                public void run(TickEvent.Type type) {
                    worldObj.setBlockToAir(getPosition());
                }
            });
        }
        if(!MiniGame.elytra.isStart())
            setDead();
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityElytraPumpkin)) {
            super.collideWithEntity(entityIn);
        } else {
            return;
        }

        if (entityIn instanceof EntityFakePlayer) {
            if (isBurning()) {
                entityIn.setFire(2);
            }
            if(isTNTMode()){
                worldObj.createExplosion(this, posX, posY, posZ, 1.5F, true);
            }
            entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), getDamage());
            this.setDead();
        }
    }
    public void setDamage(float damage) {
        dataManager.set(DAMAGE, damage);
    }

    public float getDamage() {
        return dataManager.get(DAMAGE);
    }

}
