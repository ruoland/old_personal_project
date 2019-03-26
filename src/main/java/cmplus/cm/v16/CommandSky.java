package cmplus.cm.v16;

import cmplus.util.CommandPlusBase;
import cmplus.util.Sky;
import oneline.api.WorldAPI;
import oneline.effect.AbstractTick;
import oneline.effect.TickRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSky extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, final String[] args) {
        if (t.length(args)) {
            return;
        }
        TickRegister.register(new AbstractTick(Type.RENDER, 1, false) {
            @Override
            public void run(Type type) {
                try {
                    if (args[0].equals("fog")) {
                        if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))
                            Sky.fogOnOff(t.parseBoolean(args[1]));
                        else
                            Sky.fogDistance(Float.valueOf(args[1]));
                    }
                    if (args[1].equals("star") && args.length > 1) {
                        Sky.starGenerate(t.findInteger(args, 2, 1500));
                    }
                    if (args[0].equals("cloud")) {
                        //if (args[1].equals("scale") && args.length > 3) {
                        //	Sky.cloudSize(Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
                        //}
                        //if (args[1].equals("color") && args.length > 3) {
                        //	Sky.cloudColor(Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
                        //}
                        if (args[1].equals("height") && args.length > 1) {
                            Sky.cloudHeight(Integer.valueOf(args[2]));
                        }
                    }
                    if (args[0].equals("weather")) {
                        if (args[1].equals("rain") && args.length > 1) {
                            Sky.setRain(new ResourceLocation(args[2]));
                        }
                        if (args[1].equals("snow") && args.length > 1) {
                            Sky.setSnow(new ResourceLocation(args[2]));
                        }
                        if (args[1].equals("sun") && args.length > 0) {
                            Sky.setSunTexture((args[1]));
                        }
                        if (args[1].equals("moon") && args.length > 0) {
                            Sky.setMoonTexture((args[1]));
                        }
                        if (args[1].equals("reset")) {
                            Sky.setSnow(new ResourceLocation("textures/environment/snow.png"));
                            Sky.setRain(new ResourceLocation("textures/environment/rain.png"));
                        }
                    }
                    if (args[0].equals("sky")) {

                        //if (args[1].equals("color")  && args.length > 3) {
                        //	Sky.skyRGB(Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
                        //}

                        if (args[0].equals("reset")) {
                            Sky.setSunTexture("textures/environment/sun.png");
                            Sky.setMoonTexture("textures/environment/moon_phases.png");
                            Sky.skyRGB(0, 0, 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    WorldAPI.addMessage(e.getLocalizedMessage());
                }


            }
        });

    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
                                                @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "cloud", "weather", "sky", "reset");

        }
        if (args.length == 2) {
            if (args[0].equals("sky")) {
                return getListOfStringsMatchingLastWord(args, "star", "color", "sunbright");
            }
            if (args[0].equals("cloud")) {
                return getListOfStringsMatchingLastWord(args, "scale", "color", "height");
            }
            if (args[0].equals("weather")) {
                return getListOfStringsMatchingLastWord(args, "snow", "rain", "sun", "moon", "reset");
            }
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
