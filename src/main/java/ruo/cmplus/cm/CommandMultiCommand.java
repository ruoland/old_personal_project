package ruo.cmplus.cm;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

public class CommandMultiCommand extends CommandPlusBase {
	@Override
	public String getCommandName() {
		return "multi";
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		
		StringBuffer multi = new StringBuffer();
		for(String s : p_71515_2_){
			multi.append(s+" ");
		}

		String[] command = multi.toString().split("/");
		for(String comm : command){
			if(comm.equals(""))
				continue;
			WorldAPI.command(p_71515_1_,"/"+comm);
		}
	}

}
