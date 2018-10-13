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
import org.lwjgl.Sys;
import ruo.minigame.MiniGame;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.Direction;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.EntityDefaultNPC;
import scala.xml.dtd.EntityDef;

public class EntityElytraPumpkin extends EntityDefaultNPC {
    public int attackCooldown = 200, defaultCooldown = 30;
    private double returnX, returnY, returnZ;
    private Direction spawnDirection;
    private static final DataParameter<Boolean> TNT_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WATER_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MOVE_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TELEPORT_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    //앞으로 이동함
    private static final DataParameter<Boolean> FORWARD_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    //불릿을 발사하는가
    private static final DataParameter<Boolean> ATTACK_MODE = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    //불타는 불릿을 발사하는가
    private static final DataParameter<Boolean> FIRE_ATTACK = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    //시간이 지나서 뒤로 후퇴하는가
    private static final DataParameter<Boolean> IS_RETURN = EntityDataManager.createKey(EntityElytraPumpkin.class, DataSerializers.BOOLEAN);

    public EntityElytraPumpkin(World world) {
        super(world);
        this.setBlockMode(Blocks.PUMPKIN);
        this.setSize(1, 5);
        this.setRotate(0,90,0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(FORWARD_MODE, false);
        dataManager.register(ATTACK_MODE, true);
        dataManager.register(FIRE_ATTACK, false);
        dataManager.register(IS_RETURN, false);
        dataManager.register(WATER_MODE, false);
        dataManager.register(MOVE_MODE, false);
        dataManager.register(TELEPORT_MODE, false);
        dataManager.register(TNT_MODE, false);

    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2000000000);
    }

    public void setDirection(Direction spawn) {
        spawnDirection = spawn;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(getHealth() - amount <= 0){
            if(rand.nextInt(5) == 0) {
                EntityElytraChest elytraChest = new EntityElytraChest(worldObj);
                elytraChest.setPosition(getPositionVector());
                elytraChest.updateSpawnPosition();
                if(isServerWorld())
                worldObj.spawnEntityInWorld(elytraChest);
            }
        }
        return super.attackEntityFrom(source, amount);

    }

