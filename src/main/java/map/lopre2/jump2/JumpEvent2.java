package map.lopre2.jump2;

import minigameLib.action.ActionEffect;
import minigameLib.api.WorldAPI;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import map.lopre2.CommandJB;
import map.lopre2.ItemSpanner;
import map.lopre2.LoPre2;


public class JumpEvent2 {

    @SubscribeEvent
    public void chatMessage(ClientChatReceivedEvent e) {
        if (LoPre2.checkWorld()) {
            if (e.getType() == 1) {
                if (e.getMessage().getUnformattedComponentText().indexOf("의 리스폰 지점을") != -1) {
                    e.setCanceled(true);

                }
            }
        }
    }

    @SubscribeEvent
    public void playerTick(CommandEvent e) {
        if (LoPre2.checkWorld()) {
            if (e.getCommand().getCommandName().equalsIgnoreCase("spawnpoint")) {
                WorldAPI.command("/heal");
            }
        }
    }

    static BlockPos pos = new BlockPos(1093.5, 224.0, -68.5);
    static BlockPos pos2 = new BlockPos(1093.5, 225.0, -68.5);
    static BlockPos pos3 = new BlockPos(1091.5, 224.0, -68.5);
    static BlockPos pos4 = new BlockPos(1091.5, 225.0, -68.5);
    static BlockPos pos5 = new BlockPos(1091.5, 223.0, -69.5);
    static BlockPos pos6 = new BlockPos(1093.5, 223.0, -69.5);
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e) {
        if (WorldAPI.getCurrentWorldName().equalsIgnoreCase("JumpMap Sea2")) {
            e.player.worldObj.setBlockState(pos, Blocks.AIR.getDefaultState());
            e.player.worldObj.setBlockState(pos2, Blocks.AIR.getDefaultState());
            e.player.worldObj.setBlockState(pos3, Blocks.AIR.getDefaultState());
            e.player.worldObj.setBlockState(pos4, Blocks.AIR.getDefaultState());
            e.player.worldObj.setBlockState(pos5, Blocks.STONE.getDefaultState());
            e.player.worldObj.setBlockState(pos6, Blocks.STONE.getDefaultState());
            if (e.player.getBedLocation() != null) {
                if(e.player.getBedLocation().getY() - 20 > 0 && ActionEffect.canYTP("JumpMap Sea2")) {
                    ActionEffect.setYTP(e.player.getBedLocation().getY() - 20, ActionEffect.getPitch(), ActionEffect.getYaw());
                }
            }
        }
    }

}
