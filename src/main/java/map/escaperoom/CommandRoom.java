package map.escaperoom;

import cmplus.util.CommandPlusBase;
import map.escaperoom.base.EntityRoomDoorBase;
import map.lopre2.EntityPreBlock;
import minigameLib.command.CommandEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import olib.api.WorldAPI;
import olib.map.EntityDefaultNPC;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


public class CommandRoom extends CommandPlusBase {
    private int x,y,z, x2,y2,z2;
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equalsIgnoreCase("cleararrow")){
            for(Entity entity : sender.getEntityWorld().loadedEntityList){
                if(entity instanceof EntityArrow){
                    entity.setDead();
                }
            }
        }
        if(args[0].equalsIgnoreCase("jumpclear")){
            for(Entity entity : sender.getEntityWorld().loadedEntityList){
                if(entity instanceof EntityRoomBlockJumpMap){
                    entity.setDead();
                    EntityRoomBlockJumpMap.jump_count = 0;
                    EntityPreBlock.ax = 3;
                }
            }
        }
        if(args[0].equalsIgnoreCase("movezombie")){
            for(Entity entity : sender.getEntityWorld().loadedEntityList){
                if(entity instanceof EntityRoomMoveZombie){
                    EntityRoomMoveZombie entitydefServer = (EntityRoomMoveZombie) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                    EntityRoomMoveZombie entitydefClient = (EntityRoomMoveZombie) entity;
                    entitydefServer.teleportSpawnPos();
                    entitydefServer.removeTarget();
                    entitydefServer.startMove();
                    entitydefClient.teleportSpawnPos();
                    entitydefClient.removeTarget();
                    entitydefClient.startMove();
                }
            }
        }

        if(args[0].equalsIgnoreCase("pos1")){
            BlockPos vec3d = sender.getPosition();
            x = vec3d.getX();
            y = vec3d.getY();
            z = vec3d.getZ();
        }
        if(args[0].equalsIgnoreCase("pos2")){
            BlockPos vec3d = sender.getPosition();
            x2 = vec3d.getX();
            y2 = vec3d.getY();
            z2 = vec3d.getZ();
        }
        if(args[0].equalsIgnoreCase("set")){
            WorldAPI.setBlock(sender.getEntityWorld(), parseInt(args[1]), parseInt(args[2]), parseInt(args[3]), parseInt(args[4]), parseInt(args[5]), parseInt(args[6]), Blocks.QUARTZ_BLOCK);
        }

        if(args[0].equalsIgnoreCase("clip")){
            Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringBuilder builder = new StringBuilder("room set ").append(x).append(" ").append(y).append(" ").append(z).append(" ").append(x2)
                    .append(" ").append(y2).append(" ").append(z2);
            board.setContents(new StringSelection(builder.toString()), null);
        }
        if(args[0].equalsIgnoreCase("open")){
            EntityRoomDoorBase doorBlock = (EntityRoomDoorBase) EntityDefaultNPC.getNPC(args[1]);
            doorBlock.open();
        }
        if(args[0].equalsIgnoreCase("close")){
            EntityRoomDoorBase doorBlock = (EntityRoomDoorBase) EntityDefaultNPC.getNPC(args[1]);
            doorBlock.close();
        }

    }
}
