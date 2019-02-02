package map.lopre2.jump1;

import minigameLib.action.GrabHelper;
import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import map.lopre2.CommandJB;
import map.lopre2.EntityPreBlock;

import java.util.List;


public class EntityMoveBlock extends EntityPreBlock {
    protected double speed = 0.03D;
    public static double playerSpeed = 0.001D, playerYSpeed = 0.1;
    protected double[] startPos = new double[3];
    protected double endPos[] = new double[3];
    protected double entityPos[] = new double[3];
    protected double prevX, prevY, prevZ;
    //전체 블럭을 멈출 때 씀
    public static boolean allBlockMoveStop;
    //플레이어만 움직이는 벨트 블럭을 위해서 있음
    private static final DataParameter<Boolean> CAN_BLOCK_MOVE = EntityDataManager.createKey(EntityMoveBlock.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAN_PLAYER_MOVE = EntityDataManager.createKey(EntityMoveBlock.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Float> MOVE_DISTANCE = EntityDataManager.createKey(EntityMoveBlock.class,
            DataSerializers.FLOAT);
    private static final DataParameter<EnumFacing> FACING = EntityDataManager.createKey(EntityMoveBlock.class,
            DataSerializers.FACING);

    private static final DataParameter<Boolean> IS_BUILDBLOCK = EntityDataManager.createKey(EntityMoveBlock.class, DataSerializers.BOOLEAN);

    public EntityMoveBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
        this.setCollision(true);
        this.isFly = true;
    }

    @Override
    public String getCustomNameTag() {
        return "MoveBlock" + " 이동 중?" + !dataManager.get(CAN_BLOCK_MOVE) + " - 목적지" + (endPos != null ? endPos[0] + " - " + endPos[1] + " - " + endPos[2] : "");
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CAN_BLOCK_MOVE, true);
        dataManager.register(CAN_PLAYER_MOVE, false);
        dataManager.register(MOVE_DISTANCE, 2F);
        dataManager.register(FACING, EnumFacing.EAST);
        dataManager.register(IS_BUILDBLOCK, false);
    }

    public EnumFacing getFacing() {
        return dataManager.get(FACING);
    }

    public void setFacing(EnumFacing facing) {
        dataManager.set(FACING, facing);
    }

    public void setPos(EnumFacing facing, double y, float distance) {
        if (y != posY) {
            startPos[0] = posX;
            startPos[2] = posZ;
            startPos[1] = posY;
            endPos = new double[]{posX, y, posZ};
            setFacing(y > posY ? EnumFacing.UP : EnumFacing.DOWN);
            return;
        }
        startPos[0] = posX - EntityAPI.lookX(facing, distance);
        startPos[1] = posY;
        startPos[2] = posZ - EntityAPI.lookZ(facing, distance);
        this.endPos = new double[]{posX + EntityAPI.lookX(facing, distance), posY, posZ + EntityAPI.lookZ(facing, distance)};

        setFacing(facing);
        this.setMoveDistance(distance, false);
    }

    public void setMoveDistance(float moveDistance, boolean posReset) {
        dataManager.set(MOVE_DISTANCE, moveDistance);
        this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
        if (posReset)
            this.setPos(getFacing(), posY, moveDistance);
    }

