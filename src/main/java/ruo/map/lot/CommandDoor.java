package ruo.map.lot;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandDoor extends CommandPlusBase{

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        EntityDoorBlock wind = (EntityDoorBlock)getPlusEntity(server,sender, args[0]);
        if(args[1].equalsIgnoreCase("open"))
            wind.open();
        if(args[1].equalsIgnoreCase("close"))
            wind.close();

    }
}
