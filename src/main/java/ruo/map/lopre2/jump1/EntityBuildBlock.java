package ruo.map.lopre2.jump1;

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
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.CommandJB;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityBuildBlock extends EntityPreBlock {
    public static EntityBuildBlock eBlock, nBlock, dBlock;

    private static HashMap<String, ArrayList<BlockPos>> blockPosHashMap = new HashMap<>();
    private static HashMap<String, ArrayList<ItemStack>> blockHashMap = new HashMap<>();

    public ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();
    public ArrayList<ItemStack> blockList = new ArrayList<ItemStack>();
    private static final DataParameter<String> CUSTOM_NAME = EntityDataManager.<String>createKey(EntityBuildBlock.class,
            DataSerializers.STRING);
    private static final DataParameter<BlockPos> BLOCK_POS1 = EntityDataManager.<BlockPos>createKey(EntityBuildBlock.class,
            DataSerializers.BLOCK_POS);
    private static final DataParameter<BlockPos> BLOCK_POS2 = EntityDataManager.<BlockPos>createKey(EntityBuildBlock.class,
            DataSerializers.BLOCK_POS);

    public EntityBuildBlock(World worldObj) {
        super(worldObj);
        this.setBlockMode(Blocks.STONE);
        isFly = true;
        this.setCollision(false);
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
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (isServerWorld()) {
            if (WorldAPI.equalsItem(stack, Items.APPLE)) {
                dataManager.set(CUSTOM_NAME, "E 블럭");
                System.out.println("E 블럭됨");
                eBlock = (EntityBuildBlock) spawn(posX, posY, posZ);
                eBlock.setInv(true);
                eBlock.setCustomName("빌드 블럭");
            }
            if (WorldAPI.equalsItem(stack, Items.BREAD)) {
                dataManager.set(CUSTOM_NAME, "N 블럭");
                System.out.println("N 블럭됨");
                nBlock = (EntityBuildBlock) spawn(posX, posY, posZ);
                nBlock.setInv(true);
                nBlock.setCustomName("빌드 블럭");
            }
            if (WorldAPI.equalsItem(stack, Items.CHICKEN)) {
                dataManager.set(CUSTOM_NAME, "D 블럭");
                System.out.println("D 블럭됨");
                dBlock = (EntityBuildBlock) spawn(posX, posY, posZ);
                dBlock.setInv(true);
                dBlock.setCustomName("빌드 블럭");

            }
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandXyz(10);
    }

    public void setCustomName(String name) {
        dataManager.set(CUSTOM_NAME, name);
    }

    public String getCustomName() {
        return dataManager.get(CUSTOM_NAME);
    }

    @Override
    public String getCustomNameTag() {
        return "빌드 블럭 " + " RoX:" + getRotateX() + " RoY:" + getRotateY() + " RoZ:" + getRotateZ();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(BLOCK_POS1, BlockPos.ORIGIN);
        dataManager.register(BLOCK_POS2, BlockPos.ORIGIN);
        dataManager.register(CUSTOM_NAME, "빌드 블럭");
    }

    public void setBlock(int xx, int yy, int zz, int x2, int y2, int z2) {
        dataManager.set(BLOCK_POS1, new BlockPos(xx, yy, zz));
        dataManager.set(BLOCK_POS2, new BlockPos(x2, y2, z2));
        WorldAPI.blockTick(worldObj, xx, x2, yy, y2, zz, z2, new AbstractTick.BlockXYZ() {
            @Override
            public void run(TickEvent.Type type) {
                if (worldObj.getBlockState(getPos()).getBlock() != null && worldObj.getBlockState(getPos()).getBlock() != Blocks.AIR) {
                    blockPosList.add(new BlockPos(xx - x, yy - y, zz - z));
                    IBlockState state = worldObj.getBlockState(getPos());
                    Block block = state.getBlock();
                    blockList.add(new ItemStack(state.getBlock(), 1, block.getMetaFromState(state)));
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

        if (CommandJB.endTime != 0 && eBlock != null) {
            if (!eBlock.isInv()) {
                String customName = dataManager.get(CUSTOM_NAME);
                System.out.println(customName + getTargetPosition());
                if (customName.equalsIgnoreCase("E 블럭")) {
                    setInv(true);
                }
                if (customName.equalsIgnoreCase("N 블럭")) {
                    setInv(true);
                }
                if (customName.equalsIgnoreCase("D 블럭")) {
                    setInv(true);
                }
            }
            eBlock.setTeleportLock(false);
            nBlock.setTeleportLock(false);
            dBlock.setTeleportLock(false);
            eBlock.setInv(false);
            nBlock.setInv(false);
            dBlock.setInv(false);
            eBlock.setTarget(1127, 247, -70, 1);
            nBlock.setTarget(1127, 247, -61, 1);
            dBlock.setTarget(1127, 249, -56);
            Vec3d eVec = new Vec3d(-180 - eBlock.getRotateX(), 90 - eBlock.getRotateY(), -180 - eBlock.getRotateZ()).normalize().scale(0.6);
            Vec3d nVec = new Vec3d(-180 - nBlock.getRotateX(), 0 - nBlock.getRotateY(), -180 - nBlock.getRotateZ()).normalize().scale(0.8);
            Vec3d dVec = new Vec3d(-180 - dBlock.getRotateX(), 0 - dBlock.getRotateY(), -180 - dBlock.getRotateZ()).normalize().scale(0.3);

            eBlock.addRotate(eVec.xCoord, eVec.yCoord, eVec.zCoord);
            nBlock.addRotate(nVec.xCoord, nVec.yCoord, nVec.zCoord);
            dBlock.addRotate(dVec.xCoord, dVec.yCoord, dVec.zCoord);
        }
    }

    @Override
    public void targetArrive() {
        super.targetArrive();

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("customName", dataManager.get(CUSTOM_NAME));
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
        dataManager.set(CUSTOM_NAME, compound.getString("customName"));
        if (dataManager.get(CUSTOM_NAME).equalsIgnoreCase("E 블럭")) {
            eBlock = this;
        }
        if (dataManager.get(CUSTOM_NAME).equalsIgnoreCase("N 블럭")) {
            nBlock = this;
        }
        if (dataManager.get(CUSTOM_NAME).equalsIgnoreCase("D 블럭")) {
            dBlock = this;
        }

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
