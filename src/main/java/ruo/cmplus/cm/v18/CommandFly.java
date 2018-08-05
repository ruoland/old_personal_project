package ruo.cmplus.cm.v18;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandFly extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		EntityPlayerMP mp = (EntityPlayerMP) sender;
		mp.capabilities.isFlying = Boolean.valueOf(args[0]);
		mp.sendPlayerAbilities();
	}
}
