package cmplus.cm.v18.function;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandWhile extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, final String[] args) throws CommandException {
		
		if(t.length(args)){
			return;
		}
		if(args[0].equals("off")){
			FunctionWhile.removeFor();
			return;
		}
		final String command = t.getCommand(args, 3, args.length-1);
		int tick = parseInt(args[args.length - 1]);
		FunctionIF fif = FunctionIF.create(args[0], args[1], args[2]);
		FunctionWhile.addFor(fif, tick, command);
	}
	
}
