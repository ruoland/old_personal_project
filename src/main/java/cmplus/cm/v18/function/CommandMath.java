package cmplus.cm.v18.function;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;

public class CommandMath extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(args[0].equals("abs")) {
			VAR.putDouble(args[1], (double) MathHelper.abs(Float.valueOf(args[2])));
		}
		if(args[0].equals("floor")) {
			VAR.putDouble(args[1], MathHelper.floor_double(Double.valueOf(args[2])));
		}
		if(args[0].equals("max")) {
			double arg1 = VAR.getDouble(args[2]);
			double arg2 = VAR.getDouble(args[3]);
			if(arg1 < arg2) {
				VAR.putDouble(args[1], arg1);
			}
			if(arg1 > arg2) {
				VAR.putDouble(args[1], arg2);
			}
		}
		if(args[0].equals("min")) {
			double arg1 = VAR.getDouble(args[2]);
			double arg2 = VAR.getDouble(args[3]);
			if(arg1 > arg2) {
				VAR.putDouble(args[1], arg1);
			}
			if(arg1 < arg2) {
				VAR.putDouble(args[1], arg2);
			}
		}
	}
}
