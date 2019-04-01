package minigameLib.command;

import cmplus.util.Sky;
import minigameLib.MiniGame;
import oneline.api.WorldAPI;
import oneline.effect.AbstractTick;
import oneline.effect.TickRegister;
import minigameLib.minigame.minerun.MineRun;
import minigameLib.minigame.minerun.MineRunEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandMineRun extends CommandBase {

    @Override
    public String getCommandName() {
        return "minerun";

    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equals("fog") && args.length > 1) {
                Sky.fogDistance(-1);
                TickRegister.register(new AbstractTick(1, true) {
                    @Override
                    public void run(TickEvent.Type type) {
                        Sky.fogDistance(Sky.getFogDistance() - 0.5F);
                        try {
                            if (Sky.getFogDistance() <= parseInt(args[1])) {
                                absLoop = false;
                            }
                        } catch (NumberInvalidException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return;
            }
            if (args[0].equals("left")) {
                EntityPlayer player = (EntityPlayer) sender;
                WorldAPI.teleport(player.posX, player.posY, player.posZ, player.getHorizontalFacing().getHorizontalAngle() + 90, 70);
                MiniGame.minerun.start(sender, args);
                return;
            }
            if (args[0].equals("right")) {
                EntityPlayer player = (EntityPlayer) sender;
                WorldAPI.teleport(player.posX, player.posY, player.posZ, player.getHorizontalFacing().getHorizontalAngle() - 90, 70);
                MiniGame.minerun.start(sender, args);
                return;
            }
            if (args[0].equals("lava")) {
                for (int i = 0; i < MineRun.removeLavaState.size(); i++) {
                    sender.getEntityWorld().setBlockState(MineRun.removeLavaPos.get(i), MineRun.removeLavaState.get(i));
                }
                return;
            }
            if (args[0].equals("spawnpoint")) {
                MineRun.spawnPoint = MineRun.runner.getPositionVector();

                return;
            }
            if (args[0].equals("respawn")) {
                MineRunEvent.respawnTime = 61;
                WorldAPI.command("/minerun lava");
                return;
            }
            if (args[0].equals("half")) {
                MineRunEvent.halfMode =  parseBoolean(args[1]);
                MiniGame.minerun.start(sender, args);
                return;
            }
        }
        if (args.length > 0 && (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("s")))
            MiniGame.minerun.end();
        else
            MiniGame.minerun.start(sender, args);
    }

}
