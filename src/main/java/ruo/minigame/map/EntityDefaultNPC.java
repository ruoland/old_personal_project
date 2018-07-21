package ruo.minigame.map;

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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.minigame.api.EntityAPI;
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

    public boolean isFly;
    public TextEffect eft;

    public EntityDefaultNPC(World worldIn) {
        super(worldIn);
        this.eft = TextEffect.getHelper(this);
        this.setSize(0.6F, 1.95F);
        PathNavigateGround path = (PathNavigateGround) this.getNavigator();
        path.setEnterDoors(true);
        path.setBreakDoors(true);
        if(!worldIn.isRemote) {
            TextEffect.lookMob.put(this.getName(), this);
            npcHash.put(getCustomNameTag(), this);
            uuidHash.put(getUniqueID().toString(), this);
        }
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

    public double getDistance(Vec3d vec3d){
        return super.getDistance(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isSturn()) {
            this.rotationPitch = getDataManager().get(LOCK_PITCH);
            this.rotationYaw = getDataManager().get(LOCK_YAW);
            this.rotationYawHead = getDataManager().get(LOCK_YAW);
        }
        if (isFly) {
            motionY = 0;
        }
        if(isServerWorld()) {
            if (!npcHash.containsKey(this.getCustomNameTag())) {
                npcHash.put(this.getCustomNameTag(), this);
            }
            if (!uuidHash.containsKey(this.getUniqueID().toString())) {
                uuidHash.put(this.getUniqueID().toString(), this);
            }

        }
        if(getDataManager().get(ON_DEATH_TIMER)) {
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
        compound.setTag("SPAWNXYZ", getSpawnXYZ().writeToNBT());
        compound.setInteger("DEATH_TIMER", getDataManager().get(DEATH_TIMER));
        compound.setBoolean("ON_DEATH_TIMER", getDataManager().get(ON_DEATH_TIMER));

        compound.setFloat("LOCKPITCH", getDataManager().get(LOCK_PITCH));
        compound.setFloat("LOCKYAW", getDataManager().get(LOCK_YAW));
        compound.setBoolean("canCollision", canCollision());
        compound.setBoolean("ISSTURN", isSturn());
        AbstractTick absTick = TickRegister.getAbsTick(getUniqueID().toString()+"-STURN");
        if(absTick != null) {
            int sturnTick =absTick.getCurrentTick();
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
        if(compound.getBoolean("ISSTURN"))
            setSturn(compound.getInteger("STURN_TICK"));
    }

    @Override
    protected boolean canDespawn() {
        return !super.canDespawn();
    }

    public void setSpawnPosition(){
        setPosition(getSpawnX(),getSpawnY(),getSpawnZ());
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

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        setSpawnXYZ(posX,posY,posZ);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public void setDeathTimer(int deathTimer){
        if(deathTimer > 0) {
            dataManager.set(DEATH_TIMER, deathTimer);
            dataManager.set(ON_DEATH_TIMER, true);
        }else
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
        if(!(isSturn() && onGround))
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

    public static Collection<EntityDefaultNPC> getAllNPC(){
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
        if(!worldObj.isRemote) {
            uuidHash.remove(this.getUniqueID().toString());
            uuidHash.put(uniqueIdIn.toString(), this);
        }
        super.setUniqueId(uniqueIdIn);
    }

    @Override
    public void setCustomNameTag(String name) {
        if(!worldObj.isRemote) {

            npcHash.remove(this.getCustomNameTag());
            npcHash.put(this.getCustomNameTag(), this);
        }
        super.setCustomNameTag(name);
    }


    public void setPosition(BlockPos position) {
        if(position != null)
        this.setPosition(position.getX(), position.getY(), position.getZ());
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


    public double lookPosX(float plusYaw, double plus) {
        return EntityAPI.lookPosX(this, plusYaw, plus);
    }

    public double lookPosZ(float plusYaw, double plus) {
        return EntityAPI.lookPosZ(this, plusYaw, plus);
    }

    public double lookX(float plusYaw, double plus) {
        return EntityAPI.lookX(this, plusYaw, plus);
    }

    public double lookZ(float plusYaw, double plus) {
        return EntityAPI.lookZ(this, plusYaw, plus);
    }
    public double lookX(double plus) {
        return EntityAPI.lookX(this, plus);
    }
    public double lookZ(double plus) {
        return EntityAPI.lookZ(this, plus);
    }
    public double forwardLeftX(double plus, boolean pos) {
        return EntityAPI.forwardLeftX(this, plus, pos);
    }
    public double forwardLeftZ(double plus, boolean pos) {
        return EntityAPI.forwardLeftZ(this, plus, pos);

    }
    public double forwardRightX(double plus, boolean pos) {
        return EntityAPI.forwardRightX(this,plus, pos);
    }
    public double forwardRightZ(double plus, boolean pos) {
        return EntityAPI.forwardRightZ(this,plus, pos);
    }

    public double forwardX(double plus, boolean pos) {
        return forwardX(this,this.getHorizontalFacing(), plus, pos);
    }

    public double forwardZ(double plus, boolean pos) {
        return forwardZ(this,this.getHorizontalFacing(), plus, pos);
    }

    public double backX(double minus, boolean pos)
    {
        return EntityAPI.backX(this, minus, pos);
    }
    public double backZ(double minus, boolean pos) {
        return EntityAPI.backX(this, minus, pos);
    }

    public double forwardX(EntityLivingBase base, EnumFacing facing, double plus, boolean pos) {
        double position = base.posX;
        if(!pos)
            position = 0;
        if (facing.getName().equalsIgnoreCase("NORTH")) {
            return position;
        }
        if (facing.getName().equalsIgnoreCase("SOUTH")) {
            return position;
        }
        if (facing.getName().equalsIgnoreCase("EAST")) {
            return position + plus;
        }
        if (facing.getName().equalsIgnoreCase("WEST")) {
            return position - plus;
        }
        return position;
    }

    public double forwardZ(EntityLivingBase base, EnumFacing facing, double plus, boolean pos) {
        double position = base.posZ;
        if(!pos)
            position = 0;
        if (facing.getName().equalsIgnoreCase("NORTH")) {
            return position - plus;
        }
        if (facing.getName().equalsIgnoreCase("SOUTH")) {
            return position + plus;
        }
        if (facing.getName().equalsIgnoreCase("EAST")) {
            return position;
        }
        if (facing.getName().equalsIgnoreCase("WEST")) {
            return position;
        }
        return position;
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

    public boolean isAttackTargetPlayer(){
        return getAttackTarget() != null && getAttackTarget() instanceof EntityPlayer;
    }
}
