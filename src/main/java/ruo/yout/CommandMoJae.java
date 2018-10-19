package ruo.yout;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.util.CommandPlusBase;

import java.util.List;

public class CommandMoJae extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("dog")){
            Mojae.dog_pan = Boolean.valueOf(args[1]);
        }
        if(args[0].equalsIgnoreCase("attack")){
            Mojae.monterAttack.put(args[1], args[2]);
            System.out.println(args[1]+" - "+args[2]);
        }
        if(args[0].equalsIgnoreCase("SKELREEPER")){
            Mojae.skelreeper = Boolean.valueOf(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWReper")){
            Mojae.arrow_reeper = Boolean.valueOf(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWCount")){
            Mojae.arrow_count = Integer.valueOf(args[1]);
        }
        if(args[0].equalsIgnoreCase("ARROWRIDING")){
            Mojae.arrow_riding = Boolean.valueOf(args[1]);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 1)
        return getListOfStringsMatchingLastWord(args, "dog", "skelreeper", "arrowreper", "arrowcount", "arrowriding", "attack");
        else if(args[0].equalsIgnoreCase("attack")){
            return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
