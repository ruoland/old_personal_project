package ruo.yout.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.yout.MoJaeEvent;

import java.util.List;

public class CommandUnlockEntity extends CommandEntity {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        super.execute(server, sender, args);
            MoJaeEvent.lockList.remove(args[0]);
    }

    @Override
    public void runCommand(Entity livingBase, String[] args) throws CommandException {
        if (livingBase.getCustomNameTag().equalsIgnoreCase("잠금")) {
            livingBase.setCustomNameTag("");
            System.out.println("잠금 해제 됐습니다");
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
    }
}
