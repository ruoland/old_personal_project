package ruo.cmplus.cm;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CommandStopsound extends CommandBase{

	@Override
	public String getCommandName() {
		
		return "stopsound";
	}
	@Override
	public int getRequiredPermissionLevel() {
	
		return 2;
	}
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		
		return "commandPlus.stopsound.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		p_71515_1_.addChatMessage((ITextComponent) new TextComponentString("commandPlus.stopsound.stop"));
	}

}
