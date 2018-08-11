package ruo.cmplus.cm;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ruo.cmplus.util.CommandPlusBase;

public class CommandStopsound extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		sender.addChatMessage(new TextComponentString("commandPlus.stopsound.stop"));
	}

}
