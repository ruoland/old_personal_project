package map.lopre2;

import cmplus.deb.DebAPI;
import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
import minigameLib.map.EntityDefaultNPC;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import map.lopre2.jump1.EntityBuildBlock;
import map.lopre2.jump1.EntityLavaBlock;
import map.lopre2.jump2.EntityBigBlock;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EntityPreBlock extends EntityDefaultNPC {
    protected static Block prevBlock = Blocks.STONE;
    private static final DataParameter<String> JUMP_NAME = EntityDataManager.createKey(EntityPreBlock.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> ISINV = EntityDataManager.createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FORCE_SPAWN = EntityDataManager.createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);//클라이언트 월드에서 엔티티가 스폰 될 수 있게 함, 가짜 블럭 생성용
    private static final DataParameter<Boolean> TELEPORT_LOCK = EntityDataManager.createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DIFFICULTY = EntityDataManager.createKey(EntityPreBlock.class,
            DataSerializers.VARINT);

    public EntityPreBlock(World worldObj) {
        super(worldObj);
        //this.setCustomNameTag(this.getClass().getSimpleName().replace("Entity", ""));
        this.setDeathTimer(-1);
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(TELEPORT_LOCK, false);
        dataManager.register(ISINV, false);
        dataManager.register(FORCE_SPAWN, false);
        dataManager.register(DIFFICULTY, -1);
        dataManager.register(JUMP_NAME, "");
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        if (getRotateX() == 0 && getRotateY() == 0 && getRotateZ() == 0)
            setRotate(90, 90, 90);
        this.setTeleport(true);

        return super.onInitialSpawn(difficulty, livingdata);
    }

    public void setInv(boolean is) {
        dataManager.set(ISINV, is);
    }

    @Override
    public boolean hasCustomName() {
        return WorldAPI.equalsHeldItem(LoPre2.itemDifficulty) || WorldAPI.equalsHeldItem(LoPre2.itemSpanner);
    }

    public boolean isInv() {
        return dataManager.get(ISINV);
    }


    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            if (stack != null && stack.getItem() == (LoPre2.itemCopy)) {
                return false;
            }
            if (WorldAPI.equalsHeldItem(LoPre2.itemSpanner) || WorldAPI.equalsHeldItem(LoPre2.itemCopy)) {
                List<EntityPreBlock> list = EntityAPI.getEntity(worldObj, this.getEntityBoundingBox(), EntityPreBlock.class);
                System.out.println("카운트" + list.size());
                for (EntityPreBlock pre : list) {
                    System.out.println(pre.getCustomNameTag());
                }
                return super.processInteract(player, hand, stack);
            }
            if (stack != null && Block.getBlockFromItem(stack.getItem()) != null) {
                setBlock(stack);
                prevBlock = Block.getBlockFromItem(stack.getItem());
                System.out.println("블럭 교체됨" + stack.getItem());
                return super.processInteract(player, hand, stack);
            }

            if (DebAPI.isKeyDown(Keyboard.KEY_COMMA)) {
                setInv(!isInv());
                setInvisible(isInv());
                System.out.println("블럭의 투명이" + isInvisible() + isInv() + "으로 설정됨");
                return super.processInteract(player, hand, stack);
            }
            if (isServerWorld() && DebAPI.isKeyDown(Keyboard.KEY_L)) {
                setTeleportLock(!canTeleportLock());
                System.out.println("isLock이" + canTeleportLock() + "으로 설정됨");
                return super.processInteract(player, hand, stack);
            }
            if (player.isSneaking() && player.isCreative()) {
                setTeleport(true);
                return super.processInteract(player, hand, stack);
            }
        }
        return super.processInteract(player, hand, stack);
    }

    public int getDifficulty() {
        return dataManager.get(DIFFICULTY);
    }

    public void setDifficulty(int i) {
        dataManager.set(DIFFICULTY, i);

    }

    public String getJumpName() {
        return dataManager.get(JUMP_NAME);
    }

    public void setJumpName(String name) {
        dataManager.set(JUMP_NAME, name);
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if (CommandJB.isDebMode) {
            if (source.getEntity() != null && (source.getEntity().isSneaking() || WorldAPI.equalsHeldItem(Items.BONE) || WorldAPI.equalsHeldItem(Items.ARROW))) {
                if (WorldAPI.equalsHeldItem(Items.ARROW)) {
                    List<EntityPreBlock> list = EntityAPI.getEntity(worldObj, this.getEntityBoundingBox().expand(5, 5, 5), EntityPreBlock.class);
                    DebAPI.msgText("LoopPre2", "겹친 블럭 수" + list.size());
                    for (EntityPreBlock pre : list) {
                        if (pre == this) {
                            continue;
                        }
                        pre.setDead();
                    }
                }
                this.setDead();
                System.out.println("사라진 블럭 좌표 " + this.getSpawnX() + ", " + this.getSpawnY() + ", " + this.getSpawnZ());
            }
        }
        return false;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void setFire(int seconds) {
        // super.setFire(seconds);
    }


    private int delayTick;

    @Override
    public void onLivingUpdate() {
        setInvisible(isInv());
        if (getDifficulty() > 0) {
            //setBlock(Blocks.WOOL);
            //setBlockMetadata(6);
            if (getDifficulty() >= worldObj.getDifficulty().getDifficultyId()) {
                setInv(false);
                setCollision(true);
            } else {
                setInv(true);
                setCollision(false);
            }
        }
        if (CommandJB.isLavaInvisible && !(this instanceof EntityBigBlock)) {
            setInvisible(!isInv());
        } else
            setInvisible(isInv());

        this.rotationYaw = 0;
        this.renderYawOffset = 0;
        if (delayTick > 0) {
            delayTick--;
        }
        if (isTeleport()) {
            teleport();
        }
        if (canTeleportLock() && !isTeleport() && getSpawnX() != 0 && getSpawnZ() != 0) {
            if (MathHelper.floor_double(posX) != MathHelper.floor_double(getSpawnX()) || MathHelper.floor_double(posZ) != MathHelper.floor_double(getSpawnZ())) {
                this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
                //this.setPositionAndRotationDirect(getSpawnX(), getSpawnY(), getSpawnZ(), 90, 90, 1, true);
            }
        }
        super.onLivingUpdate();
        this.rotationYaw = 0;
        this.renderYawOffset = 0;
    }

    protected void resetHeight() {
        float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        this.playSound(this.getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
    }

    public boolean canTeleportLock() {
        return dataManager.get(TELEPORT_LOCK);

    }

    public void teleportEnd() {

    }

    public void teleport() {

        Vec3d vec = Minecraft.getMinecraft().thePlayer.getLookVec();
        this.setPosition((WorldAPI.x() + vec.xCoord * ax),
                WorldAPI.y() + WorldAPI.getPlayerMP().getEyeHeight()+ vec.yCoord * ax,
                WorldAPI.z() + vec.zCoord * ax);
        int xyz = 1;
        if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL))
            xyz = 3;
        if (WorldAPI.equalsHeldItem(Items.STICK)) {
            if (DebAPI.isKeyDown(Keyboard.KEY_UP)) {
                addTraXYZ(0, xyz, 0);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_DOWN)) {
                addTraXYZ(0, -xyz, 0);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_RIGHT)) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    addTraXYZ(-xyz, 0, 0);
                } else
                    addTraXYZ(xyz, 0, 0);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_LEFT)) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    addTraXYZ(0, 0, -xyz);
                } else
                    addTraXYZ(0, 0, xyz);
            }
        } else {
            if (DebAPI.isKeyDown(Keyboard.KEY_UP)) {
                addRotate(0, xyz, 0);
                System.out.println("YY");
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_DOWN)) {
                addRotate(0, -xyz, 0);
                System.out.println("--YY");

            }
            if (DebAPI.isKeyDown(Keyboard.KEY_RIGHT)) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    addRotate(xyz, 0, 0);
                } else
                    addRotate(-xyz, 0, 0);
                System.out.println("XX");

            }
            if (DebAPI.isKeyDown(Keyboard.KEY_LEFT)) {
                System.out.println("ZZ");
                if (DebAPI.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    addRotate(0, 0, xyz);
                } else
                    addRotate(0, 0, -xyz);
            }
        }
        if (isServerWorld() && DebAPI.isKeyDown(Keyboard.KEY_K)) {
            System.out.println("키 누름");
            EntityPreBlock lavaBlock = spawn(WorldAPI.x() + vec.xCoord * ax,
                    WorldAPI.y() + WorldAPI.getPlayerMP().getEyeHeight() + vec.yCoord * ax,
                    WorldAPI.z() + vec.zCoord * ax);
            lavaBlock.teleportEnd();
            lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());

            if (this instanceof EntityBuildBlock) {
                lavaBlock.setDead();
                this.setTeleport(false);
                this.setPositionAndUpdate(WorldAPI.x() + vec.xCoord * ax,
                        WorldAPI.y() + WorldAPI.getPlayerMP().getEyeHeight() + vec.yCoord * ax,
                        WorldAPI.z() + vec.zCoord * ax);
                this.setSpawnXYZ(posX, posY, posZ);
                System.out.println(posX + " - " + posY + " - " + posZ + " 소환됨");
            } else {
                setDead();
                setTeleport(false);
                System.out.println(isDead + " - " + isTeleport() + " - " + lavaBlock.isTeleport());
            }
        }
        if (isServerWorld() && DebAPI.isKeyDown(Keyboard.KEY_J) && delayTick == 0) {
            delayTick = 20;
            EntityPreBlock lavaBlock = spawn(WorldAPI.x() + vec.xCoord * ax,
                    WorldAPI.y() + WorldAPI.getPlayerMP().getEyeHeight() + vec.yCoord * ax,
                    WorldAPI.z() + vec.zCoord * ax);
            lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
            System.out.println(getCustomNameTag() + " 소환됨");
        }
    }

    public abstract EntityPreBlock spawn(double x, double y, double z);

    public void setTeleportLock(boolean a) {
        this.dataManager.set(TELEPORT_LOCK, a);
    }

    public void setForceSpawn(boolean a) {
        this.dataManager.set(FORCE_SPAWN, a);
    }

    public boolean canForceSpawn() {
        return dataManager.get(FORCE_SPAWN);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void despawnEntity() {
        //super.despawnEntity();
    }

    public void dataCopy(EntityPreBlock lavaBlock, double x, double y, double z) {
        lavaBlock.setTeleportLock(canTeleportLock());
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
        lavaBlock.setJumpName(getJumpName());
        lavaBlock.setBlockMode(getCurrentBlock());
        this.copyModel(lavaBlock);
        lavaBlock.setRotate(getRotateX(), getRotateY(), getRotateZ());

        lavaBlock.setBlockMetadata(getBlockMetadata());
        lavaBlock.setInv(isInv());
        lavaBlock.setInvisible(isInvisible());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("falling", canTeleportLock());
        compound.setBoolean("isInv", isInv());
        compound.setInteger("difficulty", getDifficulty());
        if (!getJumpName().isEmpty())
            compound.setString("jumpname", getJumpName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setTeleportLock(compound.getBoolean("falling"));
        setSize(compound.getFloat("widthl"), compound.getFloat("heightl"));
        setInvisible(compound.getBoolean("isInv"));
        setInv(compound.getBoolean("isInv"));
        setDifficulty(compound.getInteger("difficulty"));
        if (compound.hasKey("jumpname"))
            setJumpName(compound.getString("jumpname"));
    }

    @Override
    public void setDead() {
        if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
            super.setDead();
        }
    }
}
