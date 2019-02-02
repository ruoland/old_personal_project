package map.mafence.system;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandMafence extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("waveend")){//모든 웨이브 몬스터를 내보낸 경우에 발동
            MafenceSystem.getInstance().waveEnd();
        }
    }
}
