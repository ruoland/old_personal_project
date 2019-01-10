package ruo.minigame.minigame.minerun;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

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
                    MineRun.removeLavaPos.add(blockPos);
                    MineRun.removeLavaState.add(WorldAPI.getWorld().getBlockState(blockPos));
                    WorldAPI.getWorld().setBlockToAir(blockPos);
                });
            }
            return false;
        }
        return super.renderBlock(state, pos, blockAccess, worldRendererIn);
    }
}
