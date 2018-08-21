package ruo.cmplus.cm;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import ruo.cmplus.CMManager;
import ruo.cmplus.util.CommandPlusBase;
import ruo.cmplus.util.CommandTool;

import java.util.ArrayList;
import java.util.List;

public class CommandUI extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        CommandTool t = new CommandTool(getCommandName());
        if (t.length(args)) {
            return;
        }
        String ui = args[0].toUpperCase();
        try {
            if (!ui.equalsIgnoreCase("hand") && !ui.equalsIgnoreCase("blocklayer"))
                RenderGameOverlayEvent.ElementType.valueOf(ui);
        } catch (java.lang.IllegalArgumentException e) {
            t.addErrorMessage(ui);
            return;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            for (ElementType elementType : RenderGameOverlayEvent.ElementType.values())
                CMManager.setUI(elementType.name(), true);
            CMManager.setUI("HAND", true);
            CMManager.setUI("blocklayer", true);
            t.addLoMessage("reset");
            return;
        }
        if (ui.equalsIgnoreCase("exp")) {
            ui = "EXPERIENCE";
        }
        CMManager.setUI(ui, Boolean.valueOf(args[1]));
        t.addSettingMessage(args[0], Boolean.valueOf(args[1]));
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
                                                BlockPos pos) {
        if (args.length == 1) {
            ArrayList<String> str = new ArrayList<String>();
            for (ElementType s : RenderGameOverlayEvent.ElementType.values()) {
                str.add(s.name());
            }
            str.add("HAND");
            str.add("BLOCKLAYER");

            return getListOfStringsMatchingLastWord(args, str);
        } else if (args.length == 2)
            return getListOfStringsMatchingLastWord(args, "true", "false");
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
