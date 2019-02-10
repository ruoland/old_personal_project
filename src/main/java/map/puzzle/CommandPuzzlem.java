package map.puzzle;

import cmplus.util.CommandPlusBase;
import map.lopre2.EntityPreBlock;
import minigameLib.map.EntityDefaultNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public class CommandPuzzlem extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
        if (entity != null) {
            EntityDefaultNPC entitydefServer = EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
            EntityDefaultNPC entitydefClient = (EntityDefaultNPC) entity;

            if(args[0].equalsIgnoreCase("com")){
                EntityPuzzleBlockButton serverBlock = (EntityPuzzleBlockButton) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                EntityPuzzleBlockButton clientBlock = (EntityPuzzleBlockButton) entity;
                serverBlock.setCommand(t.getCommand(args, 1, args.length));
            }
        }
    }
}
