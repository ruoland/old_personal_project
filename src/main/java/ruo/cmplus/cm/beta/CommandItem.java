package ruo.cmplus.cm.beta;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandItem extends CommandPlusBase{

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		super.execute(server, sender, args);
		if(t.argCheck(args[0], "name", "na")){
			if(sender instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) sender;
				player.getHeldItemMainhand().setStackDisplayName(args[1]);
				
			}
		}
	}
}
