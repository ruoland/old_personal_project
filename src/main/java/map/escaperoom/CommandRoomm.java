package map.escaperoom;

import cmplus.util.CommandPlusBase;
import map.escaperoom.base.EntityRoomDoorBase;
import olib.map.EntityDefaultNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public class CommandRoomm extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
        if (entity != null) {
            EntityDefaultNPC entitydefServer = EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
            EntityDefaultNPC entitydefClient = (EntityDefaultNPC) entity;

            if(args[0].equalsIgnoreCase("com")){
                EntityRoomBlockButton serverBlock = (EntityRoomBlockButton) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                EntityRoomBlockButton clientBlock = (EntityRoomBlockButton) entity;
                serverBlock.setCommand(t.getCommand(args, 1, args.length));
            }
            if(args[0].equalsIgnoreCase("name")){
                if(entity instanceof EntityRoomDoorBase) {
                    EntityRoomDoorBase serverBlock = (EntityRoomDoorBase) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                    EntityRoomDoorBase clientBlock = (EntityRoomDoorBase) entity;
                    serverBlock.setDoorName(t.getCommand(args, 1, args.length));
                }else{
                    EntityRoomPathCreeper serverPath = (EntityRoomPathCreeper) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                    EntityRoomPathCreeper clientPath = (EntityRoomPathCreeper) entity;
                    serverPath.setCustomNameTag(args[1 ]);
                }
            }
            if(args[0].equalsIgnoreCase("mincount")){
                EntityRoomDoorBase serverBlock = (EntityRoomDoorBase) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                EntityRoomDoorBase clientBlock = (EntityRoomDoorBase) entity;
                serverBlock.setOpenMinCount(parseInt(args[1]));
            }
            if(args[0].equalsIgnoreCase("pathcom")){
                EntityRoomPathCreeper serverBlock = (EntityRoomPathCreeper) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                EntityRoomPathCreeper clientBlock = (EntityRoomPathCreeper) entity;
                serverBlock.setCommand(t.getCommand(args, 1, args.length));
            }
            if(args[0].equalsIgnoreCase("pathpath")){
                EntityRoomPathCreeper serverBlock = (EntityRoomPathCreeper) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                EntityRoomPathCreeper clientBlock = (EntityRoomPathCreeper) entity;
                serverBlock.teleportSpawnPos();
                serverBlock.setTarget(parseDouble(args[1]), parseDouble(args[2]), parseDouble(args[3]));
            }
        }
    }

}
