package cmplus.cm.v18;

import cmplus.util.CommandPlusBase;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CommandOpenFolder extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		try {
			Desktop.getDesktop().open(new File(Minecraft.getMinecraft().mcDataDir+"/"+args[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
