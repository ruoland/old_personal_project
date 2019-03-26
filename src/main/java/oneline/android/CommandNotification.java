package oneline.android;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandNotification extends CommandBase{

	@Override
	public String getCommandName() {
		
		return "noti";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Android android = new Android();
		if(android.isLogin){
			android.sendNotification(Integer.valueOf(args[0]), args[1], args[2], args[3]);
	        notifyCommandListener(sender, this, "asdf");

		}

	}

}