    public void setPos(EntityPlayer player, double y, float distance) {
        this.setPos(player.getHorizontalFacing(), y, distance);
    }


    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        this.setTeleport(true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (CommandJB.isDebMode) {
            if (hand == EnumHand.MAIN_HAND) {
                if (WorldAPI.equalsHeldItem(Items.WHEAT)) {
                    if (player.isSneaking())
                        playerYSpeed+=0.01;
                    else
                        playerYSpeed-=0.01;
                    System.out.println(playerYSpeed);

                }
                if (WorldAPI.equalsHeldItem(Items.APPLE)) {
                    this.endPos = new double[]{posX + EntityAPI.lookX(player, 2), posY, posZ + EntityAPI.lookZ(player, 2)};
                    return true;
                }

                if (WorldAPI.equalsHeldItem(Items.BREAD) && !isServerWorld()) {
                    if (!player.isSneaking()) {
                        playerSpeed += 0.001;
                    } else {
                        playerSpeed -= 0.001;
                    }
                    System.out.println(" - " + playerSpeed);
                    return true;
                }
                if (WorldAPI.equalsHeldItem(Items.GOLDEN_APPLE) && isServerWorld()) {
                    setPlayerMove(!dataManager.get(CAN_PLAYER_MOVE));
                    System.out.println(dataManager.get(CAN_PLAYER_MOVE));
                    return true;
                }
            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (endPos != null) {
            compound.setDouble("startPosX", startPos[0]);
            compound.setDouble("startPosY", startPos[1]);
            compound.setDouble("startPosZ", startPos[2]);
            compound.setDouble("endPosX", endPos[0]);
            compound.setDouble("endPosY", endPos[1]);
            compound.setDouble("endPosZ", endPos[2]);
        }
        compound.setBoolean("noClip", noClip);
        compound.setBoolean("CAN_PLAYER_MOVE", dataManager.get(CAN_PLAYER_MOVE));
        compound.setFloat("MoveDistance", dataManager.get(MOVE_DISTANCE));
    }

    @Override
    public EntityMoveBlock spawn(double x, double y, double z) {
        EntityMoveBlock lavaBlock = new EntityMoveBlock(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());

        this.copyModel(lavaBlock);
        lavaBlock.prevBlock = prevBlock;
        lavaBlock.setBlockMode(getCurrentBlock());
        worldObj.spawnEntityInWorld(lavaBlock);
        return lavaBlock;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        noClip = compound.getBoolean("noClip");
        setPlayerMove(compound.getBoolean("CAN_PLAYER_MOVE"));
        setMoveDistance(compound.getFloat("MoveDistance"), false);
        if (compound.hasKey("startPosX")) {
            startPos = new double[3];
            endPos = new double[3];
            startPos[0] = compound.getDouble("startPosX");
            startPos[1] = compound.getDouble("startPosY");
            startPos[2] = compound.getDouble("startPosZ");
            endPos[0] = compound.getDouble("endPosX");
            endPos[1] = compound.getDouble("endPosY");
            endPos[2] = compound.getDouble("endPosZ");
            teleportSpawnPos();
        }
    }


    public boolean canBeLeashedTo(EntityPlayer player) {
        return true;
    }

    int delay;//벽에 막혔을 때 딜레이랑 플레이어가 처음 들어갔을 때 motionXYZ를 초기화 함 이걸 언제 다시 초기화 하게 할 건지 딜레이
    boolean onPlayer;//플레이어가 처음 들어가면 초기화함, onPlayer 플레이어가 있을 때

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        entityPos = WorldAPI.changePosArray(this);
        if ((startPos[0] == 0 && startPos[1] == 0 && startPos[2] == 0) || (endPos[0] == 0 && endPos[1] == 0 && endPos[2] == 0)) {
            return;
        }
        if (getDistance(endPos[0], endPos[1], endPos[2]) < 0.5) {
            double start2[] = startPos;
            double end2[] = endPos;
            startPos = end2;
            endPos = start2;
        }
        if ((Double.compare(posX, prevX) == 0 && Double.compare(posY, prevY) == 0 && Double.compare(posZ, prevZ) == 0)) {
            delay++;
            if (delay == 20) {
                delay = 0;
                double start2[] = startPos;
                double end2[] = endPos;
                startPos = end2;
                endPos = start2;
            }
        }

        double x = 0, y = 0, z = 0;
        prevX = posX;
        prevY = posY;
        prevZ = posZ;
        if (getFacing() == EnumFacing.UP || getFacing() == EnumFacing.DOWN) {
            if (endPos[1] > entityPos[1]) {
                y = speed;
            }
            if (endPos[1] < entityPos[1]) {
                y = -speed;
            }
        } else {
            if (endPos[0] > entityPos[0]) {
                x = speed;
            }
            if (endPos[0] < entityPos[0]) {
                x = -speed;
            }
            if (endPos[2] > entityPos[2]) {
                z = speed;
            }
            if (endPos[2] < entityPos[2]) {
                z = -speed;
            }
        }
        if (!dataManager.get(CAN_BLOCK_MOVE)) {
            x = 0;
            y = 0;
            z = 0;
        }

        if (getLeashed()) {
            speed = 0.09;
//                playerSP.motionX = ((posX - playerSP.posX) / 20) + x;
//                playerSP.motionZ = ((posZ - playerSP.posZ) / 20) + z;
//                playerSP.motionY = ((posY - 2.5 - playerSP.posY) / 10);
        }

        if (!allBlockMoveStop) {
            playerMove(x, y, z);
            moveEntity(x, y, z);
        }
    }

    private void playerMove(double x, double y, double z) {
        if (dataManager.get(CAN_PLAYER_MOVE)) {
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - 1D, this.posY, this.posZ - 1D, this.posX + 1D, this.posY + 1.8, this.posZ + 1D));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if ((entity instanceof EntityPlayer) && !entity.noClip && (GrabHelper.wallGrab || entity.posY > posY + 0.2)) {
                        if (GrabHelper.wallGrab || entity.onGround) {
                            double px = x, py = y, pz = z;
                            if (!GrabHelper.wallGrab && !onPlayer) {
                                entity.motionX = 0;
                                entity.motionY = 0;
                                entity.motionZ = 0;
                            }
                            if (px > 0)
                                px += playerSpeed;
                            if (px < 0)
                                px -= playerSpeed;
                            if (pz > 0)
                                pz += playerSpeed;
                            if (pz < 0)
                                pz -= playerSpeed;
                            if (py > 0) {
                                py += playerYSpeed;
                            }
//                                    if (playerSP.movementInput.jump) {
//                                        playerSP.jump();
//                                        playerSP.jump();
//                                        ActionEffect.setForceJump(true);
//                                    }
//                                    playerSP.moveEntity(px, py, pz);
                            entity.moveEntity(px, py, pz);
                            onPlayer = true;
                            break;
                        }
                    } else
                        onPlayer = false;
                }
            }
        }
    }

    public double[] getSpeed() {
        double x = 0, y = 0, z = 0;
        prevX = posX;
        prevY = posY;
        prevZ = posZ;
        if (endPos[1] > entityPos[1]) {
            y = speed;
        }
        if (endPos[1] < entityPos[1]) {
            y = -speed;
        }
        if (endPos[0] > entityPos[0]) {
            x = speed;
        }
        if (endPos[0] < entityPos[0]) {
            x = -speed;
        }
        if (endPos[2] > entityPos[2]) {
            z = speed;
        }
        if (endPos[2] < entityPos[2]) {
            z = -speed;
        }
        double px = x, py = y, pz = z;
        if (px > 0)
            px += playerSpeed - 0.0009D;
        if (px < 0)
            px -= playerSpeed - 0.0009D;
        if (pz > 0)
            pz += playerSpeed - 0.0009D;
        if (pz < 0)
            pz -= playerSpeed - 0.0009D;
        if (py > 0) {
            py += playerYSpeed;
        }
        return new double[]{px, py, pz};
    }

    public float getMoveDistance() {
        return dataManager.get(MOVE_DISTANCE);
    }

    public void setPlayerMove(boolean blockMove) {
        dataManager.set(CAN_PLAYER_MOVE, blockMove);
    }

    public void setBlockMove(boolean blockMove) {
        dataManager.set(CAN_BLOCK_MOVE, blockMove);
    }

    /*
     * 
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
                    this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 2, this.posZ + 0.5D));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if ((entity instanceof EntityPlayer) && !entity.noClip) {
                    	if(y != 0)
                    		motionY = 0;
                    	else if(Minecraft.getMinecraft().thePlayer != null)
                    		Minecraft.getMinecraft().thePlayer.moveEntity(x,0,z);
                    	System.out.println(entity);
                    }
                }
            }
     */

    @Override
    public void teleportEnd() {
        endPos[0] = 0;
        endPos[1] = 0;
        endPos[2] = 0;

        startPos[0] = 0;
        startPos[1] = 0;
        startPos[2] = 0;
    }

    @Override
    public boolean canTeleportLock() {
        return false;
    }
}