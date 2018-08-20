package ruo.cmplus.cm.v18.function;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import ruo.cmplus.deb.DebAPI;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

import java.io.File;
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
					.appendSibling(text("(파일 열기)", new ClickEvent(Action.RUN_COMMAND, "/func " + args[0] + " open"),
							TextFormatting.UNDERLINE))
					.appendSibling(
							text(" (폴더 열기)", new ClickEvent(Action.RUN_COMMAND, "/func " + args[0] + " openfolder"),
									TextFormatting.UNDERLINE)));
		}
	}

	public static ITextComponent text(String text) {
		return new TextComponentString(text);
	}

	public static ITextComponent text(String text, ClickEvent event) {
		return new TextComponentSelector(text).setStyle(new Style().setClickEvent(event));
	}

	public static ITextComponent text(String text, ClickEvent event, TextFormatting form) {
		return new TextComponentSelector(text).setStyle(new Style().setClickEvent(event).setColor(form));
	}

	public static ITextComponent text(String text, TextFormatting form) {
		return new TextComponentSelector(text).setStyle(new Style().setColor(form));
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		File file = new File(WorldAPI.getCurrentWorldFile() + "/commandplus/" + "script/");
		String[] str = new String[file.list().length];
		for(int i = 0; i < file.list().length;i++) {
			str[i] = file.list()[i].replace(".txt", "");
		}
		if(args.length == 1)
		return getListOfStringsMatchingLastWord(args, str);
		else
			return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
