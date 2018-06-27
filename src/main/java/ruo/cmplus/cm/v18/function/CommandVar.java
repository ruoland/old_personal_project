package ruo.cmplus.cm.v18.function;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandVar extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(t.length(args)){
			return;
		}		
		if(args[0].equals("boolean")){
			VAR.putBoolean(args[1], Boolean.valueOf(args[2]));
			t.addSettingMessage(args[1]);
			return;
		}
		if(args[0].equals("int") || args[0].equals("double")){
			VAR.putDouble(args[1], Double.valueOf(args[2]));
			t.addSettingMessage(args[1]);
			return;
		}
		if(args[0].equals("string") || args[0].equals("str")){
			VAR.putString(args[1], t.getCommand(args, 2, args.length));
			t.addSettingMessage(args[1]);
			return;
		}
		t.addErrorMessage();
	}

	
}
