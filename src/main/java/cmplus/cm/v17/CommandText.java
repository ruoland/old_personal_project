package cmplus.cm.v17;

import cmplus.util.CommandPlusBase;
import olib.effect.TextEffect;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandText extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		String command = t.getCommand(args, args.length);
		TextEffect ef = TextEffect.getHelper();
		ef.addChat(Integer.valueOf(args[0]+"000"), command);
	}
}
