package ruo.cmplus.cm;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.util.CommandPlusBase;
import ruo.cmplus.util.CommandTool;
import ruo.minigame.api.WorldAPI;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCloud extends CommandPlusBase {

	@Override
	public String getCommandName() {
		
		return "cloud";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
	
		return 2;
	}
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commandPlus.cloud.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, final String[] command) throws CommandException{
		CommandTool t = new CommandTool(getCommandName());
		EntityPlayerMP p = WorldAPI.getPlayerMP();
		if(t.length(command)){
			return;
		}
		int b = Integer.valueOf(command[0]);
		t.addSettingMessage(b);
		//sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commandPlus.cloud.setting", Boolean.valueOf(args[0]))));
		t.s.clouds = b;
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	return getListOfStringsMatchingLastWord(args, "0","1","2");
    }
}
