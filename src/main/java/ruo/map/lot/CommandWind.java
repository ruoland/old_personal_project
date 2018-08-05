package ruo.map.lot;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.map.lot.dungeon.EntityWind;

public class CommandWind extends CommandPlusBase{

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        EntityWind wind = (EntityWind)getPlusEntity(server,sender, args[0]);
        wind.setWindMode(Integer.valueOf(args[1]));
    }
}
