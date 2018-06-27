package ruo.cmplus.cm.v18.function;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

public class CommandIF extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.execute(server, sender, args);
		if(t.length(args)){
			return;
		}
		if (args[0].equals("off")){
			FunctionIF.removeIF();
			return;
		}
		if(args.length == 1)
			FunctionIF.addIF(args[0]);
		if(args.length > 2) {
			FunctionIF funcIF = FunctionIF.create(args[0], args[1], args[2]);
			WorldAPI.command(t.getCommand(args, 3, args.length));
		}
		FunctionIF.addIF(args[0], args[1], args[2]);
	}
}
