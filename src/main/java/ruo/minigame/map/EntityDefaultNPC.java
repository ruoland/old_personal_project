package ruo.minigame.map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.Move;
import ruo.minigame.effect.TextEffect;
import ruo.minigame.effect.TickRegister;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class EntityDefaultNPC extends EntityModelNPC {

    private static final DataParameter<Rotations> SPAWN_XYZ = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.ROTATIONS);
    //스턴 관련 코드. 스턴 상태로 설정하면  얼굴회전 그리고 이동을 멈춤
    private static final DataParameter<Boolean> IS_STURN = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Float> LOCK_YAW = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> LOCK_PITCH = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Integer> DEATH_TIMER = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.VARINT);
    private static final DataParameter<Boolean> ON_DEATH_TIMER = EntityDataManager.createKey(EntityDefaultNPC.class,
            DataSerializers.BOOLEAN);//데드 타이머가 켜져있는지 여부
    private static final DataParameter<Boolean> COLLISION = EntityDataManager
            .createKey(EntityDefaultNPC.class, DataSerializers.BOOLEAN);

    private static HashMap<String, EntityDefaultNPC> npcHash = new HashMap<>();
    private static HashMap<String, EntityDefaultNPC> uuidHash = new HashMap<>();
    private PosHelper posHelper;
    public boolean isFly;
    public TextEffect eft;
    private Vec3d targetPosition;
    private double distance = 0.8;
    private double targetMoveSpeed;

    public int random = 0;
    public double eyeCloseScaleY;
    public boolean eyeCloseReverse;
    public EntityDefaultNPC(World worldIn) {
        super(worldIn);
        this.eft = TextEffect.getHelper(this);
        posHelper = new PosHelper(this);
        this.setSize(0.6F, 1.95F);
        PathNavigateGround path = (PathNavigateGround) this.getNavigator();
        this.setRotate(0,0,0);
        path.setEnterDoors(true);
        path.setBreakDoors(true);
        if (!worldIn.isRemote) {
            TextEffect.lookMob.put(this.getName(), this);
            npcHash.put(getCustomNameTag(), this);
            uuidHash.put(getUniqueID().toString(), this);
        }
        random = rand.nextInt(100);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DEATH_TIMER, -1);
        this.dataManager.register(IS_STURN, false);
        this.dataManager.register(LOCK_YAW, 0F);
        this.dataManager.register(LOCK_PITCH, 0F);
        this.dataManager.register(COLLISION, false);
        this.dataManager.register(ON_DEATH_TIMER, false);
        dataManager.register(SPAWN_XYZ, new Rotations(0, 0, 0));
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

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        this.faceEntity(player, 360, 360);
        random = rand.nextInt(100);
        return super.processInteract(player, hand, stack);
    }

    public void setTarget(double x, double y, double z, double speed) {
        if (x == 0 && y == 0 && z == 0) {
            targetPosition = null;
        } else {
            this.targetPosition = new Vec3d(x, y, z);
        }
        this.targetMoveSpeed = speed;
    }
    public void setTarget(double x, double y, double z) {
        setTarget(x,y,z,0.4);
    }
    public void setTarget(@Nullable EntityLivingBase target, double speed) {
        if(target == null)
            setTarget(0,0,0,0);
        this.setTarget(target.posX, target.posY, target.posZ, speed);
    }

    public boolean noTarget() {
        return targetPosition == null;
    }

    public Vec3d getTargetPosition() {
        return targetPosition;
    }

    public void targetArrive() {

    }

    public boolean isTargetArrive(){
        return getDistance(targetPosition) < distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(getSpawnX() == 0 && getSpawnY() == 0 && getSpawnZ() == 0){
            setSpawnXYZ(posX, posY, posZ);
        }
        if(random > 0 && getModel() == TypeModel.NPC){
            random--;
        }
        if (isSturn()) {
            this.rotationPitch = getDataManager().get(LOCK_PITCH);
            this.rotationYaw = getDataManager().get(LOCK_YAW);
            this.rotationYawHead = getDataManager().get(LOCK_YAW);
        }
        if (targetPosition != null) {
            setVelocity(this.targetPosition.subtract(this.getPositionVector()).normalize().scale(targetMoveSpeed));
            System.out.println(this.targetPosition.subtract(this.getPositionVector()).normalize().scale(targetMoveSpeed));
            if (getDistance(targetPosition) < distance) {
                targetArrive();
                setTarget(0, 0, 0, 0);
            }
        } else if (isFly) {
            motionY = 0;
        }
        if (isServerWorld()) {
            if (!npcHash.containsKey(this.getCustomNameTag())) {
                npcHash.put(this.getCustomNameTag(), this);
            }
            if (!uuidHash.containsKey(this.getUniqueID().toString())) {
                uuidHash.put(this.getUniqueID().toString(), this);
            }

        }
        if (getDataManager().get(ON_DEATH_TIMER)) {
            int deathTime = getDataManager().get(DEATH_TIMER);
            if (isServerWorld() && deathTime > 0) {
                getDataManager().set(DEATH_TIMER, deathTime - 1);
            }
            if (deathTime == 0) {
                this.setDead();
            }
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {

        if (targetPosition != null) {
            compound.setDouble("targetX", targetPosition.xCoord);
            compound.setDouble("targetY", targetPosition.yCoord);
            compound.setDouble("targetZ", targetPosition.zCoord);
            compound.setDouble("speed", targetMoveSpeed);

        }
        compound.setDouble("distance", distance);

        compound.setTag("SPAWNXYZ", getSpawnXYZ().writeToNBT());
        compound.setInteger("DEATH_TIMER", getDataManager().get(DEATH_TIMER));
        compound.setBoolean("ON_DEATH_TIMER", getDataManager().get(ON_DEATH_TIMER));

        compound.setFloat("LOCKPITCH", getDataManager().get(LOCK_PITCH));
        compound.setFloat("LOCKYAW", getDataManager().get(LOCK_YAW));
        compound.setBoolean("canCollision", canCollision());
        compound.setBoolean("ISSTURN", isSturn());
        AbstractTick absTick = TickRegister.getAbsTick(getUniqueID().toString() + "-STURN");
        if (absTick != null) {
            int sturnTick = absTick.getCurrentTick();
            compound.setInteger("STURN_TICK", sturnTick);
        }
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        getDataManager().set(DEATH_TIMER, compound.getInteger("DEATH_TIMER"));
        getDataManager().set(ON_DEATH_TIMER, compound.getBoolean("ON_DEATH_TIMER"));
        getDataManager().set(SPAWN_XYZ, getRotations(compound, "SPAWNXYZ"));
        getDataManager().set(LOCK_YAW, compound.getFloat("STURNYAW"));
        getDataManager().set(LOCK_PITCH, compound.getFloat("STURNPITCH"));
        setCollision(compound.getBoolean("canCollision"));
        if (compound.getBoolean("ISSTURN"))
            setSturn(compound.getInteger("STURN_TICK"));
        if(compound.hasKey("targetX"))
        setTarget(compound.getDouble("targetX"), compound.getDouble("targetY"), compound.getDouble("targetZ"), compound.getDouble("speed"));
        setDistance(compound.getDouble("distance"));

    }

    @Override
    protected boolean canDespawn() {
        return !super.canDespawn();
    }

    public void setSpawnPosition() {
        setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
    }

    public Rotations getSpawnXYZ() {
        return dataManager.get(SPAWN_XYZ);
    }

    public double getSpawnX() {
        return getSpawnXYZ().getX();
    }

    public double getSpawnY() {
        return getSpawnXYZ().getY();
    }

    public double getSpawnZ() {
        return getSpawnXYZ().getZ();
    }

    public void setSpawnXYZ(double x, double y, double z) {
        this.dataManager.set(SPAWN_XYZ, new Rotations((float) x, (float) y, (float) z));
    }
    public void setSpawnXYZ(Vec3d vec3d) {
        this.dataManager.set(SPAWN_XYZ, new Rotations((float) vec3d.xCoord, (float) vec3d.yCoord, (float) vec3d.yCoord));
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public void setDeathTimer(int deathTimer) {
        if (deathTimer > 0) {
            dataManager.set(DEATH_TIMER, deathTimer);
            dataManager.set(ON_DEATH_TIMER, true);
        } else
            dataManager.set(ON_DEATH_TIMER, false);

    }

    public BlockPos getSpawnPos() {
        return new BlockPos(getSpawnX(), getSpawnY(), getSpawnZ());
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
        setSturn(true);
        TickRegister.register(new AbstractTick(getUniqueID().toString() + "-STURN", Type.SERVER, tick, false) {
            @Override
            public boolean stopCondition() {
                return isDead;
            }

            @Override
            public void run(Type type) {
                setSturn(false);
            }
        });
    }

    public void setSturn(boolean is) {
        dataManager.set(IS_STURN, is);
        dataManager.set(LOCK_YAW, this.rotationYaw);
        dataManager.set(LOCK_PITCH, this.rotationPitch);
        if (TickRegister.isAbsTickRun(getUniqueID().toString() + "-STURN"))
            TickRegister.remove(TickRegister.getAbsTick(getUniqueID().toString() + "-STURN"));
        onSturn();
    }

    /**
     * 스턴 걸렸을 때나 해제됐을 때 호출되는 메서드
     */
    public void onSturn() {

    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (!(isSturn() && onGround))
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
            npcHash.put(this.getCustomNameTag(), this);
        }
        super.setCustomNameTag(name);
    }


    public void setPosition(BlockPos position) {
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(Vec3d vec3d) {
        this.setPosition(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }


    public void addChat(int second, String te) {
        if (eft == null)
            eft = TextEffect.getHelper(this);
        eft.addChat(Integer.valueOf((second + "000")), te, null);
    }

    public void addChat(int second, String text, String tick) {
        if (eft == null)
            eft = TextEffect.getHelper(this);
        eft.addChat(this, Integer.valueOf((second + "000")), text, tick);
    }

    public void cancel() {
        if (eft != null)
            eft.cancel();
    }

    public Vec3d getXZ(SpawnDirection spawnDirection, double plus, boolean pos) {
        return posHelper.getXZ(spawnDirection, plus, pos);
    }

    public Vec3d getXZ(SpawnDirection spawnDirection, double plus, double rlplus, boolean pos) {
        return posHelper.getXZ(spawnDirection, plus, rlplus, pos);
    }

    public double getX(SpawnDirection spawnDirection, double plus, boolean pos) {
        return posHelper.getX(spawnDirection, plus, pos);
    }

    public double getX(SpawnDirection spawnDirection, double plus, double rlplus, boolean pos) {
        return posHelper.getX(spawnDirection, plus, rlplus, pos);
    }

    public double getZ(SpawnDirection spawnDirection, double plus, boolean pos) {
        return posHelper.getZ(spawnDirection, plus, pos);
    }

    public double getZ(SpawnDirection spawnDirection, double plus, double rlplus, boolean pos) {
        return posHelper.getZ(spawnDirection, plus, rlplus, pos);
    }

    public void look(EntityLivingBase mob2) {
        EntityAPI.look(this, mob2);
    }

    public void look(double x, double y, double z) {
        EntityAPI.look(this, x, y, z);
    }

    public void lookPlayer() {
        EntityAPI.lookPlayer(this);
    }

    public void removeLook() {
        EntityAPI.removeLook(this);
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
}
