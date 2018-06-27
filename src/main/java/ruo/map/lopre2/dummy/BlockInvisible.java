package ruo.map.lopre2.dummy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;

public class BlockInvisible extends Block {

    public BlockInvisible(){
        super(Material.IRON);
        this.setUnlocalizedName("asdfasdf");
        this.setRegistryName("LoopPre2", "asdfasdf");
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return !super.isOpaqueCube(state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}

