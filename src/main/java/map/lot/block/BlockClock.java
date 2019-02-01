package map.lot.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockClock extends BlockContainer{
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1D, 1.0D);

	public BlockClock() {
		super(Material.ROCK);
		this.setHardness(10);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileClock();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(hand == EnumHand.MAIN_HAND && !worldIn.isRemote) {
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
		if(!worldIn.isRemote) {

		}
	}

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB_BOTTOM_HALF;
    }


}
