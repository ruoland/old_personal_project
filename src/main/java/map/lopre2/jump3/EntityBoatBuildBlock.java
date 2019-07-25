package map.lopre2.jump3;

import map.lopre2.EntityPreBlock;
import map.lopre2.jump1.EntityBuildBlock;
import map.lopre2.jump1.EntityLavaBlock;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import olib.api.WorldAPI;

import java.util.List;

public class EntityBoatBuildBlock extends EntityBuildBlock {
    private static final DataParameter<Float> MO_X = EntityDataManager.createKey(EntityBoatBuildBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> MO_Z = EntityDataManager.createKey(EntityBoatBuildBlock.class, DataSerializers.FLOAT);
    public static float path, moX, moZ;
    private static final DataParameter<Boolean> MOVE_STOP = EntityDataManager.createKey(EntityBoatBuildBlock.class, DataSerializers.BOOLEAN);

    public EntityBoatBuildBlock(World worldObj) {
        super(worldObj);
        isFly = true;
        setBlockMode(Blocks.STONE);
        setCollision(true);
        setTeleportLock(false);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(MO_X, 0F);
        dataManager.register(MO_Z, 0F);
        dataManager.register(MOVE_STOP, false);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityPlayer) {
            dataManager.set(MOVE_STOP, !dataManager.get(MOVE_STOP));
            System.out.println(dataManager.get(MOVE_STOP));

            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - 3D, this.posY, this.posZ - 3D, this.posX + 3D, this.posY + 4, this.posZ + 3D));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof EntityLavaBlock && !((EntityLavaBlock) entity).isBoatBlock()) {
                        ((EntityLavaBlock) entity).teleportSpawnPos();
                    }
                }
            }
            teleportSpawnPos();
            if (WorldAPI.equalsHeldItem(Items.APPLE))
                return super.attackEntityFrom(source, amount);
            else
                return false;

        } else
            return false;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            if (moX != 0 && moZ != 0) {
                System.out.println(moX + " - " + moZ);
                setMoX((float) (moX - posX) / 200);
                setMoZ((float) (moZ - posZ) / 200);
                moX = 0;
                moZ = 0;
                System.out.println("설정됨" + getMoX() + " - " + getMoZ());
            }
        }
        return super.processInteract(player, hand, stack);
    }

    public void setMoX(float x) {
        dataManager.set(MO_X, x);
    }

    public void setMoZ(float z) {
        dataManager.set(MO_Z, z);
    }

    public float getMoX() {
        return dataManager.get(MO_X);
    }

    public float getMoZ() {
        return dataManager.get(MO_Z);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityBoatBuildBlock lavaInvisible = new EntityBoatBuildBlock(worldObj);
        dataCopy(lavaInvisible, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaInvisible);
        }
        System.out.println(lavaInvisible.getMoX() + " - " + lavaInvisible.getMoZ());
        return lavaInvisible;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!dataManager.get(MOVE_STOP) && getMoX() != 0 && getMoZ() != 0) {
            if (!isTeleport()) {
                List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                        this.posX - 3D, this.posY, this.posZ - 3D, this.posX + 3D, this.posY + 4, this.posZ + 3D));
                if (!list.isEmpty()) {
                    for (Entity entity : list) {
                        if ((entity instanceof EntityPlayer) && !entity.noClip) {
                            if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
                                entity.motionY = Minecraft.getMinecraft().thePlayer.jumpMovementFactor;
                            } else if (entity.onGround) {
                                entity.moveEntity(getMoX(), 0, getMoZ());
                            }
                        }
                        if (entity instanceof EntityLavaBlock) {
                            ((EntityLavaBlock) entity).setTeleportLock(false);
                            ((EntityLavaBlock) entity).setBoatBlock();
                            entity.moveEntity(getMoX(), 0, getMoZ());
                        }
                    }
                }
            }
            moveEntity(getMoX(), 0, getMoZ());
        }
    }

    @Override
    public String getText() {
        return "용암위에 있으면 아무 방향으로 이동하는 블럭입니다";
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("mox", getMoX());
        compound.setDouble("moz", getMoZ());
        compound.setBoolean("movestop", dataManager.get(MOVE_STOP));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setMoX((float) compound.getDouble("mox"));
        setMoZ((float) compound.getDouble("moz"));
        dataManager.set(MOVE_STOP, compound.getBoolean("movestop"));
    }
}
