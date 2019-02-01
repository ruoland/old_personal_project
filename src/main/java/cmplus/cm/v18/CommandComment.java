package cmplus.cm.v18;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandComment extends CommandPlusBase {

	@Override
	public String getCommandName() {
		return "-";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
	}
}
