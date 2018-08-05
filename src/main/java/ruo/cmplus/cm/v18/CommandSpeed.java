package ruo.cmplus.cm.v18;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;

public class CommandSpeed extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        EntityPlayer base = (EntityPlayer) sender;
        if(args.length > 0) {

            if (args[0].equalsIgnoreCase("reset"))
                base.capabilities.setPlayerWalkSpeed(0.1F);
            else
                base.capabilities.setPlayerWalkSpeed(Float.valueOf(args[0]));
        }
               else
        WorldAPI.addMessage("이동 속도 :"+base.capabilities.getWalkSpeed());
        base.sendPlayerAbilities();
    }
}

