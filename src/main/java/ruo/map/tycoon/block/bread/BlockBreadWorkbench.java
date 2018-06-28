package ruo.map.tycoon.block.bread;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;

public class BlockBreadWorkbench extends BlockContainer{
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1D, 1.0D);

	public BlockBreadWorkbench() {
		super(Material.ROCK);
		this.setHardness(10);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBreadWorkbench();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		TileBreadWorkbench call = (TileBreadWorkbench) worldIn.getTileEntity(pos);
		if(!worldIn.isRemote && entityIn instanceof EntityEgg){
			System.out.println("계란을 맞음");
			call.addEggCount(1);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileBreadWorkbench call = (TileBreadWorkbench) worldIn.getTileEntity(pos);
		if(hand == EnumHand.MAIN_HAND && !worldIn.isRemote) {
			if(!call.isStart && heldItem.getItem() == Items.WHEAT) {
				call.itemSetting();
				call.sendUpdates();
			}
			if(call.isStart && call.getProgress() > 5){
				if(WorldAPI.equalsItem(heldItem, Items.MILK_BUCKET)) {
					call.addMilkCount(1);
					heldItem.stackSize --;
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.BUCKET));
				}
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
		if(!worldIn.isRemote) {
			TileBreadWorkbench call = (TileBreadWorkbench) worldIn.getTileEntity(pos);
			if (call.getProgress() < 5) {
				call.addProgress(1);
				call.sendUpdates();
			}
		}
	}

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB_BOTTOM_HALF;
    }


}
