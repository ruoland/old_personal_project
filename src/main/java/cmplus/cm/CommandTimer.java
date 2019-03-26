package cmplus.cm;

import cmplus.util.CommandPlusBase;
import cmplus.util.CommandTool;
import oneline.api.WorldAPI;
import oneline.effect.AbstractTick;
import oneline.effect.TickRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class CommandTimer extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		CommandTool t = new CommandTool(getCommandName());
		if(t.length(args)){
			return;
		}		
		int tick = Integer.parseInt(args[args.length-1]);
		final String command = t.getCommand(args, 0, args.length-1);
		TickRegister.register(new AbstractTick(tick * 20, false) {
			@Override
			public void run(Type type) {
				WorldAPI.command(sender, command);
			}
		});

		t.addLoMessage("start");
	}
}
