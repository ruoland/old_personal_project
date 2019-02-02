package map.platformer;

import cmplus.util.CommandPlusBase;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStage extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        if(args[0].equalsIgnoreCase("reload")){
            Minecraft.getMinecraft().getLanguageManager().onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());

        }
        if(args[0].equalsIgnoreCase("stage1")){
            PlatEffect.getStage(1).start();
        }
        if(args[0].equalsIgnoreCase("func")){
            PlatEffect.getStage(1).runScript(args[1]);
        }
    }
}