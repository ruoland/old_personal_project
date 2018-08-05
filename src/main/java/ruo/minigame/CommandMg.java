package ruo.minigame;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

public class CommandMg extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        EntityPlayer player = (EntityPlayer) sender;
        Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
        if(args[0].equalsIgnoreCase("aa")){
            System.out.println(MiniGame.elytra.isStart());
            System.out.println(MiniGame.bomber.isStart());
            System.out.println(MiniGame.minerun.isStart());
            System.out.println(MiniGame.scroll.isStart());
        }
        if(entity != null){
            EntityDefaultNPC entitydefServer = EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
            EntityDefaultNPC entitydefClient = (EntityDefaultNPC) entity;

            float	x=0,y=0,z=0;
            if(args[0].equalsIgnoreCase("mo")) {
                entitydefServer.setModel(TypeModel.valueOf(args[1]));
                entitydefClient.setModel(TypeModel.valueOf(args[1]));

            }
            if(args[0].equalsIgnoreCase("block")) {
                entitydefServer.setBlockMode(Block.getBlockFromName(args[1]));
                entitydefClient.setBlockMode(Block.getBlockFromName(args[1]));

            }
            if(args[0].equalsIgnoreCase("ro")){
                x = t.math(args[1], entitydefServer.getRotateX(), Float.valueOf(args[2]));
                y = t.math(args[1], entitydefServer.getRotateY(), Float.valueOf(args[3]));
                z = t.math(args[1], entitydefServer.getRotateZ(), Float.valueOf(args[4]));
                entitydefServer.setRotate(x,y,z);
                entitydefClient.setRotate(x,y,z);
            }
            if(args[0].equalsIgnoreCase("sc") || args[0].equalsIgnoreCase("sca")){
                x = t.math(args[1], entitydefServer.getScaleX(), Float.valueOf(args[2]));
                y = t.math(args[1], entitydefServer.getScaleY(), Float.valueOf(args[3]));
                z = t.math(args[1], entitydefServer.getScaleZ(), Float.valueOf(args[4]));
                entitydefServer.setScale(x,y,z);
                entitydefClient.setScale(x,y,z);

            }
            if(args[0].equalsIgnoreCase("tra")){
                x = t.math(args[1], entitydefServer.getTraX(), Float.valueOf(args[2]));
                y = t.math(args[1], entitydefServer.getTraY(), Float.valueOf(args[3]));
                z = t.math(args[1], entitydefServer.getTraZ(), Float.valueOf(args[4]));
                entitydefServer.setTra(x,y,z);
                entitydefClient.setTra(x,y,z);
            }

            if(args[0].equalsIgnoreCase("trans")){
                x = t.math(args[1], entitydefServer.getTransparency(), Float.valueOf(args[2]));
                entitydefServer.setTransparency(x);
                entitydefClient.setTransparency(x);
            }
            if(args[0].equalsIgnoreCase("rgb")){
                x = t.math(args[1], entitydefServer.getRed(), Float.valueOf(args[2]));
                y = t.math(args[1], entitydefServer.getGreen(), Float.valueOf(args[3]));
                z = t.math(args[1], entitydefServer.getBlue(), Float.valueOf(args[4]));
                entitydefServer.setRGB(x,y,z);
                entitydefClient.setRGB(x,y,z);
            }
            System.out.println(""+entitydefServer.isServerWorld()+entitydefClient.isServerWorld());
        }
    }
}
