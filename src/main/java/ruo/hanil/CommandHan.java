package ruo.hanil;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandHan extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("sel"))
        Minecraft.getMinecraft().displayGuiScreen(new GuiSelect());
        if(args[0].equalsIgnoreCase("loa"))
            Minecraft.getMinecraft().displayGuiScreen(new GuiHanLoading());

    }
}
