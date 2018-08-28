package ruo.minigame.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.minigame.MiniGame;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class CommandMineRun extends CommandBase {

	@Override
	public String getCommandName() {
		return "minerun";

	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0){
			if(args[0].equals("fake")){
				FakePlayerHelper.spawnFakePlayer(false);
				return;
			}
			if(args[0].equals("dead")){
				FakePlayerHelper.setFakeDead();
				return;
			}
		}
		if(args.length > 0 && (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("stop")))
			MiniGame.minerun.end();
		else
			MiniGame.minerun.start(sender, args);
	}

}
