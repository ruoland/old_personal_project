package ruo.cmplus.cm.v16;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.CMManager;
import ruo.cmplus.util.CommandPlusBase;
import ruo.cmplus.util.CommandTool;

import javax.annotation.Nullable;
import java.util.List;

public class CommandMouseCancel extends CommandPlusBase{

	@Override
	public String getCommandName() {
		return "mouse";
	}
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commandPlus.mouse.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CommandTool t = new CommandTool(getCommandName());
		if(t.length(args)){
			return;
		}
		CMManager.setMouse(Boolean.valueOf(args[0]));
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	if(args.length == 1){
    		 return getListOfStringsMatchingLastWord(args, "true", "false");
    	}
    	return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
