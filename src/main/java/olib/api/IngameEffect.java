package olib.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class IngameEffect {

	public static void addChestItem(int index, double x, double y, double z, ItemStack stack) {
		addChestItem(index, new BlockPos(x, y, z), stack);
	}

	public static void addChestItem(int index, BlockPos pos, ItemStack stack) {
		ILockableContainer ilock = Blocks.CHEST.getLockableContainer(WorldAPI.getWorld(), pos);
		ilock.setInventorySlotContents(index, stack);
	}

	public static void lever(BlockPos pos, boolean onoff) {
		lever(pos.getX(), pos.getY(), pos.getZ(), onoff);
	}
	public static void lever(double x, double y, double z, boolean onoff) {
		BlockPos pos = new BlockPos(x,y,z);
		if (WorldAPI.getBlock(pos) instanceof BlockLever && (isLever(x,y,z) != onoff)) {
			BlockLever lever = (BlockLever) WorldAPI.getBlock(pos);
			lever.onBlockActivated(WorldAPI.getWorld(), pos, WorldAPI.getWorld().getBlockState(pos),
					WorldAPI.getPlayer(), EnumHand.MAIN_HAND, null, EnumFacing.EAST, 0, 0, 0);
		} else {
			System.out.println("레버를 제대로 설정하지 못했습니다. "+"블럭 캐스팅 여부 : "+(WorldAPI.getBlock(pos) instanceof BlockLever)+"블럭 :"+WorldAPI.getBlock(pos).getLocalizedName() +".레버 상태:"+isLever(x,y,z)+ " onoff 변수"+ onoff);
		}
	}
	public static boolean isLever(double x, double y, double z) {
		BlockPos pos = new BlockPos(x,y,z);
		if (WorldAPI.getBlock(pos) instanceof BlockLever) {
			World worldIn = WorldAPI.getWorld();
			IBlockState state = WorldAPI.getWorld().getBlockState(pos);
			BlockLever lever = (BlockLever) state.getBlock();
			PropertyBool POWERED = null;
			try {
				POWERED = (PropertyBool) ReflectionHelper.findField(BlockLever.class, "POWERED").get(lever);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return state.getValue(POWERED).booleanValue();
		} 
			System.out.println(
					pos.getX() + " --- " + pos.getY() + " --- " + pos.getZ() + "의 블럭이 레버가 아니라 레버가 어떤 상태인지 알지 못했습니다.");
		
		return false;
	}
	public static void openChestGui(BlockPos pos) {
		ILockableContainer ilockablecontainer = Blocks.CHEST.getLockableContainer(WorldAPI.getWorld(), pos);
		WorldAPI.getPlayer().displayGUIChest(ilockablecontainer);
	}

	public static void openChestEffect(BlockPos pos, boolean isOpen) {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		Block block = WorldAPI.getBlock(i, j, k).getBlock();
		double d1 = (double) i + 0.5D;
		double d2 = (double) k + 0.5D;
		double d3 = (double) i + 0.5D;
		double d0 = (double) k + 0.5D;
		WorldAPI.getWorld().addBlockEvent(pos, block, 1, isOpen ? 1 : 0);
		WorldAPI.getWorld().notifyNeighborsOfStateChange(pos, block);
		WorldAPI.getWorld().notifyNeighborsOfStateChange(pos.down(), block);
		WorldAPI.getWorld().playSound((EntityPlayer) null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN,
				SoundCategory.BLOCKS, 0.5F, WorldAPI.getWorld().rand.nextFloat() * 0.1F + 0.9F);
	}

	public static void openDoor(BlockPos pos, boolean isOpen) {
		if (WorldAPI.getBlock(pos) instanceof BlockDoor) {
			BlockDoor door = (BlockDoor) WorldAPI.getBlock(pos);
			door.toggleDoor(WorldAPI.getWorld(), pos, isOpen);
		}
	}

	public static boolean isOpenDoor(BlockPos pos) {
		BlockDoor door = (BlockDoor) WorldAPI.getBlock(pos);
		return BlockDoor.isOpen(WorldAPI.getWorld(), pos);
	}
	
}
