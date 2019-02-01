package map.lopre2;

import cmplus.cm.v18.CommandFly;
import minigameLib.action.GrabHelper;
import minigameLib.api.BlockAPI;
import minigameLib.api.WorldAPI;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;


public class LooPreClientEvent {
    @SubscribeEvent
    public void client(TickEvent.ClientTickEvent event){
        if(LoPre2.checkWorld() && Minecraft.getMinecraft().currentScreen == null && Keyboard.isKeyDown(Keyboard.KEY_R) && WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getBedLocation() != null){
            BlockPos bedLocation = WorldAPI.getPlayerMP().getBedLocation();
            WorldAPI.teleport(bedLocation.getX()+0.5, bedLocation.getY(), bedLocation.getZ()+0.5);
            WorldAPI.getPlayerMP().heal(20);
            WorldAPI.getPlayer().fallDistance = 0;
            WorldAPI.getPlayer().getFoodStats().setFoodLevel(20);
            if(WorldAPI.getBlock(bedLocation.add(0,-1,0)) == Blocks.AIR){
                BlockAPI blockAPI = WorldAPI.getBlock(WorldAPI.getWorld(), bedLocation, 5);
                for(BlockPos pos : blockAPI.getPosList()){
                    if(WorldAPI.getBlock(pos) instanceof BlockBasePressurePlate){
                        WorldAPI.teleport(pos.add(0,1,0));
                    }
                }
            }

        }
    }
}
