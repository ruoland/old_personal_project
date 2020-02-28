package ruo.yout.skeldelay0;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.yout.MojaeTest;

public class SkeletonDelay0 extends CommandBase {
    @Override
    public String getCommandName() {
        return "skd";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "ㅎ";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("1")){
            MojaeTest.monsterSpawn("Skeleton", "Zombie", 5, 10, true);
        }
    }
}
