package ruo.minigame.minigame.elytra_scroll;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class CommandElytraScroll extends CommandBase{

	@Override
	public String getCommandName() {
		return "elytrascroll";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender;

		if(args[0].equals("start"))
			MiniGame.elytraScroll.start();
		else
			MiniGame.elytraScroll.end();
		
	}

}
