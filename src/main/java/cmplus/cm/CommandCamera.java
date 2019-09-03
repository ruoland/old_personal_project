package cmplus.cm;

import cmplus.CMManager;
import cmplus.CMPlusCameraEvent;
import cmplus.camera.Camera;
import cmplus.util.CommandPlusBase;
import cmplus.util.CommandTool;
import olib.api.WorldAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCamera extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		
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
		if(t.argCheck(args[0], "pos")){
			CMPlusCameraEvent.x = sender.getPositionVector().xCoord;
			CMPlusCameraEvent.y = sender.getPositionVector().yCoord;
			CMPlusCameraEvent.z = sender.getPositionVector().zCoord;
		}
		if(t.argCheck(args[0], "reset", "리셋")){
			Camera.reset();
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
			Camera.setYP(t.parseBoolean(args[1]));
			t.addLoMessage("yp",t.parseBoolean(args[1]));
			return;
		}
		if(t.argCheck(args[0], "yaw", "요")){
			float yaw;
			if(Camera.isLock()){
				yaw = (float) t.math(args[1], Camera.lockPlayerYaw, args[2]);
				Camera.lockPlayerYaw = yaw;
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
			if(Camera.isLock()){
				pitch = (float) t.math(args[1], Camera.lockPlayerPitch, args[2]);
				Camera.lockPlayerPitch = pitch;
			}
			else{
				 pitch = (float)t.math(args[1], p.rotationPitch, args[2]);
				 p.connection.setPlayerLocation(p.posX, p.posY, p.posZ, p.rotationYaw, (float) pitch);
			}
			t.addLoMessage("pitch",pitch);
			return;

		}
		if(t.argCheck(args[0], "zoom", "줌")){
			Camera.setZoom(t.math(args[1],Camera.getZoom(),(args[2])));
			t.addLoMessage("zoom",t.math(args[1],Camera.getZoom(),(args[2])));
			return;
		}
		if(t.argCheck(args[0], "rotate", "회전")){
			double x,y,z;
			x = t.math(args[1], Camera.rotateX, args[2]);
			y = t.math(args[1], Camera.rotateY, args[3]);
			z = t.math(args[1], Camera.rotateZ, args[4]);
			Camera.rotateCamera(x,y,z);
			return;
		}
		if(t.argCheck(args[0], "move", "이동")){
			double x,y,z;
			x = t.math(args[1], Camera.traX, args[2]);
			y = t.math(args[1], Camera.traY, args[3]);
			z = t.math(args[1], Camera.traZ, args[4]);
			Camera.moveCamera(x,y,z);
			return;

		}
		if(t.argCheck(args[0], "player", "플레이어")){
			Camera.playerCamera(t.parseBoolean(args[1]));
			return;
		}
		if(t.argCheck(args[0], "lock", "잠금")){
			Camera.lockCamera(t.parseBoolean(args[1]), p.prevRotationYaw, p.prevRotationPitch);
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
