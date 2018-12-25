package com.ruoland.customclient;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.lwjgl.opengl.GL11;
import ruo.minigame.api.RenderAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

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
                    CustomTool.closeBrowser();
                }
                try {
                    CustomTool.drawBrowser("https://www.youtube.com/watch?v=sBapIS_NEgE", parseInt(args[0]), parseInt(args[1]), parseInt(args[2]), parseInt(args[3]));
                } catch (NumberInvalidException e) {
                    e.printStackTrace();
                }
            }
        };
        TickRegister.register(abstractTick);
    }


}
