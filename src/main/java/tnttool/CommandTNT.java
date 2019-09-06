package tnttool;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Explosion;

public class CommandTNT extends CommandBase {
    @Override
    public String getCommandName() {
        return "tnt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tnt help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("tnt")){

        }
    }
}
