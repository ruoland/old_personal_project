package ruo.cmplus.cm.v17;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.effect.AbstractTick;

public class CommandPosition extends CommandPlusBase {

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "플레이어가 정한 위치에 가면 명령어가 실행됩니다.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        double[] pos = getPos(sender, 0, args);
        if (args.length >= 4 && !args[3].startsWith("/")) {
            double[] pos2 = getPos(sender, 3, args);
            EntityAPI.position(pos[0], pos[1], pos[2],  pos2[0], pos2[1], pos2[2], 1, new AbstractTick.Command(t.getCommand(args, 3, args.length)));
            return;
        }

        if (args.length >= 4)
            EntityAPI.position((EntityLivingBase) sender, pos[0], pos[1], pos[2], new AbstractTick.Command(t.getCommand(args, 3, args.length)));
        else
            EntityAPI.position((EntityLivingBase) sender, sender.getPosition(), new AbstractTick.Command(t.getCommand(args, 0, args.length)));
    }

}
