package ruo.map.lopre2;

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
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.dummy.EntityBuildBlockMove;
import ruo.map.lopre2.jump2.EntityBigBlock;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;
import java.util.List;

public class EntityPreBlock extends EntityDefaultNPC {
    protected static Block prevBlock = Blocks.STONE;
    public static double ax = 3;
    private static final DataParameter<Boolean> ISINV = EntityDataManager.<Boolean>createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FORCE_SPAWN = EntityDataManager.<Boolean>createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);//클라이언트 월드에서 엔티티가 스폰 될 수 있게 함, 가짜 블럭 생성용
    private static final DataParameter<Boolean> COPY = EntityDataManager.<Boolean>createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);//복사용 블럭인가
    private static final DataParameter<Boolean> LOCK = EntityDataManager.createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ISTELEPORT = EntityDataManager.createKey(EntityPreBlock.class,
            DataSerializers.BOOLEAN);

    public EntityPreBlock(World worldObj) {
        super(worldObj);
        //this.setCustomNameTag(this.getClass().getSimpleName().replace("Entity", ""));
        this.setDeathTimer(-1);
    }

    @Override
    public boolean hasCustomName() {
        return Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox() || (WorldAPI.equalsHeldItem(LoPre2.itemSpanner) || WorldAPI.equalsHeldItem(LoPre2.itemCopy));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ISTELEPORT, false);
        dataManager.register(LOCK, false);
        dataManager.register(ISINV, false);
        dataManager.register(FORCE_SPAWN, false);
        dataManager.register(COPY, false);
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
        if (is) {
            setLock(true);
        }
    }

    public boolean isInv() {
        return dataManager.get(ISINV).booleanValue();
    }

    public void setCopyBlock(boolean is) {
        dataManager.set(COPY, is);
    }

    public boolean isCopyBlock() {
        return dataManager.get(COPY).booleanValue();
    }

    public void setTeleport(boolean a) {
        this.dataManager.set(ISTELEPORT, a);
    }

    public boolean isTeleport() {
        return dataManager.get(ISTELEPORT);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (CommandJB.isDebMode && hand == EnumHand.MAIN_HAND) {
            if (WorldAPI.equalsHeldItem(LoPre2.itemSpanner) || WorldAPI.equalsHeldItem(LoPre2.itemCopy)) {
                List<EntityPreBlock> list = EntityAPI.getEntity(worldObj, this.getEntityBoundingBox(), EntityPreBlock.class);
                System.out.println("카운트" + list.size());
                for (EntityPreBlock pre : list) {
                    System.out.println(pre.getCustomNameTag());
                }
                return super.processInteract(player, hand, stack);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_COMMA)) {
                setInv(!isInv());
                setInvisible(isInv());
                setLock(true);
                System.out.println("블럭의 투명이" + isInvisible() + isInv() + "으로 설정됨(락 걸림)");

                return super.processInteract(player, hand, stack);
            }
            if (player.isSneaking()) {
                setTeleport(true);
                return super.processInteract(player, hand, stack);
            }
            if (isServerWorld() && DebAPI.isKeyDown(Keyboard.KEY_L)) {
                setLock(!isLock());
                System.out.println("isLock이" + isLock() + "으로 설정됨");
                return super.processInteract(player, hand, stack);
            }
            if (stack != null && Block.getBlockFromItem(stack.getItem()) != null) {
                setBlock(stack);
                prevBlock = Block.getBlockFromItem(stack.getItem());
                return super.processInteract(player, hand, stack);
            }

            if (CommandJB.downMode || CommandJB.upMode) {
                if (this instanceof EntityBigBlock) {
                    System.out.println("업모드 다운모드 둘중 하나 활성화된상태");
                    EntityBigBlock bigBlock = (EntityBigBlock) this;
                    List<EntityLavaBlock> list = worldObj.getEntitiesWithinAABB(EntityLavaBlock.class, getEntityBoundingBox().offset(1, 0, 1).expand(1, 2, 1));
                    System.out.println("" + list.size() + " - " + list);
                }
                return super.processInteract(player, hand, stack);

            }
        }
        return super.processInteract(player, hand, stack);
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
                    DebAPI.msgText("LoopPre2","겹친 블럭 수" + list.size());
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
        if (CommandJB.isLavaInvisible && !(this instanceof EntityBigBlock)) {
            setInvisible(!isInv());
        } else
            setInvisible(isInv());
        if (LooPre2Event.prevText != null) {
            String xyz = LooPre2Event.prevText;
            if (xyz.split(",").length > 2) {
                try {
                    String xyzarray[] = xyz.split(",");
                    float x = Float.valueOf(xyzarray[0]);
                    float y = Float.valueOf(xyzarray[1]);
                    float z = Float.valueOf(xyzarray[2]);
                    setRotate(x, y, z);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    LooPre2Event.prevText = null;
                }
            }
        }
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
        return true;

    }

    public void teleportEnd() {

    }

    public void teleport() {
        if (WorldAPI.getPlayerMP() == null)
            return;
        if (DebAPI.isKeyDown(Keyboard.KEY_HOME)) {
            System.out.println(WorldAPI.getPlayer().getDistanceToEntity(this));
        }

        Vec3d vec = WorldAPI.getPlayer().getLookVec();
        this.setPositionAndRotationDirect(WorldAPI.x() + vec.xCoord * ax,
                WorldAPI.y() + WorldAPI.getPlayer().getEyeHeight() + vec.yCoord * ax,
                WorldAPI.z() + vec.zCoord * ax, 90, 90, 0, true);
        this.setPosition((WorldAPI.x() + vec.xCoord * ax),
                WorldAPI.y() + WorldAPI.getPlayerMP().getEyeHeight() + vec.yCoord * ax,
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
            EntityPreBlock lavaBlock = spawn(WorldAPI.x() + vec.xCoord * ax,
                    WorldAPI.y() + WorldAPI.getPlayerMP().getEyeHeight() + vec.yCoord * ax,
                    WorldAPI.z() + vec.zCoord * ax);
            lavaBlock.teleportEnd();
            lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());
            if (this instanceof EntityBuildBlock || this instanceof EntityBuildBlockMove) {
                lavaBlock.setDead();
                this.setTeleport(false);
                this.setPositionAndUpdate(WorldAPI.x() + vec.xCoord * ax,
                        WorldAPI.y() + WorldAPI.getPlayerMP().getEyeHeight() + vec.yCoord * ax,
                        WorldAPI.z() + vec.zCoord * ax);
                this.setSpawnXYZ(posX, posY, posZ);
                System.out.println(posX + " - " + posY + " - " + posZ + " 소환됨");
            } else {
                this.setDead();
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

    public EntityPreBlock spawn(double x, double y, double z) {
        return this;
    }

    public void setLock(boolean a) {
        this.dataManager.set(LOCK, a);
    }

    public boolean isLock() {
        return dataManager.get(LOCK);
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

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("falling", isLock());
        compound.setBoolean("isInv", isInv());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setLock(compound.getBoolean("falling"));
        setSize(compound.getFloat("widthl"), compound.getFloat("heightl"));
        setInvisible(compound.getBoolean("isInv"));
        setInv(compound.getBoolean("isInv"));

    }

    @Override
    public void setDead() {
        if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
            super.setDead();
        }
    }
}
