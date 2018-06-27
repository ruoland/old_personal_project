package ruo.cmplus.cm;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.CMManager;
import ruo.cmplus.util.CommandPlusBase;
import ruo.cmplus.util.CommandTool;

public class CommandShader extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) throws CommandException {
		super.execute(server, p_71515_1_, p_71515_2_);
		CommandTool t = new CommandTool(getCommandName());
		if(Integer.valueOf(p_71515_2_[0]) >= 22){
			t.addErrorMessage();
			return;
		}
		CMManager.shader(Integer.valueOf(p_71515_2_[0]));
		t.addSettingMessage(Integer.valueOf(p_71515_2_[0]));
	}
}
