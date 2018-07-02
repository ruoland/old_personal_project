package ruo.minigame.minigame.minerun;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

public class MineRunEvent {
    public int lineLR = 0, lineFB = 0;
    public double lineX, lineZ, lineFBX, lineFBZ;

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        if (e.phase == Phase.END && MiniGame.minerun.isStart()) {
            if(MineRun.isElytraMode()){
                e.player.motionY = 0.2;
            }
            else {
                e.player.motionX = EntityAPI.lookX(e.player, 0.3);
                e.player.motionZ = EntityAPI.lookZ(e.player, 0.3);
            }
        }
    }

    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        EntityPlayerMP player = WorldAPI.getPlayerMP();
        if(DebAPI.isKeyDown(Keyboard.KEY_V)){
            MineRun.setElytraMode(!MineRun.isElytraMode());
        }
        if (MineRun.isElytraMode()) {
            if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX + lineFBX, player.posY, player.posZ + lineFBZ);
                lineFB++;
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX - lineFBX, player.posY, player.posZ - lineFBZ);
                lineFB--;
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX + lineX, player.posY, player.posZ + lineZ);
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX - lineX, player.posY, player.posZ - lineZ);
                lineLR--;
            }
        } else {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX +  (lineX * 2), player.posY, player.posZ +  (lineZ * 2));
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX -  (lineX * 2), player.posY, player.posZ -  (lineZ * 2));
                lineLR--;
            }
            System.out.print(lineLR);
        }
    }

    @SubscribeEvent
    public void logout(WorldEvent.Unload e) {
        MiniGame.minerun.end();
    }
}
