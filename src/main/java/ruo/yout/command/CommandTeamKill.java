package ruo.yout.command;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.yout.Mojae;

public class CommandTeamKill extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Mojae.canTeamKill = parseBoolean(args[0]);
    }
}
