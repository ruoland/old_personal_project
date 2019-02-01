package cmplus.cm.v18;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandFire extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		EntityPlayer player = (EntityPlayer) sender;
		player.setFire(Integer.valueOf(args[0]));
	}
	
}
