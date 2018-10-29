package ruo.yout.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CommandLockEntity extends CommandEntity {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        super.execute(server, sender, args);
    }

    @Override
    public void runCommand(EntityLivingBase livingBase, String[] args) throws CommandException {
        if (livingBase.getCustomNameTag().equalsIgnoreCase("잠금")) {
            livingBase.setCustomNameTag("");
            System.out.println("잠금 해제 됐습니다");
        }
        else
            livingBase.setCustomNameTag("잠금");
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
    }
}
