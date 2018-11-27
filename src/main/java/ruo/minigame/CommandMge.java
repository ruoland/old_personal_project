package ruo.minigame;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import ruo.cmplus.util.CommandPlusBase;
import ruo.map.lopre2.EntityPreBlock;
import ruo.map.lopre2.jump1.EntityLavaBlock;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;
import ruo.yout.command.CommandEntity;

public class CommandMge extends CommandEntity {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        super.execute(server, sender, args);
    }

    @Override
    public void runCommand(Entity livingBase, String[] args) throws CommandException {
        EntityDefaultNPC entitydefServer = EntityDefaultNPC.getUUIDNPC(livingBase.getUniqueID());
        EntityDefaultNPC entitydefClient = (EntityDefaultNPC) livingBase;

        String type = args[1], arg1 = args[2];
        System.out.println(type+ " - " + arg1+" - "+entitydefServer.isServerWorld() +" - "+entitydefClient.isServerWorld());

        float	x,y,z;

        if(type.equalsIgnoreCase("model")) {
            entitydefServer.setModel(TypeModel.valueOf(arg1));
            entitydefClient.setModel(TypeModel.valueOf(arg1));
        }
        if(type.equalsIgnoreCase("block")) {
            entitydefServer.setBlockMode(Block.getBlockFromName(arg1));
            entitydefClient.setBlockMode(Block.getBlockFromName(arg1));
        }
        if(type.equalsIgnoreCase("rotate") || type.equalsIgnoreCase("ro")){
            x = t.math(arg1, entitydefServer.getRotateX(), Float.valueOf(args[3]));
            y = t.math(arg1, entitydefServer.getRotateY(), Float.valueOf(args[4]));
            z = t.math(arg1, entitydefServer.getRotateZ(), Float.valueOf(args[5]));
            entitydefServer.setRotate(x,y,z);
            entitydefClient.setRotate(x,y,z);
        }
        if(type.equalsIgnoreCase("sc") || args[0].equalsIgnoreCase("scale")){
            x = t.math(arg1, entitydefServer.getScaleX(), Float.valueOf(args[3]));
            y = t.math(arg1, entitydefServer.getScaleY(), Float.valueOf(args[4]));
            z = t.math(arg1, entitydefServer.getScaleZ(), Float.valueOf(args[5]));
            entitydefServer.setScale(x,y,z);
            entitydefClient.setScale(x,y,z);

        }
        if(type.equalsIgnoreCase("move") || type.equalsIgnoreCase("tra")){
            x = t.math(arg1, entitydefServer.getTraX(), Float.valueOf(args[3]));
            y = t.math(arg1, entitydefServer.getTraY(), Float.valueOf(args[4]));
            z = t.math(arg1, entitydefServer.getTraZ(), Float.valueOf(args[5]));
            entitydefServer.setTra(x,y,z);
            entitydefClient.setTra(x,y,z);
        }

        if(type.equalsIgnoreCase("pos")){
            entitydefServer.setPosition(getBlockPos(null, 2, args));
            entitydefClient.setPosition(getBlockPos(null, 2, args));
        }
        if(type.equalsIgnoreCase("collision") || type.equalsIgnoreCase("col")){
            entitydefServer.setCollision(parseBoolean(arg1));
            entitydefClient.setCollision(parseBoolean(arg1));
        }
        if(type.equalsIgnoreCase("size")){
            if(entitydefClient instanceof EntityLavaBlock){
                EntityLavaBlock lavaBlock = (EntityLavaBlock) entitydefClient;
                lavaBlock.setWidth((float) parseDouble(arg1));
                lavaBlock.setHeight((float) parseDouble(args[3]));
            }
            if(entitydefServer instanceof EntityLavaBlock){
                EntityLavaBlock lavaBlock = (EntityLavaBlock) entitydefServer;
                lavaBlock.setWidth((float) parseDouble(arg1));
                lavaBlock.setHeight((float) parseDouble(args[3]));
            }
        }
        if(type.equalsIgnoreCase("invisible") || type.equalsIgnoreCase("inv")){
            if(entitydefClient instanceof EntityPreBlock){
                EntityPreBlock lavaBlock = (EntityPreBlock) entitydefClient;
                lavaBlock.setInv(parseBoolean(arg1));
            }
            if(entitydefServer instanceof EntityPreBlock){
                EntityPreBlock lavaBlock = (EntityPreBlock) entitydefServer;
                lavaBlock.setInv(parseBoolean(arg1));
            }
        }
        if(type.equalsIgnoreCase("trans")){
            x = t.math(arg1, entitydefServer.getTransparency(), Float.valueOf(args[3]));
            entitydefServer.setTransparency(x);
            entitydefClient.setTransparency(x);
        }
        if(type.equalsIgnoreCase("rgb")){
            x = t.math(arg1, entitydefServer.getRed(), Float.valueOf(args[3]));
            y = t.math(arg1, entitydefServer.getGreen(), Float.valueOf(args[4]));
            z = t.math(arg1, entitydefServer.getBlue(), Float.valueOf(args[5]));
            entitydefServer.setRGB(x,y,z);
            entitydefClient.setRGB(x,y,z);
        }
    }
}
