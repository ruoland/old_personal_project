package cmplus.cm.v17;

import cmplus.util.CommandPlusBase;
import minigameLib.api.IngameEffect;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CommandOpen extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		

		if(t.argCheck(args[0], "txt")){
			try {
				Desktop.getDesktop().edit(new File(args[1]));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return;
		}
		
		BlockPos pos = new BlockPos(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
		if(t.argCheck(args[0], "문", "door")){
			IngameEffect.openDoor(pos, Boolean.valueOf(args[4]));
		}
		if(t.argCheck(args[0], "상자", "chest")){
			IngameEffect.openChestEffect(pos, Boolean.valueOf(args[4]));
		}
	}
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "door", "chest", "txt");
		return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
