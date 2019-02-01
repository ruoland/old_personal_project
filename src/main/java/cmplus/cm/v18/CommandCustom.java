package cmplus.cm.v18;

import cmplus.CMPlus;
import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientCommandHandler;

import java.io.Serializable;

public class CommandCustom extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		ClientCommandHandler.instance.registerCommand(new CustomCommand(args[0], args[1]));
		CMPlus.cmPlusConfig.get("customcommand", args[0], args[1]);
		CMPlus.cmPlusConfig.save();
	}
	
	public static class CustomCommand extends CommandPlusBase implements Serializable {

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			
			
		}
		private String name, command;

		public CustomCommand(String name, String command) {
			this.name = name;
			this.command = command;
		}

		public String getName() {
			return name;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
