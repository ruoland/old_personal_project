package ruo.yout.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CommandHealthEntity extends CommandEntity {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        super.execute(server, sender, args);
    }

    @Override
    public void runCommand(Entity entity, String[] args) throws CommandException{
        if(entity instanceof EntityLivingBase) {
            double health;
            if(args[1].equalsIgnoreCase("max")){
                health = ((EntityLivingBase) entity).getMaxHealth();
            }
            else
                health = parseDouble(args[1]);
            ((EntityLivingBase) entity).setHealth((float) health);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
    }
}
