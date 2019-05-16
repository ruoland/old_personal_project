package minigameLib.minigame.minerun;

import minigameLib.MiniGame;
import olib.api.WorldAPI;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockRendererDispatcherMineRun extends BlockRendererDispatcher {
    public BlockRendererDispatcherMineRun(BlockModelShapes p_i46577_1_, BlockColors p_i46577_2_) {
        super(p_i46577_1_, p_i46577_2_);
    }

    @Override
    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn) {
        if (MiniGame.minerun.isStart() && (state.getBlock() instanceof BlockLiquid) && pos.getZ() >= WorldAPI.getPlayer().posZ) {
            BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
            if (pos.getY() + 1 > WorldAPI.getPlayer().posY && pos.getY() < WorldAPI.getPlayer().posY + 5) {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                    World worldObj = WorldAPI.getPlayer().worldObj;
                    worldObj.markBlockRangeForRenderUpdate(pos, pos);
                    worldObj.notifyBlockUpdate(pos, state, state, 3);
                    if(worldObj.getTileEntity(pos) != null)
                    worldObj.scheduleBlockUpdate(pos, worldObj.getTileEntity(pos).getBlockType(), 0, 0);

                });
            }
            return false;
        }
        return super.renderBlock(state, pos, blockAccess, worldRendererIn);
    }
}
