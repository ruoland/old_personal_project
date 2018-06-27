package ruo.cmplus.cm;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.util.CommandTool;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

public class CommandTimer extends CommandBase {

	@Override
	public String getCommandName() {
		
		return "timer";
	}
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		
		return "commandPlus.timer.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] args) {
		CommandTool t = new CommandTool(getCommandName());
		EntityPlayerMP p = WorldAPI.getPlayerMP();
		if(t.length(args)){
			return;
		}		
		int tick = Integer.parseInt(args[args.length-1]);
		final String command = t.getCommand(args, 0, args.length-1);
		TickRegister.register(new AbstractTick(tick * 20, false) {
			@Override
			public void run(Type type) {
				WorldAPI.command(command.toString());
			}
		});
		if(!WorldAPI.getPlayer().worldObj.getGameRules().getBoolean("sendCommandFeedback"))
			return;
		p_71515_1_.addChatMessage((ITextComponent) new TextComponentString(I18n.format("commandPlus.timer.start")));
	}
}
