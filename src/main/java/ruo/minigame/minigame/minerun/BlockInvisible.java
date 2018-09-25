package ruo.minigame.minigame.minerun;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockInvisible extends Block {

    public BlockInvisible(Material materialIn) {
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
        return Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox() ? super.getRenderType(state) : EnumBlockRenderType.INVISIBLE;
    }
}
