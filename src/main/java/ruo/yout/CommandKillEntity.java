package ruo.yout;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandKillEntity extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for(Entity entity : sender.getEntityWorld().loadedEntityList){
            if(entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)){
                if(EntityList.getEntityString(entity).equalsIgnoreCase(args[0])){
                    entity.setDead();
                }
            }
        }
    }
}
