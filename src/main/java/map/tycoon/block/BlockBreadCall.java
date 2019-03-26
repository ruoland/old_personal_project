package map.tycoon.block;

import oneline.api.WorldAPI;
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

public class BlockBreadCall extends BlockContainer{
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1D, 1.0D);

	public BlockBreadCall() {
		super(Material.ROCK);
		this.setHardness(10);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBreadCall();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(hand == EnumHand.MAIN_HAND && !worldIn.isRemote) {
			TileBreadCall call = (TileBreadCall) worldIn.getTileEntity(pos);
			if(playerIn.isSneaking())
				call.subBread();
			else
				call.addBread();
			call.sendUpdates();
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
		if(!worldIn.isRemote) {
			TileBreadCall call = (TileBreadCall) worldIn.getTileEntity(pos);
			call.callBread(5);
			call.sendUpdates();
			WorldAPI.addMessage("빵 주문을 시작했습니다.");

		}
	}

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB_BOTTOM_HALF;
    }


}
