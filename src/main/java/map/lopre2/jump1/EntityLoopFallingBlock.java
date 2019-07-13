package map.lopre2.jump1;

import olib.api.WorldAPI;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import map.lopre2.CommandJB;
import map.lopre2.EntityPreBlock;
import map.lopre2.LoPre2;

//반복해서 떨어지는 블럭 - 6월 21일
//1탄에서 용암에 떨어지는 블럭으로 쓰임
//용암에 들어서는 경우 천천히 용암속으로 사라짐

public class EntityLoopFallingBlock extends EntityPreBlock {

    private static final DataParameter<Float> LAVA_Y = EntityDataManager.createKey(EntityLoopFallingBlock.class, DataSerializers.FLOAT);

    public EntityLoopFallingBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.GRAVEL);
        this.setCollision(true);
        setJumpName("떨어지는 블럭");
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(LAVA_Y, 0F);
    }

    @Override
    public EntityLoopFallingBlock spawn(double x, double y, double z) {
        EntityLoopFallingBlock lavaBlock = new EntityLoopFallingBlock(worldObj);
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
        lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3,
                lavaBlock.getSpawnZ(), 90, 90, 1, false);
        this.copyModel(lavaBlock);
        lavaBlock.prevBlock = prevBlock;
        lavaBlock.setBlockMode(getCurrentBlock());
        worldObj.spawnEntityInWorld(lavaBlock);
        return lavaBlock;
    }

    @Override
    public String getText() {
        return "이 블럭은 용암에 떨어지면 잠깐의 시간이 지난 후 소환된 장소로 다시 이동합니다.";
    }

    public float getLavaY() {
        return dataManager.get(LAVA_Y);
    }

    public void setLavaY(float i) {
        dataManager.set(LAVA_Y, i);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        if (getLavaY() == 0)
            lavaYUpdate();
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public void lavaYUpdate() {
        for (float i = 0; i < 70; i += 0.2) {
            Block block = worldObj.getBlockState(new BlockPos(getSpawnX(), posY - i, getSpawnZ()))
                    .getBlock();
            if (block == Blocks.LAVA) {
                setLavaY((float) (posY - i));
                break;
            }
        }
    }

    @Override
    public String getCustomNameTag() {
        return getJumpName()+ getLavaY();
    }

    private boolean firstReset = false;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!isTeleport()) {
            AxisAlignedBB aabb = getEntityBoundingBox().expand(-0.1, -0.25F, -0.1);
            if (this.worldObj.isMaterialInBB(aabb, Material.LAVA)) {
                if (!firstReset) {
                    setPositionAndUpdate(posX, getLavaY() - 0.3, posZ);
                    firstReset = true;
                }
                motionX = 0;
                motionY = -0.003;
                motionZ = 0;
            }
            if (getLavaY() - 1 > posY) {
                this.setPositionAndUpdate(posX, getSpawnY(), posZ);
                firstReset = false;
                lavaYUpdate();
            }
        }
    }

    @Override
    public void teleportEnd() {
        super.teleportEnd();
        lavaYUpdate();
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (CommandJB.isDebMode) {
            if (WorldAPI.equalsItem(stack, LoPre2.itemSpanner)) {
                this.setPositionAndUpdate(posX, getSpawnY(), posZ);
                setLavaY(0);
                lavaYUpdate();
            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("lavaY", getLavaY());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setLavaY(compound.getFloat("lavaY"));
        System.out.println(getLavaY());
    }

}
