package map.lot.dungeon;

import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
import minigameLib.map.EntityDefaultNPC;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.World;
import map.lot.EntityFallBlock;

import javax.annotation.Nullable;
import java.util.List;

public class EntityWind extends EntityDefaultNPC {
    private static final DataParameter<Integer> WINDMODE = EntityDataManager.<Integer>createKey(EntityWind.class,
            DataSerializers.VARINT);    //0 = 작동 정지, 1 = 작동 중, 2 = 딜레이. 3 = 위, 4 = 아래
    private static final DataParameter<Float> WINDFACINGY = EntityDataManager.<Float>createKey(EntityWind.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> WINDFACINGXZ = EntityDataManager.<Float>createKey(EntityWind.class,
            DataSerializers.FLOAT);
    private float yaw, pitch;
    private int windDelay = 40;
    private boolean windStop;

    public EntityWind(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
        this.setCollision(false);
        this.setSize(1, 1);
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
    public boolean hasCustomName() {
        return WorldAPI.equalsHeldItem(Items.NAME_TAG) || WorldAPI.equalsHeldItem(Items.STICK) || WorldAPI.equalsHeldItem(Items.GOLDEN_APPLE);
    }

    @Override
    public String getCustomNameTag() {
        StringBuffer name = new StringBuffer();
        if (getWindMode() == 0)
            name.append("작동 정지");
        if (getWindMode() >= 1) {
            name.append("작동 중");
        }
        if (getWindMode() >= 3)
            name.append(" Y" + getFacingY());
        else
            name.append(" XZ" + getFacingXZ());

        name.append(": 방향 :" + this.getHorizontalFacing().getName());
        return name.toString();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity().isSneaking()) {
            this.setDead();
        }
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(WINDMODE, 1);
        dataManager.register(WINDFACINGY, 10F);
        dataManager.register(WINDFACINGXZ, 5F);
    }

    public void addFacingY(float y) {
        setFacingY(getFacingY() + y);
    }

    public void setFacingY(float y) {
        getDataManager().set(WINDFACINGY, y);
    }

    public float getFacingY() {
        return getDataManager().get(WINDFACINGY);
    }

    public void addFacingXZ(float y) {
        setFacingXZ(getFacingXZ() + y);
    }

    public void setFacingXZ(float y) {
        getDataManager().set(WINDFACINGXZ, y);
    }

    public float getFacingXZ() {
        return getDataManager().get(WINDFACINGXZ);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (stack != null) {
            if (stack.getItem() == Items.NAME_TAG)
                this.setRotation(rotationYaw + 90, rotationPitch);
            if (stack.getItem() == Items.GOLDEN_APPLE) {
                if (getWindMode() == 4)
                    setWindMode(3);
                else
                    setWindMode(4);
            }
            if (stack.getItem() == Items.BREAD) {
                this.addScale(1);
            }
            if (stack.getItem() == Items.STICK) {
                if (getWindMode() == 4 || getWindMode() == 3) {
                    if (player.isSneaking())
                        addFacingY(-0.5F);
                    else
                        addFacingY(0.5F);
                } else {
                    if (player.isSneaking())
                        addFacingXZ(-0.5F);
                    else
                        addFacingXZ(0.5F);
                }

            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
        super.setRotation(yaw, pitch);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setWindMode(int windmode) {
        dataManager.set(WINDMODE, windmode);
        if (windmode == 3) {
            this.setRotation(rotationYaw, -90);
        } else if (windmode == 4) {
            this.setRotation(rotationYaw, 90);
        } else
            this.setRotation(rotationYaw, -90);
    }

    public int getWindMode() {
        return dataManager.get(WINDMODE);
    }

    private boolean isBlock;
    private double blockY;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        int windMode = getDataManager().get(WINDMODE).intValue();
        if (WorldAPI.getPlayer() != null && windMode > 0) {
            if (windMode == 2) {//바람을 멈췄다가 실행함
                if (windDelay > 0)
                    windDelay--;
                else if (!windStop) {
                    windStop = true;
                    windDelay = 40;
                } else {
                    windStop = false;
                    windDelay = 40;
                }
            }
            if (!windStop) {
                List<Entity> aabb = EntityAPI.getEntity(worldObj, this.getEntityBoundingBox().addCoord(
                        facingX(), facingY(), facingZ()), Entity.class);
                for (Entity entity : aabb) {//블럭이 있는지 체크
                    if (entity instanceof EntityFallBlock) {
                        if (getWindMode() >= 3) {
                            blockY = entity.posY;
                            isBlock = true;
                            ((EntityFallBlock) entity).setRandomDelay(-190);
                            ((EntityFallBlock) entity).setMaxY((float) facingY());
                        }else {
                            ((EntityFallBlock) entity).setRandomDelay(-190);
                            ((EntityFallBlock) entity).setMaxY((float) entity.posY);
                        }

                    }
                }
                for (Entity base : aabb) {
                    if (base instanceof EntityWind)
                        continue;
                    base.fallDistance = 0;
                    if (base instanceof EntityFallBlock) {
                        if (getWindMode() >= 3) {
                            base.addVelocity(0, 0.05, 0);
                            continue;
                        }
                    }
                    if ((isBlock && base.posY > blockY)) {//블럭이 있으면
                        break;
                    }
                    if (getWindMode() == 3) {
                        base.addVelocity(0, 0.3, 0);
                        continue;
                    }
                    if (getWindMode() == 4) {
                        base.addVelocity(0, -0.3, 0);
                        continue;
                    } else
                        base.addVelocity(EntityAPI.getFacingX(rotationYaw) / 15, EntityAPI.getFacingY(rotationPitch) / 15, EntityAPI.getFacingZ(rotationYaw) / 15);
                }
            }
        }
    }


    public double facingX() {
        if (getWindMode() < 3) {
            return 0.5 + EntityAPI.getFacingX(rotationYaw) * getFacingXZ();
        }
        return 0;
    }

    public double facingZ() {
        if (getWindMode() < 3) {
            return 0.5 + EntityAPI.getFacingZ(rotationYaw) * getFacingXZ();
        }
        return 0;
    }

    public double facingY() {
        if (getWindMode() >= 3) {
            return getFacingY();
        } else
            return 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("windmode", getWindMode());
        compound.setInteger("winddelay", windDelay);
        compound.setBoolean("windstop", windStop);
        compound.setFloat("windyaw", yaw);
        compound.setFloat("windpitch", pitch);
        compound.setFloat("windfacingy", getFacingY());
        compound.setFloat("windfacingxz", getFacingXZ());
        compound.setBoolean("windFly", isFly);


    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setWindMode(compound.getInteger("windmode"));
        windDelay = compound.getInteger("winddelay");
        windStop = compound.getBoolean("windstop");
        pitch = compound.getFloat("windpitch");
        yaw = compound.getFloat("windyaw");
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        setFacingY(compound.getFloat("windfacingy"));
        setFacingXZ(compound.getFloat("windfacingxz"));

        isFly = compound.getBoolean("windFly");

    }


}
