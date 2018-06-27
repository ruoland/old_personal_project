package ruo.cmplus.cm;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandTool;
import ruo.minigame.api.WorldAPI;

public class CommandTexture extends CommandBase{

	@Override
	public String getCommandName() {
		
		return "texture";
	}
	@Override
	public int getRequiredPermissionLevel() {
	
		return 2;
	}
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		
		return "commandPlus.texture.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		
		CommandTool t = new CommandTool(getCommandName());
		EntityPlayerMP p = WorldAPI.getPlayerMP();
		if(t.length(p_71515_2_)){
			return;
		}		
	}

	/*
	 * 
		for(DrawTexture d : CMManager.list){
			if(p_71515_2_[0].equals(d.texture.split("/")[d.texture.split("/").length-1])){
				if(p_71515_2_[1].equals("rotate")){
					float x = t.math(p_71515_2_[2], d.roX, Float.valueOf(p_71515_2_[3]));
					float y = t.math(p_71515_2_[2], d.roY, Float.valueOf(p_71515_2_[4]));
					float z = t.math(p_71515_2_[2], d.roZ, Float.valueOf(p_71515_2_[5]));

					d.rotate(x, y, z);
				}
				if(p_71515_2_[1].equals("move")){
					float x = t.math(p_71515_2_[2], d.traX, Float.valueOf(p_71515_2_[3]));
					float y = t.math(p_71515_2_[2], d.traY, Float.valueOf(p_71515_2_[4]));
					float z = t.math(p_71515_2_[2], d.traZ, Float.valueOf(p_71515_2_[5]));
					d.move(x, y, z);
				}
				if(p_71515_2_[1].equals("trans")){
					float x = t.math(p_71515_2_[2], d.alpha, Float.valueOf(p_71515_2_[3]));
					System.out.println("x"+x);
					d.alpha(x);
				}
				if(p_71515_2_[1].equals("tick")){
					float x = t.math(p_71515_2_[2], d.tick, Float.valueOf(p_71515_2_[3]));
					d.tick = (int) x;
				}
			}
		}
	 */
}
