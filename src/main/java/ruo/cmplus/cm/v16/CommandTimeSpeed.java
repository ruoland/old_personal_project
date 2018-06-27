package ruo.cmplus.cm.v16;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

public class CommandTimeSpeed extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (t.length(args)) {
			return;
		}
		WorldAPI.worldtime(Float.parseFloat(args[0]));
	}

}
