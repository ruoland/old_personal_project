package ruo.minigame.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.minigame.MiniGame;

public class CommandScroll extends CommandBase {

	@Override
	public String getCommandName() {
		
		return "scroll";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		EntityPlayer player = (EntityPlayer) sender;
		GameSettings s = Minecraft.getMinecraft().gameSettings;

		if(args[0].equals("x")){
			MiniGame.scroll.x = true;
			MiniGame.scroll.z = false;
			MiniGame.scroll.xR = false;
			MiniGame.scroll.zR = false;

			if(args.length > 1 && args[1].equals("r"))
				MiniGame.scroll.xR = true;
			
			MiniGame.scroll.start();
		}
		else if(args[0].equals("z")){
			MiniGame.scroll.z = true;
			MiniGame.scroll.x = false;
			MiniGame.scroll.xR = false;
			MiniGame.scroll.zR = false;
			if(args.length > 1 && args[1].equals("r"))
				MiniGame.scroll.zR = true;
			
			MiniGame.scroll.start();
		}
		else{
			if(args.length > 0)
				MiniGame.scroll.end(args[0]);
			else
				MiniGame.scroll.end();

		}
	}

}
