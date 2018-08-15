package ruo.cmplus.cm.v18;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import ruo.cmplus.WaypointManager;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

import java.util.List;

public class CommandWaypoint extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(args.length > 1) {
			String key = !args[1].contains(":") ? WorldAPI.getCurrentWorldName() + ":" + args[1] : args[1];
			if (args[0].equals("set")) {
				if(WaypointManager.has(key)){
					sender.addChatMessage(new TextComponentString(key+" 웨이포인트는 이미 존재합니다."));
					return;
				}
				if (args.length == 2) {
					sender.addChatMessage(new TextComponentString(key+" 웨이포인트를 만들었습니다."));
					WaypointManager.add(key, new double[]{WorldAPI.x(), WorldAPI.y(), WorldAPI.z()});
				}
				else
					WaypointManager.add(key, WorldAPI.parseDouble(args[2], args[3], args[4]));
			} else if (args[0].equals("remove")) {
				if(WaypointManager.has(key)){
					WaypointManager.remove(key);
					sender.addChatMessage(new TextComponentString(key+" 웨이포인트를 제거했습니다."));
				}
				else
					sender.addChatMessage(new TextComponentString(WorldAPI.getCurrentWorldName() + ":" + args[0] + " 웨이포인트가 존재하지 않습니다."));
			}
		}
		else {
			String key = !args[0].contains(":") ? WorldAPI.getCurrentWorldName() + ":" + args[0] : args[0];
			if(WaypointManager.has(key)) {
				double[] dou = WaypointManager.get(key);
				WorldAPI.teleport(dou[0], dou[1], dou[2]);
			}
			else
				sender.addChatMessage(new TextComponentString(key+"라는 웨이포인트가 존재하지 않습니다."));

		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		return getListOfStringsMatchingLastWord(args, WaypointManager.keySet());
	}
}
