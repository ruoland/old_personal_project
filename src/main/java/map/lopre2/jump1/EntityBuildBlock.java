package map.lopre2.jump1;

import cmplus.deb.DebAPI;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import map.lopre2.CommandJB;
import map.lopre2.EntityPreBlock;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityBuildBlock extends EntityPreBlock {

    private static HashMap<String, ArrayList<BlockPos>> blockPosHashMap = new HashMap<>();
    private static HashMap<String, ArrayList<ItemStack>> blockHashMap = new HashMap<>();

    public ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();
    public ArrayList<ItemStack> blockList = new ArrayList<ItemStack>();
    private static final DataParameter<BlockPos> BLOCK_POS1 = EntityDataManager.<BlockPos>createKey(EntityBuildBlock.class,
            DataSerializers.BLOCK_POS);
    private static final DataParameter<BlockPos> BLOCK_POS2 = EntityDataManager.<BlockPos>createKey(EntityBuildBlock.class,
            DataSerializers.BLOCK_POS);

    public EntityBuildBlock(World worldObj) {
        super(worldObj);
        this.setBlockMode(Blocks.STONE);
        isFly = true;
        this.setCollision(false);
        setJumpName("건물 블럭");
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }
    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityBuildBlock buildBlock = new EntityBuildBlock(worldObj);
        buildBlock.setPosition(x, y, z);
        buildBlock.setSpawnXYZ(x, y, z);
        worldObj.spawnEntityInWorld(buildBlock);
        buildBlock.setPosition(x, y, z);
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeEntityToNBT(tagCompound);
        buildBlock.readEntityFromNBT(tagCompound);
        buildBlock.setSpawnXYZ(x, y, z);
        buildBlock.setPosition(x, y, z);
        return buildBlock;
    }

    @Override
    public String getText() {
        return "건물 형태의 블럭입니다.";
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandXyz(10);
    }

    @Override
    public String getCustomNameTag() {
        return  getJumpName() + " RoX:" + getRotateX() + " RoY:" + getRotateY() + " RoZ:" + getRotateZ();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(BLOCK_POS1, BlockPos.ORIGIN);
        dataManager.register(BLOCK_POS2, BlockPos.ORIGIN);
    }

    public void setBlock(int xx, int yy, int zz, int x2, int y2, int z2) {
        dataManager.set(BLOCK_POS1, new BlockPos(xx, yy, zz));
        dataManager.set(BLOCK_POS2, new BlockPos(x2, y2, z2));
        WorldAPI.blockTick(worldObj, xx, x2, yy, y2, zz, z2, new AbstractTick.BlockXYZ() {
            @Override
            public void run(TickEvent.Type type) {
                IBlockState state = worldObj.getBlockState(getPos());
                Block block =state.getBlock();
                System.out.println(block);
                if (block != null && block != Blocks.AIR) {
                    blockPosList.add(new BlockPos(xx - x, yy - y, zz - z));
                    ItemStack itemStack = new ItemStack(state.getBlock(), 1, block.getMetaFromState(state));
                    if(state.getBlock() == Blocks.LIT_REDSTONE_ORE)
                        itemStack = new ItemStack(Blocks.REDSTONE_ORE);
                    blockList.add(itemStack);
                }
            }
        });
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (blockPosList.size() == 0 && blockPosHashMap.containsKey(this.getUniqueID().toString())) {
            this.blockPosList = blockPosHashMap.get(this.getUniqueID().toString());
            this.blockList = blockHashMap.get(this.getUniqueID().toString());
        }
        if (blockPosList.size() == 0 && getPos1().getX() != 0) {
            BlockPos pos1 = getPos1();
            BlockPos pos2 = getPos2();
            setBlock(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
        }
    }
    @Override
    public void targetArrive() {
        super.targetArrive();

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("BUILDSIZE", blockList.size());
        for (int i = 0; i < blockList.size(); i++) {
            ItemStack stack = blockList.get(i);
            BlockPos pos = blockPosList.get(i);
            compound.setTag(i + "-STACK", stack.serializeNBT());
            compound.setInteger(i + "-POSX", pos.getX());
            compound.setInteger(i + "-POSY", pos.getY());
            compound.setInteger(i + "-POSZ", pos.getZ());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        blockRead(compound);

    }

    public void blockRead(NBTTagCompound compound){
        int size = compound.getInteger("BUILDSIZE");
        for (int i = 0; i < size; i++) {
            ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag(i + "-STACK"));
            blockList.add(stack);
            blockPosList.add(new BlockPos(compound.getInteger(i + "-POSX"), compound.getInteger(i + "-POSY"), compound.getInteger(i + "-POSZ")));
        }

        blockPosHashMap.put(this.getUniqueID().toString(), blockPosList);
        blockHashMap.put(this.getUniqueID().toString(), blockList);
    }

    @Override
    public void setDead() {
        super.setDead();
        DebAPI.msgText("LoopPre2","빌드블럭 삭제됨" + getPosition());
    }

    public BlockPos getPos1() {
        return dataManager.get(BLOCK_POS1);
    }

    public BlockPos getPos2() {
        return dataManager.get(BLOCK_POS2);
    }
}