    private int moveCooldown, teleportXZ;
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(FakePlayerHelper.fakePlayer == null)
        {
            this.setDead();
            return;
        }
        if(getHealth() > 0) {
            if (isServerWorld() && isTeleportMode() && noTarget()) {
                moveCooldown++;
                if (moveCooldown >= 20) {
                    teleportXZ++;
                    moveCooldown = 0;
                    System.out.println("무브 XZ ++");
                }
                if(teleportXZ >= 3)
                    teleportXZ = 0;
                if (teleportXZ == 0) {
                    System.out.println("오른쪽으로 1 이동함");
                    this.setPosition(getSpawnPosHelper().getX(Direction.RIGHT, 1, true), posY, getSpawnPosHelper().getZ(Direction.RIGHT, 1, true));
                } else if (teleportXZ == 1) {
                    this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
                    System.out.println("가운데로 이동함");
                } else if (teleportXZ == 2) {
                    this.setPosition(getSpawnPosHelper().getX(Direction.LEFT, 1, true), posY, getSpawnPosHelper().getZ(Direction.LEFT, 1, true));
                    System.out.println("왼쪽으로 1 이동함");
                }
            }

            if (isMoveMode()) {
                moveCooldown++;
                if (moveCooldown == 40)
                    moveCooldown = 0;
                if (moveCooldown > 20) {
                    this.setVelocity(getX(Direction.RIGHT, 0.01, false), 0, getZ(Direction.RIGHT, 0.01, false));
                } else {
                    this.setVelocity(getX(Direction.LEFT, 0.01, false), 0, getZ(Direction.LEFT, 0.01, false));
                }
            }
            if (isServerWorld() && isForwardMode() && noTarget() && !isReturn())//페이크 뒤로 보냄
            {
                PosHelper posHelper = MiniGame.elytra.spawnPosHelper;
                this.setVelocity(posHelper.getX(spawnDirection.reverse().simple(), 0.1, false), 0, posHelper.getZ(spawnDirection.reverse().simple(), 0.1, false));
            }
            faceEntity(FakePlayerHelper.fakePlayer, 360, 360);
            this.motionY = 0;
        }
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        updateSpawnPosition();
        if(isReturn())
            setDead();
    }

    public double getReturnX() {
        return returnX;
    }

    public double getReturnY() {
        return returnY;
    }

    public double getReturnZ() {
        return returnZ;
    }

    public void setReturnPosition(double x, double y, double z) {
        returnX = x;
        returnY = y;
        returnZ = z;
    }

    public void setReturnPosition() {
        setReturnPosition(getSpawnX(), getSpawnY(), getSpawnZ());
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("FIRE_ATTACK", isFireAttack());
        compound.setBoolean("ATTACK_MODE", isAttackMode());
        compound.setBoolean("FORWARD_MODE", isForwardMode());
        compound.setBoolean("TNT_MODE", isTNTMode());
        compound.setBoolean("MOVE_MODE", isMoveMode());
        compound.setBoolean("TELEPORT_MODE", isTeleportMode());
        compound.setBoolean("WATER_MODE", isWaterMode());
        if(spawnDirection != null)
        compound.setString("SPAWN_DIRECTION", spawnDirection.name());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setFireAttack(compound.getBoolean("FIRE_ATTACK"));
        setAttackMode(compound.getBoolean("ATTACK_MODE"));
        setForwardMode(compound.getBoolean("FORWARD_MODE"));
        setReturn(compound.getBoolean("IS_RETURN"));
        setTNTMode(compound.getBoolean("TNT_MODE"));
        setMoveMode(compound.getBoolean("MOVE_MODE"));
        setTeleportMode(compound.getBoolean("TELEPORT_MODE"));
        setWaterMode(compound.getBoolean("WATER_MODE"));
        if(compound.hasKey("SPAWN_DIRECTION"))
        spawnDirection = Direction.valueOf(compound.getString("SPAWN_DIRECTION"));

    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityElytraPumpkin)) {
            super.collideWithEntity(entityIn);
        }
        if (entityIn instanceof EntityFakePlayer)
            entityIn.attackEntityFrom(DamageSource.fallingBlock, 2);
    }

    public EntityElytraPumpkin setFireAttack(boolean fire) {
        if (fire)
            setAttackMode(true);
        dataManager.set(FIRE_ATTACK, fire);
        return this;
    }

    public EntityElytraPumpkin setReturn(boolean retu) {
        dataManager.set(IS_RETURN, retu);
        return this;
    }

    public EntityElytraPumpkin setForwardMode(boolean forwardMode) {
        dataManager.set(FORWARD_MODE, forwardMode);
        return this;
    }

    public EntityElytraPumpkin setAttackMode(boolean attack) {
        dataManager.set(ATTACK_MODE, attack);
        return this;
    }

    public EntityElytraPumpkin setTeleportMode(boolean mode) {
        dataManager.set(TELEPORT_MODE, mode);
        return this;
    }

    public EntityElytraPumpkin setWaterMode(boolean mode) {
        dataManager.set(WATER_MODE, mode);
        if(mode){
            defaultCooldown = defaultCooldown * 2;
        }
        return this;
    }

    public EntityElytraPumpkin setMoveMode(boolean is) {
        this.dataManager.set(MOVE_MODE, is);

        return this;
    }

    public EntityElytraPumpkin setTNTMode(boolean is) {
        this.dataManager.set(TNT_MODE, is);
        return this;
    }

    public boolean isAttackMode() {
        return dataManager.get(ATTACK_MODE) && !(this instanceof EntityElytraBullet);
    }
    public boolean isReturn() {
        return dataManager.get(IS_RETURN);
    }

    public boolean isFireAttack() {
        return isAttackMode() && dataManager.get(FIRE_ATTACK);
    }

    public boolean isForwardMode() {
        return dataManager.get(FORWARD_MODE);
    }
    public boolean isTeleportMode() {
        return dataManager.get(TELEPORT_MODE);
    }

    public boolean isWaterMode() {
        return dataManager.get(WATER_MODE);
    }


    public boolean isMoveMode() {
        return dataManager.get(MOVE_MODE);
    }

    public boolean isTNTMode() {
        return dataManager.get(TNT_MODE);
    }
}
