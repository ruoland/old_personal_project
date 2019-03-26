package oneline.android;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandAnd extends CommandPlusBase {


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Android android = new Android(args[0]);
        sender.addChatMessage(new TextComponentString(android.isLogin ? "로그인 됐습니다." : "로그인에 실패했습니다."));

    }
}
