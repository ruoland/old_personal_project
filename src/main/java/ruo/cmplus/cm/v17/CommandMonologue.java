package ruo.cmplus.cm.v17;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TextEffect;
import ruo.minigame.effect.TickRegister;

public class CommandMonologue extends CommandPlusBase{

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
