package cmplus.cm.v15;

import cmplus.CMManager;
import cmplus.util.CommandPlusBase;
import oneline.api.WorldAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;

public class CommandGameOver extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer entity = WorldAPI.getPlayer();
		CMManager.setGameOver(args[0].replace("/n/", " "),new TextComponentString(args[1].replace("/n/", " ")), t.findBoolean(args, 2, true), t.findBoolean(args, 3, true));
		entity.setHealth(0);
		notifyCommandListener(sender, this, "commands.kill.successful", new Object[] {entity.getDisplayName()});
	}

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	if(args.length == 2 || args.length == 3){
    		 return getListOfStringsMatchingLastWord(args, "true", "false");
    	}
    	return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
