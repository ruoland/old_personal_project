package ruo.minigame.minigame.minerun;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.minigame.AbstractMiniGame;

public class MineRun extends AbstractMiniGame {
	public MineRun() {
		this.event = new MineRunEvent();
	}
	
	@Override
	public boolean start(Object... obj){
		String type = String.valueOf(obj[0]);
		GameSettings gs = Minecraft.getMinecraft().gameSettings;
		gs.keyBindLeft.setKeyCode(Keyboard.KEY_SLEEP);
		gs.keyBindRight.setKeyCode(Keyboard.KEY_SLEEP);
		gs.keyBindUseItem.resetKeyBindingArrayAndHash();
		ICommandSender sender = (ICommandSender) obj[0];
		EntityPlayer player = (EntityPlayer) sender;
		double yaw =  player.getHorizontalFacing().getHorizontalAngle();
		Camera.getCamera().reset();
		Camera.getCamera().lockCamera(true);
		Camera.getCamera().playerCamera(true);
		Camera.getCamera().lockCamera(true, (float) yaw, 0);
		Camera.getCamera().moveCamera(0, -1.9, 5);
		String index = player.getHorizontalFacing().getName();
		if(yaw < 0)
			Camera.getCamera().rotateCamera(0, yaw - 90, 0);
		else
			Camera.getCamera().rotateCamera(0, yaw + 90, 0);
		((MineRunEvent) event).lineX = EntityAPI.getFacingX(player.rotationYaw - 90) * 3;
		((MineRunEvent) event).lineZ = EntityAPI.getFacingZ(player.rotationYaw - 90) * 3;
		return super.start();
	}
	
	@Override
	public boolean end(Object... obj){
		GameSettings gs = Minecraft.getMinecraft().gameSettings;
		gs.keyBindLeft.setKeyCode(Keyboard.KEY_A);
		gs.keyBindRight.setKeyCode(Keyboard.KEY_D);
		gs.keyBindUseItem.resetKeyBindingArrayAndHash();
		Camera.getCamera().reset();
		return super.end();
	}
}
