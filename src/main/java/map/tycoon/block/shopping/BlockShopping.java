package map.tycoon.block.shopping;

import minigameLib.api.WorldAPI;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
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
import map.tycoon.BreadData;
import map.tycoon.block.TileBreadDisplay;

public class BlockShopping extends BlockContainer {
	protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

	public BlockShopping() {
		super(Material.ROCK);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBreadDisplay();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (hand == EnumHand.MAIN_HAND) {
			TileShopping tilebread = (TileShopping) worldIn.getTileEntity(pos);
			BreadData bread = tilebread.getBread();
			if (playerIn.isCreative()) {
				if (bread != null && bread.nameCheck(heldItem)) {// 같은 아이템을 들고 우클릭
					bread.addAmount(heldItem.stackSize);
					playerIn.inventory.removeStackFromSlot(playerIn.inventory.currentItem);
				} else if (bread == null && heldItem != null) {// 아이템 설정
					tilebread.setItem(heldItem);
				} else if (bread != null) {//
					EntityItem item = bread.getEntityItem();
					item.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
					worldIn.spawnEntityInWorld(item);
					tilebread.removeDisplayItem();
				}
				if (bread != null)
					System.out.println("아이템 설정함" + bread.getEntityItem().getEntityItem().getDisplayName());
			}
			else
			{
				if(!tilebread.isSecondActive()) {
					WorldAPI.addMessage("구매하려면 한번 더 우클릭");
					tilebread.setSecondActive();
				}
				else {
					bread.subAmount(1);
				}
			}
			tilebread.sendUpdates();
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB_BOTTOM_HALF;
	}

}
