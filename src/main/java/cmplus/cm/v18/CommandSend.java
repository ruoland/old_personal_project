package cmplus.cm.v18;

import cmplus.CMPlus;
import cmplus.test.CMPacketCommand;
import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandSend extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        final String command = t.getCommand(args, 1, args.length);
        Entity target = getEntity(server, sender, args[0]);
        if(target instanceof EntityPlayer){
            EntityPlayerMP player = (EntityPlayerMP) target;
            CMPlus.INSTANCE.sendTo(new CMPacketCommand(command), player);
        }
    }
}
