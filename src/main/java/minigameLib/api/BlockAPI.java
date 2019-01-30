package minigameLib.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.ArrayList;

public class BlockAPI  implements Serializable {
	private transient ArrayList<IBlockState> blockList = new ArrayList<IBlockState>();
	private final ArrayList<Integer> blockId = new ArrayList<>();
	private final ArrayList<BlockPos> blockPosList = new ArrayList<>();
	public BlockAPI() {
	}
	public BlockAPI(World w, int x, int y, int z) {
		this(w.getBlockState(new BlockPos(x,y,z)).getBlock(), x,y,z);
	}
	public BlockAPI(Block block, int x, int y, int z) {
		addBlock(block,x,y,z);
	}
	
	public void addBlock(Block block, int x, int y, int z){
		this.blockList.add(block.getDefaultState());
		this.blockPosList.add(new BlockPos(x,y,z));
		this.blockId.add(Block.getStateId(block.getDefaultState()));
	}
	public void addBlock(World w, int x, int y, int z){
		this.addBlock(w.getBlockState(new BlockPos(x, y, z)).getBlock(),x,y,z);
	}
	public void addBlock(World w, BlockPos pos){
		this.addBlock(w.getBlockState(pos).getBlock(),pos.getX(), pos.getY(), pos.getZ());
	}
	public int size(){
		return blockList.size();
	}
	
	public BlockPos getPos(int id){
		return blockPosList.get(id);
	}
	public BlockPos getFirstPos(){
		return blockPosList.get(0);
	}
	public ArrayList<BlockPos> getPosList(){
		return blockPosList;
	}
	public IBlockState getBlockState(int id){
		return this.blockList.get(id);
	}
	public Block getBlock(int id){
		return this.blockList.get(id).getBlock();
	}

}
