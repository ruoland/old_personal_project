package customclient.beta;

import customclient.AbstractTick;
import customclient.RenderAPI;
import customclient.TickRegister;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandDrawTexture extends CommandBase {
    @Override
    public String getCommandName() {
        return "drawtex";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }
    float alpha = 1.0F;
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        AbstractTick abstractTick = new AbstractTick(args[0]+args[1]+args[2]+args[3]+args[4]+args[5],TickEvent.Type.RENDER, 1, true) {
            @Override
            public boolean stopCondition() {
                return alpha < 0;
            }
            @Override
            public void run(TickEvent.Type type) {
                try {
                    RenderAPI.drawTexture(args[0], alpha, parseDouble(args[1]), parseDouble(args[2]), parseDouble(args[3]), parseDouble(args[4]));
                } catch (NumberInvalidException e) {
                    e.printStackTrace();
                }
            }
        };
        TickRegister.register(new AbstractTick(args[0]+args[1]+args[2]+args[3]+args[4]+args[5]+"timer", TickEvent.Type.SERVER, parseInt(args[5]), true) {
            @Override
            public boolean stopCondition() {
                return alpha < 0;
            }
            @Override
            public void run(TickEvent.Type type) {
                absDefTick = 1;
                alpha -= 1F/20F;
                if(alpha <= -1)
                {
                    absLoop =false;
                    alpha = 1;
                }
                System.out.println(alpha);
            }
        });
        TickRegister.register(abstractTick);
    }
}
