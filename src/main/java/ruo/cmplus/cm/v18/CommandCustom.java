package ruo.cmplus.cm.v18;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientCommandHandler;
import ruo.cmplus.CMPlus;
import ruo.cmplus.util.CommandPlusBase;

import java.io.Serializable;

public class CommandCustom extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.execute(server, sender, args);
		ClientCommandHandler.instance.registerCommand(new CustomCommand(args[0], args[1]));
		CMPlus.debConfig.get("customcommand", args[0], args[1]);
		CMPlus.debConfig.save();
	}
	
	public static class CustomCommand extends CommandPlusBase implements Serializable {

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			super.execute(server, sender, args);
			
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
