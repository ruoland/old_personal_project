package ruo.test;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandTest extends CommandBase {
    @Override
    public String getCommandName() {
        return "test";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("m")){
            for(int i = 0; i < 50;i++){
                server.commandManager.executeCommand(sender, " /summon Zombie ~ ~ ~");
            }
            for(int i = 0; i < 50;i++){
                server.commandManager.executeCommand(sender, " /summon Spider ~ ~ ~");
            }
            for(int i = 0; i < 50;i++){

                server.commandManager.executeCommand(sender, " /summon Skeleton ~ ~ ~");
            }
        }
    }
}
