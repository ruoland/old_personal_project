package cmplus.cm.v17;

import cmplus.util.CommandPlusBase;
import oneline.api.WorldAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public class CommandClip extends CommandPlusBase {

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "현재 좌표를 클립보드에 붙여넣습니다";
    }

    boolean debMode = false;//디버그 모드.
    String start="", end = "";
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
        double posX = WorldAPI.cut(sender.getCommandSenderEntity().posX);
        double posY = WorldAPI.cut(sender.getCommandSenderEntity().posY);
        double posZ = WorldAPI.cut(sender.getCommandSenderEntity().posZ);

        String playerPosDeb = start+posX + ", " + posY + ", " + posZ+end;
        String playerPos = start+posX + " " + posY + " " + posZ+end;
        String yawpitchDeb = start+sender.getCommandSenderEntity().rotationYaw + "," + sender.getCommandSenderEntity().rotationPitch+end;
        String yawpitch = start+sender.getCommandSenderEntity().rotationYaw + " " + sender.getCommandSenderEntity().rotationPitch+end;
        String currentClip = null;
        try {
            currentClip = "" + board.getContents(DataFlavor.stringFlavor).getTransferData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
            currentClip = "";
        }

        if (args.length == 0) {
            sender.addChatMessage(new TextComponentString("클립보드에 있는 내용:" +currentClip));
            board.setContents(new StringSelection(debMode ? playerPosDeb : playerPos), null);
            return;
        }
        if(args[0].equalsIgnoreCase("app")){
            if(args[1].equalsIgnoreCase("start")){
                if(args.length != 3)
                    start = "";
                start = t.getCommand(args, 2, args.length);
            }
            if(args[1].equalsIgnoreCase("end")){
                if(args.length != 3)
                    end = "";
                end = t.getCommand(args, 2, args.length);
            }
        }
        if (t.argCheck(args[0], "deb")) {
            debMode = !debMode;
            System.out.println(debMode);
        } else if (t.argCheck(args[0], "p", "y", "yaw", "pitch", "ya", "pi", "yp")) {
            sender.addChatMessage(new TextComponentString("YAW" + sender.getCommandSenderEntity().rotationYaw + "-Pitch:" + sender.getCommandSenderEntity().rotationPitch));
            board.setContents(new StringSelection(debMode ? yawpitchDeb : yawpitch), null);
        }
        try {
            sender.addChatMessage(new TextComponentString("클립보드에 있는 내용:" + board.getContents(DataFlavor.stringFlavor).getTransferData(DataFlavor.stringFlavor)));
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
                                                BlockPos pos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "yaw", "pitch", "look");
        return super.getTabCompletionOptions(server, sender, args, pos);
    }

}
