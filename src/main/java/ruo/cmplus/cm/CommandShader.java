package ruo.cmplus.cm;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.CMManager;
import ruo.cmplus.util.CommandPlusBase;
import ruo.cmplus.util.CommandTool;

public class CommandShader extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CommandTool t = new CommandTool(getCommandName());
		int id = parseInt(args[0], 0, 22);
		CMManager.shader(id);
		t.addSettingMessage(id);
	}
}
