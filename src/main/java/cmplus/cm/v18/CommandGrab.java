package cmplus.cm.v18;

import cmplus.util.CommandPlusBase;
import cmplus.util.MouseHelper10;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CommandGrab extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		Minecraft.getMinecraft().mouseHelper = new MouseHelper10();
		
		if(t.argCheck(args[0], "그랩", "grab", "true")){
			Minecraft.getMinecraft().setIngameFocus();
		}
		if(t.argCheck(args[0], "언그랩", "ungrab", "false"))
			Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
	}
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		
		return getListOfStringsMatchingLastWord(args,"grab", "ungrab");
	}
}
