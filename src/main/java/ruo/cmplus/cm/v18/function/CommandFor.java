package ruo.cmplus.cm.v18.function;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandFor extends CommandPlusBase{

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.execute(server, sender, args);
		if(t.length(args)){
			return;
		}
		if(args[0].equals("off")){
			FunctionFor.removeFor();
			return;
		}
		final String command = t.getCommand(args, 0, args.length-3);
		int tick = parseInt(args[args.length-2]) * 20;
		FunctionFor funcfor = FunctionFor.addFor(tick, command, Integer.valueOf(args[args.length-1]));
	}

}
