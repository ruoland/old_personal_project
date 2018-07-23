package ruo.asdfrpg.camp;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
import ruo.minigame.api.EntityAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCampFire extends BlockContainer{
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

	public BlockCampFire() {
		super(Material.ROCK);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		List<EntityItem> entityList = EntityAPI.getEntity(worldIn, new AxisAlignedBB(pos.getX(), pos.getY()-1, pos.getZ(), pos.getX(), pos.getY()+2, pos.getZ()), EntityItem.class);
		for(EntityItem item : entityList){
			ItemStack stack = item.getEntityItem();
			int metadata = stack.getMetadata();
			int amount = stack.stackSize;
			if(stack.getItem() == Items.BEEF){
				item.setEntityItemStack(new ItemStack(Items.COOKED_BEEF, amount, metadata));
			}
			if(stack.getItem() == Items.CHICKEN){
				item.setEntityItemStack(new ItemStack(Items.COOKED_CHICKEN, amount, metadata));
			}
			if(stack.getItem() == Items.POTATO){
				item.setEntityItemStack(new ItemStack(Items.BAKED_POTATO, amount, metadata));
			}
			if(stack.getItem() == Items.FISH){
				item.setEntityItemStack(new ItemStack(Items.COOKED_FISH, amount, metadata));
			}
			if(stack.getItem() == Items.MUTTON){
				item.setEntityItemStack(new ItemStack(Items.COOKED_MUTTON, amount, metadata));
			}
			if(stack.getItem() == Items.RABBIT){
				item.setEntityItemStack(new ItemStack(Items.COOKED_RABBIT, amount, metadata));
			}
			if(stack.getItem() == Items.MUTTON){
				item.setEntityItemStack(new ItemStack(Items.COOKED_MUTTON, amount, metadata));
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCampFire();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB_BOTTOM_HALF;
    }


}
