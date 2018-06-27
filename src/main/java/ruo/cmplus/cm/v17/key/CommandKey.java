package ruo.cmplus.cm.v17.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.util.CommandPlusBase;

import java.util.List;

public class CommandKey extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		super.execute(server, sender, args);
		KeyManager manager = KeyManager.instance();
		String command;
		KeyBinding keycode;
		if (t.argCheck(args[0], "reload", "리로드")) {
			manager.loadKey();
			t.addLoMessage("키를 리로드 했습니다.");
			return;
		}
		 if(t.argCheck(args[0], "add", "추가")){
			command = t.getCommand(args, 2, args.length);
			manager.addKey(manager.getKey(args[1]), command);
			t.addLoMessage(Keyboard.getKeyName(manager.getKey(args[1]))+" 키에 "+command+" 명령어를 추가 했습니다.");
			KeyBinding.resetKeyBindingArrayAndHash();
			return;
		 }
		 if (t.argCheck(args[0], "change", "변경")) {
			 command = t.getCommand(args, 1, args.length-1);//이건 명령어가 아니라 키이름을 담는 거임
			 keycode = manager.getKeyBinding(command);
			 manager.replaceKey(keycode, manager.getKey(args[args.length-1]));
			 t.addLoMessage(command+" 키를 "+Keyboard.getKeyName(keycode.getKeyCode())+"로 변경했습니다.");
			 KeyBinding.resetKeyBindingArrayAndHash();
			 return;
		 }
		command = t.getCommand(args, 1, args.length);
		keycode = manager.getKeyBinding(command);
	
		if (t.argCheck(args[0], "remove", "삭제")) {
			manager.removeKey(keycode.getKeyCode());
			t.addLoMessage(Keyboard.getKeyName(keycode.getKeyCode())+" 키에 저장된 명령어를 삭제했습니다.");
		}
		if (t.argCheck(args[0], "reset", "초기화", "리셋")) {
			keycode.setToDefault();
			t.addLoMessage(Keyboard.getKeyName(keycode.getKeyCode())+" 키를 초기 키로 설정했습니다.");
		}
		KeyBinding.resetKeyBindingArrayAndHash();

	}
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		return getListOfStringsMatchingLastWord(args, "reload","add","change", "remove",  "reset");
	}
	
}
