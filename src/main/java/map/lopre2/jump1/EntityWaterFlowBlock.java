package map.lopre2.jump1;

import oneline.api.WorldAPI;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import map.lopre2.EntityPreBlock;
import map.lopre2.LoPre2;

//물에 흘러가는 블럭
public class EntityWaterFlowBlock extends EntityPreBlock {
    private EnumFacing facing;//물에 흘러가는 방향. 수동으로 설정해야 함
    private static final DataParameter<Float> Y_SPEED = EntityDataManager.createKey(EntityWaterFlowBlock.class, DataSerializers.FLOAT);
    private float ySpeed = -0.008F;
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
        return "물에 흘러가는 블럭 내려가는 속도"+getSpeed();
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {//우클릭시 스폰된 장소로 되돌림 6월 21일자
            if (Keyboard.isKeyDown(Keyboard.KEY_L))
                this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
            if (WorldAPI.equalsItem(stack, LoPre2.itemSpanner)) {
                if (player.isSneaking()) {
                    setSpeed(getSpeed() + 0.01F);
                } else
                    setSpeed(getSpeed() - 0.01F);
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


    @Override
    public void onLivingUpdate() {
        if (isServerWorld() && delay > 0) {
            delay--;
            waterDelay--;
        }
        if (WorldAPI.getPlayer() != null) {

            if (!inWater && waterDelay == 0) {
                waterDelay = 40;
                ySpeed = dataManager.get(Y_SPEED);
            }
            if ((!inWater && waterDelay == 1) || (posX == prevX && posZ == prevZ))
                this.setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
            if (!isTeleport() && delay == 0) {
                prevX = posX;
                prevZ = posZ;
                delay = 20;
            }
            this.motionY = getSpeed();
        }
        if(ySpeed == 0) {
            setDead();
            System.out.println("Y가 0이라 삭제함");
        }
        super.onLivingUpdate();
    }

    public void setSpeed(float speed){
        this.dataManager.set(Y_SPEED, speed);
        ySpeed = speed;
    }

    public float getSpeed(){
        return ySpeed;
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
