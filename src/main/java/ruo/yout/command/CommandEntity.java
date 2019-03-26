package ruo.yout.command;

import cmplus.util.CommandPlusBase;
import map.lopre2.EntityPreBlock;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class CommandEntity extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for(Entity entity : sender.getEntityWorld().loadedEntityList){
            if(!(entity instanceof EntityPlayer)){
                boolean isJumpBlock = entity instanceof EntityPreBlock && ((EntityPreBlock) entity).getJumpName().equalsIgnoreCase(args[0]);
                if(args[0].equalsIgnoreCase("all") || EntityList.getEntityString(entity).equalsIgnoreCase(args[0])
                        || entity.getCustomNameTag().equalsIgnoreCase(args[0]) || isJumpBlock){
                    runCommand(entity, args);
                }
            }
        }
    }

    public abstract void runCommand(Entity livingBase, String[] args) throws CommandException;
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
    }

}
