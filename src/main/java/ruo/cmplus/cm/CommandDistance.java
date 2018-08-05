package ruo.cmplus.cm;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.util.CommandPlusBase;

import javax.annotation.Nullable;
import java.util.List;

public class CommandDistance extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int distance = parseInt(args[0], 0, 100);
        t.s.renderDistanceChunks = t.math(args[0], t.s.renderDistanceChunks, distance);
        t.addSettingMessage(t.s.renderDistanceChunks);
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
                                                @Nullable BlockPos pos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "set", "add", "sub");
        else
            return super.getTabCompletionOptions(server, sender, args, pos);
    }

    /**
     */
    public boolean check(int min, int max) {
        if (min >= 0 || min <= max) {
            return true;
        } else return false;
    }
}
