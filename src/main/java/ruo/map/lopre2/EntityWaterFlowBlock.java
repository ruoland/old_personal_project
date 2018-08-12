package ruo.map.lopre2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ruo.minigame.action.ActionEvent;
import ruo.minigame.action.GrabHelper;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

import java.util.List;

//물에 흘러가는 블럭
public class EntityWaterFlowBlock extends EntityPreBlock {
    private EnumFacing facing;//물에 흘러가는 방향. 수동으로 설정해야 함
    private static final DataParameter<Float> Y_SPEED = EntityDataManager.createKey(EntityWaterFlowBlock.class, DataSerializers.FLOAT);

    public EntityWaterFlowBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        setBlockMode(Blocks.STONE);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(Y_SPEED, -0.008F);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setSpawnXYZ(posX, posY, posZ);
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public String getCustomNameTag() {
        return "물에 흘러가는 블럭 내려가는 속도"+dataManager.get(Y_SPEED);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {//우클릭시 스폰된 장소로 되돌림 6월 21일자
            if (Keyboard.isKeyDown(Keyboard.KEY_L))
                this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
            if (WorldAPI.equalsItem(stack, LoPre2.itemSpanner)) {
                if (player.isSneaking()) {
                    this.dataManager.set(Y_SPEED, dataManager.get(Y_SPEED) + 0.01F);
                } else
                    this.dataManager.set(Y_SPEED, dataManager.get(Y_SPEED) - 0.01F);
                return true;

            }
        }
        return super.processInteract(player, hand, stack);
    }

    private double prevX, prevZ;
    private int delay = 20, waterDelay = 0;

    @Override
    public EntityWaterFlowBlock spawn(double x, double y, double z) {
        EntityWaterFlowBlock lavaBlock = new EntityWaterFlowBlock(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
        lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ(), 90, 90, 1, false);
        this.copyModel(lavaBlock);
        lavaBlock.prevBlock = prevBlock;
        lavaBlock.setBlockMode(getCurrentBlock());
        worldObj.spawnEntityInWorld(lavaBlock);
        return lavaBlock;

    }

    boolean isPlayer;//플레이어가 처음 블럭 위에 올라가는 경우 플레이어 motion을 0으로 바꾸는데 한번만 바꾸게 만들기 위해서 있음

    @Override
    public void onLivingUpdate() {
        if (isServerWorld() && delay > 0) {
            delay--;
            waterDelay--;
        }
        if (WorldAPI.getPlayer() != null) {
            if (!inWater && waterDelay == 0) {
                waterDelay = 40;
            }
            if ((!inWater && waterDelay == 1) || (posX == prevX && posZ == prevZ))
                this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
            if (!isTeleport() && delay == 0) {
                prevX = posX;
                prevZ = posZ;
                delay = 20;
            }
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - 1D, this.posY, this.posZ - 1D, this.posX + 1D, this.posY + 1.8, this.posZ + 1D));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (facing != null && (entity instanceof EntityPlayer) && !entity.noClip && (GrabHelper.wallGrab || entity.posY > posY + 0.2)) {
                        if (GrabHelper.wallGrab || entity.onGround) {
                            double px = EntityAPI.lookX(facing, 0.051), py = motionY / 10, pz = EntityAPI.lookZ(facing, 0.051);
                            if (!GrabHelper.wallGrab && !isPlayer) {
                                entity.setVelocity(0, 0, 0);
                                WorldAPI.getPlayerSP().setVelocity(0, 0, 0);
                            }
                            if (WorldAPI.getPlayerSP().movementInput.jump) {
                                WorldAPI.getPlayerSP().jump();
                                WorldAPI.getPlayerMP().jump();
                                ActionEvent.forceJump = true;
                            }
                            WorldAPI.getPlayerSP().moveEntity(px, py, pz);
                            WorldAPI.getPlayerMP().moveEntity(px, py, pz);
                            isPlayer = true;
                            break;
                        }
                    } else
                        isPlayer = false;
                }
            }
            this.motionY = dataManager.get(Y_SPEED);

        }
        super.onLivingUpdate();
    }

    @Override
    public boolean canTeleportLock() {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("spawnX", getSpawnX());
        compound.setDouble("spawnY", getSpawnY());
        compound.setDouble("spawnZ", getSpawnZ());
        compound.setFloat("Y_DOWN", dataManager.get(Y_SPEED));
        if (facing != null)
            compound.setString("facing", facing.getName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setSpawnXYZ(compound.getDouble("spawnX"), compound.getDouble("spawnY"), compound.getDouble("spawnZ"));
        facing = EnumFacing.byName(compound.getString("facing"));
        dataManager.set(Y_SPEED, compound.getFloat("Y_DOWN"));

    }


}
