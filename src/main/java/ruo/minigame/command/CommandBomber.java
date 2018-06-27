package ruo.minigame.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.minigame.MiniGame;

public class CommandBomber extends CommandBase{

	@Override
	public String getCommandName() {
		
		return "bomber";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(args[0].equals("start"))
			for(int i = 1; i < args.length;i++)
				MiniGame.bomber.start(args[i]);
		else 
			MiniGame.bomber.end();
	}

}
