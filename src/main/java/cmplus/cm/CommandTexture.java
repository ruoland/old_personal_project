package cmplus.cm;

import cmplus.util.CommandPlusBase;
import cmplus.util.CommandTool;
import minigameLib.api.WorldAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandTexture extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		
		CommandTool t = new CommandTool(getCommandName());
		EntityPlayerMP p = WorldAPI.getPlayerMP();
		if(t.length(args)){
			return;
		}		
	}

	/*
	 * 
		for(DrawTexture d : CMManager.list){
			if(args[0].equals(d.texture.split("/")[d.texture.split("/").length-1])){
				if(args[1].equals("rotate")){
					float x = t.math(args[2], d.roX, Float.valueOf(args[3]));
					float y = t.math(args[2], d.roY, Float.valueOf(args[4]));
					float z = t.math(args[2], d.roZ, Float.valueOf(args[5]));

					d.rotate(x, y, z);
				}
				if(args[1].equals("move")){
					float x = t.math(args[2], d.traX, Float.valueOf(args[3]));
					float y = t.math(args[2], d.traY, Float.valueOf(args[4]));
					float z = t.math(args[2], d.traZ, Float.valueOf(args[5]));
					d.move(x, y, z);
				}
				if(args[1].equals("trans")){
					float x = t.math(args[2], d.alpha, Float.valueOf(args[3]));
					System.out.println("x"+x);
					d.alpha(x);
				}
				if(args[1].equals("tick")){
					float x = t.math(args[2], d.tick, Float.valueOf(args[3]));
					d.tick = (int) x;
				}
			}
		}
	 */
}
