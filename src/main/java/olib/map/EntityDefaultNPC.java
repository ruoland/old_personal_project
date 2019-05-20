package olib.map;

import olib.api.Direction;
import olib.api.EntityAPI;
import olib.api.PosHelper;
import olib.api.WorldAPI;
import olib.effect.Move;
import olib.effect.TextEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class EntityDefaultNPC extends EntityModelNPC {

    //스턴 관련 코드. 스턴 상태로 설정하면  얼굴회전 그리고 이동을 멈춤
    private static final DataParameter<Boolean> IS_STURN = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.BOOLEAN);

    private static final DataParameter<Integer> STURN_TICK = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.VARINT);

    private static final DataParameter<Float> LOCK_YAW = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> LOCK_PITCH = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.FLOAT);

    private static final DataParameter<Boolean> ON_DEATH_TIMER = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.BOOLEAN);//데드 타이머가 켜져있는지 여부

    private static final DataParameter<Integer> DEATH_TIMER = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.VARINT);

    private static final DataParameter<Boolean> COLLISION = EntityDataManager
            .createKey(EntityDefaultNPC.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_TELEPORT = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.BOOLEAN);//라바 블럭에 쓰이던 텔레포트 기능임

    private static HashMap<String, EntityDefaultNPC> npcHash = new HashMap<>();
    private static HashMap<String, EntityDefaultNPC> uuidHash = new HashMap<>();
    private PosHelper posHelper;
    public boolean isFly;
    public TextEffect eft;
    private Vec3d targetPosition, targetVec;
    private double moveDistance = 0.8;
    private double targetMoveSpeed;
    public int eyeCloseTime = 0;
    public double eyeCloseScaleY;
    public boolean eyeCloseReverse;
    private boolean isTargetArriveStop = true;//타겟에 도착하면 이동하지 않음
    private PosHelper spawnPosHelper;
    private double spawnX, spawnY, spawnZ;

    public EntityDefaultNPC(World worldIn) {
        super(worldIn);
        this.eft = new TextEffect(this);
        posHelper = new PosHelper(this);
        this.setSize(0.6F, 1.95F);
        PathNavigateGround path = (PathNavigateGround) this.getNavigator();
        this.setRotate(0, 0, 0);
        path.setEnterDoors(true);
        path.setBreakDoors(true);
        if (!worldIn.isRemote) {
            TextEffect.lookMob.put(this.getName(), this);
            npcHash.put(getCustomNameTag(), this);
            uuidHash.put(getUniqueID().toString(), this);
        }
        eyeCloseTime = rand.nextInt(100);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(IS_TELEPORT, false);
        this.dataManager.register(IS_STURN, false);
        this.dataManager.register(LOCK_YAW, 0F);
        this.dataManager.register(LOCK_PITCH, 0F);
        this.dataManager.register(COLLISION, false);
        this.dataManager.register(ON_DEATH_TIMER, false);
        this.dataManager.register(DEATH_TIMER, -1);
        this.dataManager.register(STURN_TICK, -1);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
    }

    public void setVelocity(Vec3d vec3d) {
        super.setVelocity(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }

    public double getDistance(Vec3d vec3d) {
        return super.getDistance(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }

    public void teleportEnd() {

    }


    public EntityDefaultNPC setTargetSpeed(double speed) {
        this.targetMoveSpeed = speed;
        return this;
    }

    public EntityDefaultNPC removeTarget(){
        targetPosition = null;
        return this;

    }
    public EntityDefaultNPC setTarget(double x, double y, double z, double speed) {
        if (x == 0 && y == 0 && z == 0) {
            targetPosition = null;
            return this;
        } else {
            this.targetPosition = new Vec3d(x, y, z);
        }
        targetVec = this.targetPosition.subtract(this.getPositionVector()).normalize();
        this.targetMoveSpeed = speed;
        return this;
    }

    public EntityDefaultNPC setTarget(Vec3d vec3d) {
        return setTarget(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.4);
    }

    public EntityDefaultNPC setTarget(double x, double y, double z) {
        return setTarget(x, y, z, 0.4);
    }

    public EntityDefaultNPC setTarget(@Nullable EntityLivingBase target, double speed) {
        if (target == null)
            return setTarget(0, 0, 0, 0);
        return this.setTarget(target.posX, target.posY, target.posZ, speed);
    }


    /**
     * 목적지에 도착했을 때 이동을 계속 할 건지 말 건지 설정하는 메서드
     * true로 하면 목적지 도착시 멈춤, false로 하면 목적지에 도착해도 계속 뒤로 이동함
     */
    public void setTargetArriveStop(boolean targetArriveStop) {
        isTargetArriveStop = targetArriveStop;
    }

    public boolean noTarget() {
        return targetPosition == null;
    }

    public Vec3d getTargetPosition() {
        return targetPosition;
    }

    public void targetArrive() {
        if (isTargetArriveStop) setTarget(0, 0, 0, 0);
        this.setVelocity(0, 0, 0);
    }

    public boolean isTargetArrive() {
        return !noTarget() && getDistance(targetPosition) < moveDistance;
    }

    public void setDistance(double distance) {
        this.moveDistance = distance;
    }

    public void setTeleport(boolean a) {
        this.dataManager.set(IS_TELEPORT, a);
    }

    public boolean isTeleport() {
        return dataManager.get(IS_TELEPORT);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (getSpawnX() == 0 && getSpawnY() == 0 && getSpawnZ() == 0) {
            setSpawnXYZ(posX, posY, posZ);
        }
        if (eyeCloseTime > 0 && getModel() == TypeModel.NPC) {
            eyeCloseTime--;
        }
        if (dataManager.get(LOCK_PITCH) != 0 || dataManager.get(LOCK_YAW) != 0 || isSturn()) {
            this.rotationPitch = getDataManager().get(LOCK_PITCH);
            this.rotationYaw = getDataManager().get(LOCK_YAW);
            this.rotationYawHead = getDataManager().get(LOCK_YAW);
        }
        if (targetPosition != null) {
            moveEntity(targetVec.scale(targetMoveSpeed));
            if (getDistance(targetPosition) < moveDistance) {
                targetArrive();
            }
        } else if (isFly) {
            motionY = 0;
            onGround = true;
        }
        if (isServerWorld()) {
            if (!npcHash.containsKey(this.getCustomNameTag())) {
                npcHash.put(this.getCustomNameTag(), this);
            }
            if (!uuidHash.containsKey(this.getUniqueID().toString())) {
                uuidHash.put(this.getUniqueID().toString(), this);
            }

            if (getDataManager().get(ON_DEATH_TIMER) && getDeathTime() >= 0) {
                setDeathTimer(getDeathTime() - 1);
                if (getDeathTime() <= 0) {
                    onTimerDeath();
                }
            }
            if (isSturn()) {
                int sturnTick = dataManager.get(STURN_TICK);
                if (sturnTick > 0) {
                    dataManager.set(STURN_TICK, sturnTick - 1);
                } else
                    setSturn(false);
            }
        }
    }


    public void onTimerDeath() {
        this.setDead();
    }

    public int getDeathTime() {
        return dataManager.get(DEATH_TIMER);
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {

        if (targetPosition != null) {
            compound.setDouble("targetX", targetPosition.xCoord);
            compound.setDouble("targetY", targetPosition.yCoord);
            compound.setDouble("targetZ", targetPosition.zCoord);
            compound.setDouble("speed", targetMoveSpeed);
        }
        compound.setDouble("distance", moveDistance);
        compound.setDouble("STURN_TICK", dataManager.get(STURN_TICK));
        compound.setDouble("spawnX", spawnX);
        compound.setDouble("spawnY", spawnY);
        compound.setDouble("spawnZ", spawnZ);
        compound.setInteger("DEATH_TIMER", getDeathTime());
        compound.setBoolean("ON_DEATH_TIMER", getDataManager().get(ON_DEATH_TIMER));

        compound.setFloat("LOCKPITCH", getDataManager().get(LOCK_PITCH));
        compound.setFloat("LOCKYAW", getDataManager().get(LOCK_YAW));
        compound.setBoolean("canCollision", canCollision());
        compound.setBoolean("IS_STURN", isSturn());
        compound.setInteger("STURN_TICK", dataManager.get(STURN_TICK));
        compound.setBoolean("isFly", isFly);
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("DEATH_TIMER"))
            setDeathTimer(compound.getInteger("DEATH_TIMER"));
        if (compound.hasKey("SPAWNXYZ")) {
            Rotations rotations = getRotations(compound, "SPAWNXYZ");
            setSpawnXYZ(rotations.getX(), rotations.getY(), rotations.getZ());//스폰 XYZ를 float에서 double로 바꿈. 구버전 호환을 위해서 넣음
        } else {
            setSpawnXYZ(compound.getDouble("spawnX"), compound.getDouble("spawnY"), compound.getDouble("spawnZ"));
        }

        getDataManager().set(ON_DEATH_TIMER, compound.getBoolean("ON_DEATH_TIMER"));

        getDataManager().set(LOCK_YAW, compound.getFloat("STURNYAW"));
        getDataManager().set(LOCK_PITCH, compound.getFloat("STURNPITCH"));
        setCollision(compound.getBoolean("canCollision"));
        if (compound.getBoolean("IS_STURN"))
            setSturn(compound.getInteger("STURN_TICK"));
        if (compound.hasKey("targetX"))
            setTarget(compound.getDouble("targetX"), compound.getDouble("targetY"), compound.getDouble("targetZ"), compound.getDouble("speed"));
        setDistance(compound.getDouble("distance"));
        isFly = compound.getBoolean("isFly");

    }
    public void moveEntity(Vec3d vec3d){
        super.moveEntity(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }

    @Override
    protected boolean canDespawn() {
        return !super.canDespawn();
    }

    public void updateSpawnPosition() {
        setSpawnXYZ(posX, posY, posZ);
    }

    public void teleportSpawnPos() {
        setPositionAndUpdate(getSpawnX(), getSpawnY(), getSpawnZ());
    }

    public double getSpawnX() {
        return spawnX;
    }

    public double getSpawnY() {
        return spawnY;
    }

    public double getSpawnZ() {
        return spawnZ;
    }

    public void setSpawnXYZ(double x, double y, double z) {
        spawnX = x;
        spawnY = y;
        spawnZ = z;
    }

    public PosHelper getSpawnPosHelper() {
        if (spawnPosHelper == null || (spawnPosHelper.getPosX() != getSpawnX()
                || spawnPosHelper.getPosY() != getSpawnY()
                || spawnPosHelper.getPosZ() != getSpawnZ())) {
            spawnPosHelper = new PosHelper(getSpawnX(), getSpawnY(), getSpawnZ(), getHorizontalFacing());
        }

        return spawnPosHelper;
    }

    public void setSpawnXYZ(Vec3d vec3d) {
        setSpawnXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public void setDeathTimer(int deathTimer) {
            dataManager.set(DEATH_TIMER, deathTimer);
            dataManager.set(ON_DEATH_TIMER, true);
    }

    public Vec3d getSpawnPosVec() {
        return new Vec3d(getSpawnX(), getSpawnY(), getSpawnZ());
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return canCollision() ? getEntityBoundingBox() : super.getCollisionBoundingBox();
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity player) {
        return canCollision() ? getEntityBoundingBox() : super.getCollisionBox(player);
    }

    public boolean canCollision() {
        return dataManager.get(COLLISION);
    }

    public void setCollision(boolean is) {
        dataManager.set(COLLISION, is);
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isSturn() {
        return dataManager.get(IS_STURN);
    }

    public void setSturn(int tick) {
        dataManager.set(STURN_TICK, tick);
        setSturn(true);
    }

    public void setSturn(boolean is) {
        dataManager.set(IS_STURN, is);
        if (!is)
            dataManager.set(STURN_TICK, -1);
        else if (dataManager.get(STURN_TICK) <= 0) {
            dataManager.set(STURN_TICK, 20);
            System.out.println("[경고] " + this + "의 엔티티의 스턴 틱이 설정되지 않아 20틱으로 설정했습니다.");
        }
        dataManager.set(LOCK_YAW, this.rotationYaw);
        dataManager.set(LOCK_PITCH, this.rotationPitch);
        onSturn();
    }

    public void setLockYawPitch(float yaw, float pitch){
        dataManager.set(LOCK_YAW, yaw);
        dataManager.set(LOCK_PITCH, pitch);
    }

    public void setLockYaw(float yaw){
        dataManager.set(LOCK_YAW, yaw);
    }

    public void setLockPitch(float pitch){
        dataManager.set(LOCK_PITCH, pitch);
    }
    /**
     * 스턴 걸렸을 때나 해제됐을 때 호출되는 메서드
     */
    public void onSturn() {

    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (!(isSturn()))
            super.moveEntityWithHeading(strafe, forward);
    }

    public void command(String command) {
        WorldAPI.command(command);
    }

    public void function(String function) {
        WorldAPI.command("/npc " + getCustomNameTag() + " func " + function);
    }

    public void swingArm() {
        super.swingArm(EnumHand.MAIN_HAND);
    }

    public static Collection<EntityDefaultNPC> getAllNPC() {
        return npcHash.values();
    }

    public static EntityDefaultNPC getNPC(String name) {
        return npcHash.get(name);
    }

    public static void removeNPC(String name) {
        npcHash.get(name).setDead();
        npcHash.remove(name);
    }

    public static EntityDefaultNPC getUUIDNPC(UUID uuid) {
        return uuidHash.get(uuid.toString());
    }

    public static void removeUUIDNPC(String name) {
        uuidHash.get(name).setDead();
        uuidHash.remove(name);
    }

    @Override
    public void setUniqueId(UUID uniqueIdIn) {
        if (!worldObj.isRemote) {
            uuidHash.remove(this.getUniqueID().toString());
            uuidHash.put(uniqueIdIn.toString(), this);
        }
        super.setUniqueId(uniqueIdIn);
    }

    @Override
    public void setCustomNameTag(String name) {
        if (!worldObj.isRemote) {

            npcHash.remove(this.getCustomNameTag());

        }
        super.setCustomNameTag(name);
        npcHash.put(this.getCustomNameTag(), this);
    }


    public void setPosition(BlockPos position) {
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(Vec3d vec3d) {
        this.setPosition(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }


    public void addChat(int second, String text) {
        getEFT().addChat(this, Integer.valueOf((second + "000")), text);
    }

    public void cancel() {
        getEFT().cancel();
    }

    public TextEffect getEFT(){
        if(eft == null)
            eft = new TextEffect(this);
        return eft;
    }

    public Vec3d getXZ(Direction spawnDirection, double plus, boolean pos) {
        return posHelper.getXZ(spawnDirection, plus, pos);
    }

    public Vec3d getXZ(Direction spawnDirection, double plus, double rlplus, boolean pos) {
        return posHelper.getXZ(spawnDirection, plus, rlplus, pos);
    }

    public double getX(Direction spawnDirection, double plus, boolean pos) {
        return posHelper.getX(spawnDirection, plus, pos);
    }

    public double getX(Direction spawnDirection, double plus, double rlplus, boolean pos) {
        return posHelper.getX(spawnDirection, plus, rlplus, pos);
    }

    public double getZ(Direction spawnDirection, double plus, boolean pos) {
        return posHelper.getZ(spawnDirection, plus, pos);
    }

    public double getZ(Direction spawnDirection, double plus, double rlplus, boolean pos) {
        return posHelper.getZ(spawnDirection, plus, rlplus, pos);
    }

    public void move(double x, double y, double z, double... xyz) {
        EntityAPI.move(this, x, y, z, xyz);
    }

    public void move(Move move) {
        EntityAPI.move(move);
    }

    public boolean isAttackTargetPlayer() {
        return getAttackTarget() != null && getAttackTarget() instanceof EntityPlayer;
    }

    public void printData(){

    }
}
