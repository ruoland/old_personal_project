package ruo.cmplus.deb;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import ruo.cmplus.util.CommandPlusBase;

import java.util.List;

public class CommandClassLoader extends CommandPlusBase {

	@Override
	public String getCommandName() {
		
		return "clload";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		RClassLoader classLoader = new RClassLoader();
		try {
			classLoader.preload(p_71515_2_[0], p_71515_2_[1]);
		} catch (Exception e) {
			p_71515_1_.addChatMessage(new TextComponentString("클래스 파일을 불러오지 못했습니다"));
			e.printStackTrace();
			return;
		}
		p_71515_1_.addChatMessage(new TextComponentString(p_71515_2_[0] + " 클래스 파일을 불러왔습니다"));
		return;
	}
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if(RClassLoader.fileName.isEmpty())
			RClassLoader.findAllFile(null);

		return getListOfStringsMatchingLastWord(args, RClassLoader.fileName);
	}

	
}
