package minigameLib.minigame.minerun.invisibleblock;

import map.tycoon.block.BlockBreadDisplay;
import map.tycoon.block.TileBreadDisplay;
import minigameLib.api.WorldAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockInvisible extends BlockContainer {

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
        return Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox() ? EnumBlockRenderType.MODEL : EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileInvisible();
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        TileInvisible tileInvisible = (TileInvisible) worldIn.getTileEntity(pos);
        if(tileInvisible.getRunDelay() > 0){
            tileInvisible.setRunDelay(tileInvisible.getRunDelay() - 1);
            System.out.println(tileInvisible.getRunDelay());
        }

    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        TileInvisible tileInvisible = (TileInvisible) worldIn.getTileEntity(pos);
        if(entityIn instanceof EntityPlayer && tileInvisible.getRunDelay() == 0 && tileInvisible.getRunCount() < tileInvisible.getMaxCount()){
            tileInvisible.setRunDelay(tileInvisible.getDefaultDelay());
            tileInvisible.setRunCount(tileInvisible.getRunDelay()+1);
            WorldAPI.command(tileInvisible.getCommand());
        }
    }
}
