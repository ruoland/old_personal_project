package com.ruoland.customclient.beta;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandUI extends CommandBase {
    @Override
    public String getCommandName() {
        return "ui2";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "gui";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().displayGuiScreen(new GuiCustomUI("customgui/customUI/"+args[0], args[1]));
    }
}
