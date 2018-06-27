package ruo.map.lopre2;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityBuildBlock extends EntityPreBlock {
    /*
    	if(npc instanceof EntityBuildBlock){
				EntityBuildBlock block = (EntityBuildBlock) npc;
				if(block.blockList.size() == 0) {
					return;
				}
				GlStateManager.pushMatrix();
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(npc.getRed(),npc.getGreen(), npc.getBlue(), npc.getTransparency());
				GlStateManager.rotate(npc.getRotateX(), 1, 0, 0);
				GlStateManager.rotate(npc.getRotateY(), 0, 1, 0);
				GlStateManager.rotate(npc.getRotateZ(), 0, 0, 1);
				GlStateManager.scale(npc.getScaleX(), npc.getScaleY(), npc.getScaleZ());
				for(int i = 0; i < block.blockPosList.size();i++){
					BlockPos pos = block.blockPosList.get(i);
					if(block.blockList.get(i) == null) {
						continue;
					}
					GlStateManager.pushMatrix();
					GlStateManager.translate(pos.getX(),pos.getY(),pos.getZ());
					RenderAPI.renderBlock(block.blockList.get(i), npc);
					GlStateManager.popMatrix();
				}
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
				return;
			}
     */
    private static HashMap<String, ArrayList<BlockPos>> blockPosHashMap = new HashMap<>();
    private static HashMap<String, ArrayList<ItemStack>> blockHashMap = new HashMap<>();

    public ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();
    public ArrayList<ItemStack> blockList = new ArrayList<ItemStack>();

    private static final DataParameter<BlockPos> BLOCKPOS1 = EntityDataManager.<BlockPos>createKey(EntityBuildBlock.class,
            DataSerializers.BLOCK_POS);
    private static final DataParameter<BlockPos> BLOCKPOS2 = EntityDataManager.<BlockPos>createKey(EntityBuildBlock.class,
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

        NBTTagCompound tagCompound = new NBTTagCompound();
        writeEntityToNBT(tagCompound);
        buildBlock.readEntityFromNBT(tagCompound);
        return buildBlock;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandXyz(10);
    }

    @Override
    public String getCustomNameTag() {
        return "빌드 블럭 "+" RoX:"+getRotateX()+" RoY:"+getRotateY()+" RoZ:"+getRotateZ();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(BLOCKPOS1, BlockPos.ORIGIN);
        dataManager.register(BLOCKPOS2, BlockPos.ORIGIN);
    }



    public void setBlock(int xx, int yy, int zz, int x2, int y2, int z2) {
        dataManager.set(BLOCKPOS1, new BlockPos(xx, yy, zz));
        dataManager.set(BLOCKPOS2, new BlockPos(x2, y2, z2));
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
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        return super.processInteract(player, hand, stack);
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
        System.out.println("빌드블럭 삭제됨" + getPosition());
    }

    public BlockPos getPos1() {
        return dataManager.get(BLOCKPOS1);
    }

    public BlockPos getPos2() {
        return dataManager.get(BLOCKPOS2);
    }
}
