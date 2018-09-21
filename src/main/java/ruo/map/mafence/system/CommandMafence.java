package ruo.map.mafence.system;

import net.minecraft.block.BlockCommandBlock;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import ruo.cmplus.util.CommandPlusBase;

public class CommandMafence extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("lifesub")){
            MafenceSystem.getInstance().lifePointSub();
        }
        if(args[0].equalsIgnoreCase("lifeadd")){
            MafenceSystem.getInstance().lifePointSub();
        }
        if(args[0].equalsIgnoreCase("setspawn")){
            CommandBlockBaseLogic baseLogic = (CommandBlockBaseLogic) sender;
            MafenceSystem.getInstance().setMonsterSpawn(baseLogic.getPosition());
        }
    }
}
