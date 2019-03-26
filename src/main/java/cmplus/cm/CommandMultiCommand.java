package cmplus.cm;

import cmplus.util.CommandPlusBase;
import oneline.api.WorldAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandMultiCommand extends CommandPlusBase {
	@Override
	public String getCommandName() {
		return "multi";
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		String[] commandList = WorldAPI.strBind(true, args).split("/");
		for(String command : commandList){
			if(command.equals(""))
				continue;
			WorldAPI.command(sender,"/"+command);
		}
	}

}
