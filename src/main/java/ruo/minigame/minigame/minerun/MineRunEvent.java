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
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

public class MineRunEvent {
	private int line = 0;
	public double lineX, lineZ;

	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent e){
		if(!MiniGame.minerun.isStart())
			return;
		if(e.phase == Phase.END && MiniGame.minerun.isStart()){
			e.player.motionX = EntityAPI.lookX(e.player, 0.3);
			e.player.motionZ = EntityAPI.lookZ(e.player, 0.3);
		}
	}
	@SubscribeEvent
	public void keyInput(KeyInputEvent e){
		if(!MiniGame.minerun.isStart())
			return;
		EntityPlayerMP player = WorldAPI.getPlayerMP();
		if(line < 1 && Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()){
			WorldAPI.teleport(player.posX + lineX+line, player.posY, player.posZ + lineZ+line);
			line++;
		}
		if(line > -1 &&Keyboard.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()){
			WorldAPI.teleport(player.posX - (lineX+line), player.posY, player.posZ - (lineZ+line));
			line--;
		}
	}
	
	@SubscribeEvent
	public void logout(WorldEvent.Unload e){
		MiniGame.minerun.end();
	}
}
