package ruo.cmplus.cm.v18;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandHeal extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        EntityPlayer base;
        if(args.length == 2) {
            base = getPlayer(server, sender, args[0]);
            base.heal(Float.valueOf(args[1]));
        }
        else {
            base = (EntityPlayer) sender;
            if (args.length == 0) {
                base.heal(base.getMaxHealth());
                base.getFoodStats().setFoodLevel(20);
            }
            if (args.length == 1) {
                base.heal(Float.valueOf(args[0]));
            }
        }
    }
}
