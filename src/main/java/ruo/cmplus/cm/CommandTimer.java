package ruo.cmplus.cm;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.util.CommandPlusBase;
import ruo.cmplus.util.CommandTool;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

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
				WorldAPI.command(command);
			}
		});
		t.addLoMessage("start");
	}
}
