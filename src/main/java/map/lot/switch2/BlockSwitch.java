package map.lot.switch2;

import cmplus.cm.CommandChat;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSwitch extends BlockContainer{
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1D, 1.0D);

	public BlockSwitch() {
		super(Material.ROCK);
		this.setHardness(10);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSwitch();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(hand == EnumHand.MAIN_HAND) {
			TileSwitch ts = (TileSwitch) worldIn.getTileEntity(pos);
			if(heldItem.getItem() == Items.NAME_TAG && CommandChat.getLastChat() != null)
				ts.setCommand("/"+CommandChat.getLastChat());
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
		TileSwitch ts = (TileSwitch) worldIn.getTileEntity(pos);
		ts.setOn(true);
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		super.onBlockExploded(world, pos, explosion);
		TileSwitch ts = (TileSwitch) world.getTileEntity(pos);
		ts.setOn(true);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		if(entityIn instanceof EntityArrow){
			TileSwitch ts = (TileSwitch) worldIn.getTileEntity(pos);
			ts.setOn(true);
			System.out.println("스위치 온 됨");
		}
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB_BOTTOM_HALF;
    }

}
