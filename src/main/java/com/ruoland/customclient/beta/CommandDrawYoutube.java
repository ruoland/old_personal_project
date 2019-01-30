package com.ruoland.customclient.beta;

import com.ruoland.customclient.AbstractTick;
import com.ruoland.customclient.GuiCustomBase;
import com.ruoland.customclient.TickRegister;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandDrawYoutube extends CommandBase {
    @Override
    public String getCommandName() {
        return "drawyou";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }
    float alpha = 1.0F;
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        AbstractTick abstractTick = new AbstractTick(args[0]+args[1],TickEvent.Type.RENDER, 1, true) {
            @Override
            public boolean stopCondition() {
                return alpha < 0;
            }
            @Override
            public void run(TickEvent.Type type) {
                if(absRunCount > 500) {
                    stopTick();
                    GuiCustomBase.closeBrowser();
                }
                try {
                    GuiCustomBase.drawBrowser("https://www.youtube.com/watch?v=sBapIS_NEgE", parseInt(args[0]), parseInt(args[1]), parseInt(args[2]), parseInt(args[3]));
                } catch (NumberInvalidException e) {
                    e.printStackTrace();
                }
            }
        };
        TickRegister.register(abstractTick);
    }


}
