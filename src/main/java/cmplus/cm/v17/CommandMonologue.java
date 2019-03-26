package cmplus.cm.v17;

import cmplus.util.CommandPlusBase;
import oneline.effect.AbstractTick;
import oneline.effect.TextEffect;
import oneline.effect.TickRegister;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class CommandMonologue extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(t.length(args)) {
			return;
		}
		String text = args[0].replace("/n/", " ");
		TickRegister.register(new AbstractTick(Type.CLIENT, 1, false) {
			@Override
			public void run(Type type) {
				boolean dark = t.findBoolean(args, 1, false);
				TextEffect.monologue(text).setDark(dark).setPause(true).start();;
			}
		});
		//IngameEffect.instance().text(args[0], text.toString(), s);
	}

}
