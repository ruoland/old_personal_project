package cmplus.cm.v18.function;

import cmplus.deb.DebAPI;
import cmplus.util.CommandPlusBase;
import olib.api.WorldAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CommandFunction extends CommandPlusBase {
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if (t.length(args)) {
			t.addLoMessage("help.openfolder");
			t.addLoMessage("help.open");
			return;
		}
		Function function = Function.addFunction("커맨드", args[0]);
		DebAPI.msgfunc(args[0], "-명령어-생성함");
		if (args.length > 1 && args[1].indexOf("open") != -1) {
			if (args[1].equals("openfolder"))
				function.openFolder();
			else
				function.openFile();
			DebAPI.msgfunc(args[0] , "-명령어-펑션 파일 열었음");
			return;
		} else if(args.length > 1 && args[1].indexOf("pause") != -1){
			function.stopRead = true;
		}else if(args.length > 1 && args[1].indexOf("resume") != -1){
			function.stopRead = false;
		}else {
			DebAPI.msgfunc(args[0], "-명령어-펑션 실행");
			function.runScript(args);
		}

		
		if (function.getFile().length() == 0) {
			sender.addChatMessage(text("펑션에 명령어가 없습니다.")
					.appendSibling(text("(파일 열기) ", new ClickEvent(Action.RUN_COMMAND, "/func " + args[0] + " open"),
							TextFormatting.UNDERLINE))
					.appendSibling(
							text(" (폴더 열기)", new ClickEvent(Action.RUN_COMMAND, "/func " + args[0] + " openfolder"),
									TextFormatting.UNDERLINE)));
		}
	}


	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		File file = new File(WorldAPI.getCurrentWorldFile() + "/commandplus/" + "script/");
		File[] fileList = file.listFiles();
		Arrays.sort(fileList, new FunctionComparator());
		String[] fileNameList = file.list();
		String[] str = new String[fileNameList.length];
		for(int i = 0; i < fileNameList.length;i++) {
			str[i] = fileNameList[i].replace(".txt", "");
		}
		if(args.length == 1)
		return getListOfStringsMatchingLastWord(args, str);
		else
			return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
