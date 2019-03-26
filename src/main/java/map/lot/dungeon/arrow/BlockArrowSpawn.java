package map.lot.dungeon.arrow;

import oneline.effect.AbstractTick;
import oneline.effect.TickRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class BlockArrowSpawn extends Block{
	public BlockArrowSpawn(Material materialIn) {
		super(materialIn);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		TickRegister.register(new AbstractTick(pos.toString(), Type.SERVER, 40, true) {
			
			@Override
			public void run(Type type) {
				EntityTippedArrow arrow =  new EntityTippedArrow(world, pos.getX(), pos.getY(), pos.getZ());
				arrow.setPosition(pos.getX()+0.5, pos.getY()-1, pos.getZ()+0.5);
				world.spawnEntityInWorld(arrow);
			}
		});
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack);
	}
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		if(TickRegister.getAbsTick(pos.toString()) != null)
		TickRegister.remove(pos.toString());
	}
}
