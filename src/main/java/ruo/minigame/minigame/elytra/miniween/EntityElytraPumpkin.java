package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ATTACK_MODE, true);
        dataManager.register(FIRE_ATTACK, false);
    }

    public boolean isAttackMode() {
        return dataManager.get(ATTACK_MODE);
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
            if (fakePlayer.getDistance(this.getPositionVector()) > 10) {
                this.setVelocity(getXZ(spawnDirection.reverse(), 0.01, false));
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
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        //super.onCollideWithPlayer(entityIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    @Override
    protected void collideWithNearbyEntities() {
        //super.collideWithNearbyEntities();
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

}
