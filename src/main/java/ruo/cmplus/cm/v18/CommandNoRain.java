package ruo.cmplus.cm.v18;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.CMManager;
import ruo.cmplus.util.CommandPlusBase;

public class CommandNoRain extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        CMManager.norain = Boolean.valueOf(args[0]);
    }
}
