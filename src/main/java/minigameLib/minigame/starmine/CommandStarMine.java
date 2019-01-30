package minigameLib.minigame.starmine;

import minigameLib.MiniGame;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandStarMine extends CommandBase{

	@Override
	public String getCommandName() {
		return "starmine";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender;

		if(args[0].equals("start"))
			MiniGame.starMine.start();
		else
			MiniGame.starMine.end();
		
	}

}
