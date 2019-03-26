package ruo.yout.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CommandTPEntity extends CommandEntity {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        super.execute(server, sender, args);
    }

    @Override
    public void runCommand(Entity livingBase, String[] args) throws CommandException {
        livingBase.setPosition(parseDouble(args[1]), parseDouble(args[2]), parseDouble(args[3]));

    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
    }
}
