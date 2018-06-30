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
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent e){
		if(!MineRun.isStart())
			return;
		System.out.println();
		if(e.phase == Phase.END && MineRun.isStart()){
			double veloX = 0, veloZ = 0;
			String index = e.player.getHorizontalFacing().getName();
			if (index.equalsIgnoreCase("NORTH")) {
				veloZ = -0.3;
			}
			if (index.equalsIgnoreCase("SOUTH")) {
				veloZ = 0.3;
			}
			if (index.equalsIgnoreCase("WEST")) {
				veloX = -0.3;
			}
			if (index.equalsIgnoreCase("EAST")) {
				veloX = 0.3;
			}
			e.player.motionX = veloX;
			e.player.motionZ = veloZ;
		}
	}
	@SubscribeEvent
	public void keyInput(KeyInputEvent e){
		if(!MineRun.isStart())
			return;
		double veloX = 0, veloZ = 0;
		EntityPlayerMP player = WorldAPI.getPlayerMP();
		String index = player.getHorizontalFacing().getName();
		if(Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()){
			veloX = EntityAPI.getFacingX(player.rotationYaw - 90) * 5;
			veloZ = EntityAPI.getFacingZ(player.rotationYaw - 90) * 5;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()){
			veloX = EntityAPI.getFacingX(player.rotationYaw + 90) * 5;
			veloZ = EntityAPI.getFacingZ(player.rotationYaw + 90) * 5;
		}
		WorldAPI.teleport(player.posX + veloX, player.posY, player.posZ + veloZ);
	}
	
	@SubscribeEvent
	public void logout(WorldEvent.Unload e){
		MiniGame.minerun.end();
	}
	public void run(){
	
		//player.connection.setPlayerLocation(WorldAPI.floor(posX)+0.5, posY, WorldAPI.floor(posZ)+0.5, player.rotationYaw, player.rotationPitch);
	}
	public boolean checkBlock(double posX, double posY, double posZ){
		IBlockState bib = WorldAPI.getPlayer().worldObj.getBlockState(new BlockPos(posX, posY, posZ));
		
		if(bib.getBlock() == Blocks.AIR)
			return true;
		
		if(bib.getBlock().getUnlocalizedName().equals("tile.ladder"))
			return true;
		
		return false;
	}
}
