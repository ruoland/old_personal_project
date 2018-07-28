package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraPumpkin extends EntityDefaultNPC {
    private SpawnDirection spawnDirection;
    private static final DataParameter<Boolean> ATTACK_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FIRE_ATTACK = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    public EntityElytraPumpkin(World world) {
        super(world);
        this.setBlockMode(Blocks.PUMPKIN);
        this.setSize(1,5);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ATTACK_MODE, true);
        dataManager.register(FIRE_ATTACK, false);
    }

    public boolean isAttackMode() {
        return FakePlayerHelper.fakePlayer != null && dataManager.get(ATTACK_MODE);
    }

    public void setAttack(boolean fire) {
        dataManager.set(ATTACK_MODE, fire);
    }

    public boolean isFireAttack() {
        return isAttackMode() && dataManager.get(FIRE_ATTACK);
    }

    public void setFireAttack(boolean fire) {
        dataManager.set(FIRE_ATTACK, fire);
    }

    public SpawnDirection getSpawnDirection() {
        return spawnDirection;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2000000000);
    }

    public void setDirection(SpawnDirection spawn) {
        spawnDirection = spawn;
    }

    int attackCooldown = 40;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (fakePlayer != null && spawnDirection != null) {
            if (spawnDirection == SpawnDirection.FORWARD) {
                if (fakePlayer.getDistance(this.getPositionVector()) > 10) {
                    this.setVelocity(getXZ(spawnDirection, 0.02, false));
                    System.out.println("플레이어로부터 너무 멀어져 플레이어에게 이동 중" + fakePlayer.getDistance(getPositionVector()));
                } else if (fakePlayer.getDistance(this.getPositionVector()) < 5) {
                    this.setVelocity(getXZ(spawnDirection, -0.02, false));
                    System.out.println("플레이어와 너무 가까워 멀리이동 중" + fakePlayer.getDistance(getPositionVector()));
                }

            }
            faceEntity(fakePlayer, 360, 360);
            this.motionY = 0;
        }
    }

    public EntityElytraBullet spawnBullet() {
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        EntityElytraBullet elytraBullet = new EntityElytraBullet(worldObj);
        elytraBullet.setPosition(posX, posY, posZ);
        elytraBullet.setTarget(fakePlayer.posX, posY, fakePlayer.posZ);
        worldObj.spawnEntityInWorld(elytraBullet);
        elytraBullet.setDamage(5);
        if (isFireAttack())
            elytraBullet.setFire(100);
        return elytraBullet;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("FIRE_ATTACK", isFireAttack());
        compound.setBoolean("ATTACK_MODE", isAttackMode());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setFireAttack(compound.getBoolean("FIRE_ATTACK"));
        setAttack(compound.getBoolean("ATTACK_MODE"));
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if(!(entityIn instanceof EntityElytraPumpkin) && !(entityIn instanceof EntityElytraBullet)){
            super.collideWithEntity(entityIn);
        }
        if(entityIn instanceof EntityFakePlayer)
        entityIn.attackEntityFrom(DamageSource.fallingBlock, 2);
    }

}
