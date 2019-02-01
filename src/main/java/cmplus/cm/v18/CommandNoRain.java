package cmplus.cm.v18;

import cmplus.CMManager;
import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandNoRain extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        CMManager.norain = Boolean.valueOf(args[0]);
    }
}
