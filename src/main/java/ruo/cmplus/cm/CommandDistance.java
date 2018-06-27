package ruo.cmplus.cm;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.util.CommandPlusBase;

import javax.annotation.Nullable;
import java.util.List;

public class CommandDistance extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		int distance = Integer.valueOf(p_71515_2_[1]);
		if (!check(distance, 100)) {
			t.s.renderDistanceChunks = t.math(p_71515_2_[0], t.s.renderDistanceChunks, distance);
		}
		t.addSettingMessage(t.s.renderDistanceChunks);
	}

	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "set", "add", "sub");
		else 
			return super.getTabCompletionOptions(server, sender, args, pos);
	}

	/**
	 * 0보다 낮거나 100보다 큰 경우
	 */
	public boolean check(int min, int max){
		if(min <= 0 || min >= max){
			return true;
		}
		else return false;
	}
}
