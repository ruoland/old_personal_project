package ruo.minigame.minigame.minerun;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import ruo.map.lopre2.EntityPreBlock;

public class BlockInvisible extends Block {
    protected static final AxisAlignedBB AABB_NULL = new AxisAlignedBB(0,0,0,0,0.1,0);

    public BlockInvisible(Material materialIn) {
        super(materialIn);
    }
}
