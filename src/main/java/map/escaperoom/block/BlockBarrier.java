package map.escaperoom.block;

import map.lopre2.EntityPreBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockBarrier extends Block {
    public BlockBarrier(Material materialIn) {
        super(materialIn);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox() ? super.getCollisionBoundingBox(blockState, worldIn, pos) : NULL_AABB;
    }
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isCollidable() {
        return Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox() ? EnumBlockRenderType.MODEL : EnumBlockRenderType.INVISIBLE;
    }


    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }
}
