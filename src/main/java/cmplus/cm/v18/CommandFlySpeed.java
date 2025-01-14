package cmplus.cm.v18;

import cmplus.util.CommandPlusBase;
import olib.api.WorldAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandFlySpeed extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        EntityPlayer base = (EntityPlayer) sender;
        if(args.length > 0) {
            if (args[0].equalsIgnoreCase("reset"))
                base.capabilities.setFlySpeed(0.05F);
            else
                base.capabilities.setFlySpeed(Float.valueOf(args[0]));
        }
        else
            WorldAPI.addMessage("플라이 속도 :"+base.capabilities.getFlySpeed());
        base.sendPlayerAbilities();
    }
}

