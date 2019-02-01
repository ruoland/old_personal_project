package cmplus.cm;

import cmplus.CMManager;
import cmplus.util.CommandPlusBase;
import cmplus.util.CommandTool;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandShader extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CommandTool t = new CommandTool(getCommandName());
		int id = parseInt(args[0], 0, 22);
		CMManager.shader(id);
		t.addSettingMessage(id);
	}
}
