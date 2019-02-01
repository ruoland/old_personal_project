package cmplus.cm.beta;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandItem extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		if(t.argCheck(args[0], "name", "na")){
			if(sender instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) sender;
				player.getHeldItemMainhand().setStackDisplayName(args[1]);
				
			}
		}
	}
}
