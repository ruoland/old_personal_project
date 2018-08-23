package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraPumpkin extends EntityDefaultNPC {
    private SpawnDirection spawnDirection;
    //앞으로 이동함
    private static final DataParameter<Boolean> FORWARD_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> ATTACK_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FIRE_ATTACK = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    public EntityElytraPumpkin(World world) {
        super(world);
        this.setBlockMode(Blocks.PUMPKIN);
        this.setSize(1, 5);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(FORWARD_MODE, false);
        dataManager.register(ATTACK_MODE, true);
        dataManager.register(FIRE_ATTACK, false);
    }

    public boolean isAttackMode() {
        return dataManager.get(ATTACK_MODE);
    }

    public EntityElytraPumpkin setAttack(boolean attack) {
        dataManager.set(ATTACK_MODE, attack);
        return this;
    }

    public boolean isFireAttack() {
        return isAttackMode() && dataManager.get(FIRE_ATTACK);
    }

    public EntityElytraPumpkin setFireAttack(boolean fire) {
        if (fire)
            setAttack(true);
        dataManager.set(FIRE_ATTACK, fire);
        return this;
    }

    public boolean isForwardMode() {
        return dataManager.get(FORWARD_MODE);
    }

    public EntityElytraPumpkin setForwardMode(boolean forwardMode) {
        dataManager.set(FORWARD_MODE, forwardMode);
        return this;
    }

    /**
     * 플레이어 앞에 소환되는지 플레이어 옆에 소환 됐는지를 표시함
     */
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
        System.out.println(spawnDirection);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(FakePlayerHelper.fakePlayer == null)
        {
            this.setDead();
            return;
        }
        if (isServerWorld() && isForwardMode()) {
            this.setVelocity(getXZ(getSpawnDirection().simple(), 0.1, false));
        }
        faceEntity(FakePlayerHelper.fakePlayer, 360, 360);
        this.motionY = 0;
    }



    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("FIRE_ATTACK", isFireAttack());
        compound.setBoolean("ATTACK_MODE", isAttackMode());
        compound.setBoolean("FORWARD_MODE", isForwardMode());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setFireAttack(compound.getBoolean("FIRE_ATTACK"));
        setAttack(compound.getBoolean("ATTACK_MODE"));
        setForwardMode(compound.getBoolean("FORWARD_MODE"));
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityElytraPumpkin) && !(entityIn instanceof EntityElytraBullet)) {
            super.collideWithEntity(entityIn);
        }
        if (entityIn instanceof EntityFakePlayer)
            entityIn.attackEntityFrom(DamageSource.fallingBlock, 2);
    }

}
