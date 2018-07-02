package ruo.cmplus.cm;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.CMManager;
import ruo.cmplus.camera.Camera;
import ruo.cmplus.util.CommandPlusBase;
import ruo.cmplus.util.CommandTool;
import ruo.minigame.api.WorldAPI;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCamera extends CommandPlusBase{

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] args) {
		
		CommandTool t = new CommandTool(getCommandName());
		EntityPlayerMP p = WorldAPI.getPlayerMP();
		if(t.length(args)){
			t.addLoMessage("help.reset");
			t.addLoMessage("help.yaw");
			t.addLoMessage("help.pitch");
			t.addLoMessage("help.yp");
			t.addLoMessage("help.zoom");
			t.addLoMessage("help.rotate");
			t.addLoMessage("help.move");
			t.addLoMessage("help.player");
			t.addLoMessage("help.lock");
			return;
		}
		Camera renderer = Camera.getCamera();
		if(t.argCheck(args[0], "reset", "리셋")){
			Camera.getCamera().reset();
			t.addLoMessage("reset");
			return;
		}
		if(t.argCheck(args[0], "deb")){
			Camera.isDebug = t.parseBoolean(args[1]);
			return;
		}
		if(t.argCheck(args[0], "freecam")){
			Camera.isDebug = t.parseBoolean(args[1]);
			Camera.freeCam = t.parseBoolean(args[1]);
			CMManager.setMoveLock(t.parseBoolean(args[1]));
			return;
		}
		if(t.argCheck(args[0], "yp")){
			Camera.getCamera().setYP(t.parseBoolean(args[1]));
			t.addLoMessage("yp",t.parseBoolean(args[1]));
			return;
		}
		if(t.argCheck(args[0], "yaw", "요")){
			float yaw;
			if(Camera.getCamera().isLock()){
				yaw = (float) t.math(args[1], Camera.getCamera().yaw, args[2]);
				renderer.yaw = yaw;
			}
			else{
				yaw = (float)t.math(args[1], p.rotationYaw, args[2]);
				p.connection.setPlayerLocation(p.posX, p.posY, p.posZ, (float) yaw, p.rotationPitch);
			}
			t.addLoMessage("yaw",yaw);
			return;
		}
		if(t.argCheck(args[0], "pitch", "피치")){
			float pitch;
			if(Camera.getCamera().isLock()){
				pitch = (float) t.math(args[1], renderer.pitch, args[2]);
				renderer.pitch = pitch;
			}
			else{
				 pitch = (float)t.math(args[1], p.rotationPitch, args[2]);
				 p.connection.setPlayerLocation(p.posX, p.posY, p.posZ, p.rotationYaw, (float) pitch);
			}
			t.addLoMessage("pitch",pitch);
			return;

		}
		if(t.argCheck(args[0], "zoom", "줌")){
			Camera.getCamera().setZoom(t.math(args[1],Camera.getCamera().getZoom(),(args[2])));
			t.addLoMessage("zoom",t.math(args[1],Camera.getCamera().getZoom(),(args[2])));
			return;
		}
		if(t.argCheck(args[0], "rotate", "회전")){
			double x,y,z;
			x = t.math(args[1], renderer.rotateX, args[2]);
			y = t.math(args[1], renderer.rotateY, args[3]);
			z = t.math(args[1], renderer.rotateZ, args[4]);
			Camera.getCamera().rotateCamera(x,y,z);
			return;
		}
		if(t.argCheck(args[0], "move", "이동")){
			double x,y,z;
			x = t.math(args[1], renderer.traX, args[2]);
			y = t.math(args[1], renderer.traY, args[3]);
			z = t.math(args[1], renderer.traZ, args[4]);
			Camera.getCamera().moveCamera(x,y,z);
			return;

		}
		if(t.argCheck(args[0], "player", "플레이어")){
			Camera.getCamera().playerCamera(t.parseBoolean(args[1]));
			return;
		}
		if(t.argCheck(args[0], "lock", "잠금")){
			Camera.getCamera().lockCamera(t.parseBoolean(args[1]), p.prevRotationYaw, p.prevRotationPitch);
			return;
		}
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	if(args.length == 1){
        	return getListOfStringsMatchingLastWord(args,"zoom", "rotate", "move", "lock", "yp", "reset", "yaw", "pitch");
    	}
    	if(args.length == 0) {
			t.addLoMessage("help");
			return super.getTabCompletionOptions(server, sender, args, pos);
		}
    	return super.getTabCompletionOptions(server, sender, args, pos);
    	// "줌", "회전", "이동", "잠금", "플레이어", "리셋", "요", "피치", 
    	
    }
}
