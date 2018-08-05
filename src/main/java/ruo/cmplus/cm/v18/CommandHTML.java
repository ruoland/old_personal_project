package ruo.cmplus.cm.v18;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandHTML extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		HTMLFX fx = new HTMLFX(args[0]);
		fx.launch();
	}
	
	
}
