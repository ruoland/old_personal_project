package ruo.cmplus.cm.v17;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.effect.TextEffect;

public class CommandText extends CommandPlusBase{

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		String command = t.getCommand(args, args.length);
		TextEffect ef = TextEffect.getHelper();
		ef.addChat(Integer.valueOf(args[0]+"000"), command);
	}
}
