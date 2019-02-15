package minigameLib;

import cmplus.util.CommandPlusBase;
import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import minigameLib.minigame.minerun.invisibleblock.TileInvisible;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import map.lopre2.EntityPreBlock;
import map.lopre2.jump1.EntityLavaBlock;
import net.minecraft.util.math.BlockPos;

public class CommandMg extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
        IBlockState block = sender.getEntityWorld().getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
        BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
        if(block.getBlock() != Blocks.AIR){
            if(args[0].equalsIgnoreCase("com"))
            {
                TileInvisible tileInvisible = (TileInvisible) sender.getEntityWorld().getTileEntity(pos);
                tileInvisible.setCommand((t.getCommand(args, 1, args.length)));

            }
            if(args[0].equalsIgnoreCase("delay"))
            {
                TileInvisible tileInvisible = (TileInvisible) sender.getEntityWorld().getTileEntity(pos);
                tileInvisible.setDefaultDelay(parseInt(args[1]));
            }
        }
        if (entity != null) {
            EntityDefaultNPC entitydefServer = EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
            EntityDefaultNPC entitydefClient = (EntityDefaultNPC) entity;
            String type = args[0], arg1 = args[1];

            float x = 0, y = 0, z = 0;
            if (entity instanceof EntityPreBlock && args[0].equalsIgnoreCase("name")) {
                EntityPreBlock serverBlock = (EntityPreBlock) EntityDefaultNPC.getUUIDNPC(entity.getUniqueID());
                EntityPreBlock clientBlock = (EntityPreBlock) entity;
                String name = t.getCommand(args, 1, args.length);
                clientBlock.setJumpName(name);
                serverBlock.setJumpName(name);
            }

            if (type.equalsIgnoreCase("model")) {
                entitydefServer.setModel(TypeModel.valueOf(arg1));
                entitydefClient.setModel(TypeModel.valueOf(arg1));
            }
            if (type.equalsIgnoreCase("block")) {
                entitydefServer.setBlockMode(Block.getBlockFromName(arg1));
                entitydefClient.setBlockMode(Block.getBlockFromName(arg1));
            }
            if (type.equalsIgnoreCase("yp")) {
                entitydefServer.setLockYawPitch((float) parseDouble(arg1), (float)parseDouble(args[2]));
                entitydefClient.setLockYawPitch((float) parseDouble(arg1), (float)parseDouble(args[2]));

            }
            if (type.equalsIgnoreCase("yaw")) {
                entitydefServer.setLockYaw((float) parseDouble(arg1));
                entitydefClient.setLockYaw((float) parseDouble(arg1));
            }
            if (type.equalsIgnoreCase("pitch")) {
                entitydefServer.setLockPitch((float) parseDouble(arg1));
                entitydefClient.setLockPitch((float) parseDouble(arg1));
            }
            if (type.equalsIgnoreCase("rotate") || type.equalsIgnoreCase("ro")) {
                x = t.math(arg1, entitydefServer.getRotateX(), Float.valueOf(args[2]));
                y = t.math(arg1, entitydefServer.getRotateY(), Float.valueOf(args[3]));
                z = t.math(arg1, entitydefServer.getRotateZ(), Float.valueOf(args[4]));
                entitydefServer.setRotate(x, y, z);
                entitydefClient.setRotate(x, y, z);
            }
            if (type.equalsIgnoreCase("sc") || args[0].equalsIgnoreCase("scale")) {
                x = t.math(arg1, entitydefServer.getScaleX(), Float.valueOf(args[2]));
                y = t.math(arg1, entitydefServer.getScaleY(), Float.valueOf(args[3]));
                z = t.math(arg1, entitydefServer.getScaleZ(), Float.valueOf(args[4]));
                entitydefServer.setScale(x, y, z);
                entitydefClient.setScale(x, y, z);

            }
            if (type.equalsIgnoreCase("move") || type.equalsIgnoreCase("tra")) {
                x = t.math(arg1, entitydefServer.getTraX(), Float.valueOf(args[2]));
                y = t.math(arg1, entitydefServer.getTraY(), Float.valueOf(args[3]));
                z = t.math(arg1, entitydefServer.getTraZ(), Float.valueOf(args[4]));
                entitydefServer.setTra(x, y, z);
                entitydefClient.setTra(x, y, z);
            }

            if (type.equalsIgnoreCase("pos")) {
                entitydefServer.setPosition(getBlockPos(null, 2, args));
                entitydefClient.setPosition(getBlockPos(null, 2, args));
            }
            if (type.equalsIgnoreCase("collision") || type.equalsIgnoreCase("col")) {
                entitydefServer.setCollision(parseBoolean(arg1));
                entitydefClient.setCollision(parseBoolean(arg1));
            }
            if (type.equalsIgnoreCase("size")) {
                if (entitydefClient instanceof EntityLavaBlock) {
                    EntityLavaBlock lavaBlock = (EntityLavaBlock) entitydefClient;
                    lavaBlock.setWidth((float) parseDouble(arg1));
                    lavaBlock.setHeight((float) parseDouble(args[3]));
                }
                if (entitydefServer instanceof EntityLavaBlock) {
                    EntityLavaBlock lavaBlock = (EntityLavaBlock) entitydefServer;
                    lavaBlock.setWidth((float) parseDouble(arg1));
                    lavaBlock.setHeight((float) parseDouble(args[3]));
                }
            }
            if (type.equalsIgnoreCase("invisible") || type.equalsIgnoreCase("inv")) {
                entitydefServer.setCollision(parseBoolean(arg1));
                entitydefClient.setCollision(parseBoolean(arg1));
            }
            if (type.equalsIgnoreCase("trans")) {
                x = t.math(arg1, entitydefServer.getTransparency(), Float.valueOf(args[3]));
                entitydefServer.setTransparency(x);
                entitydefClient.setTransparency(x);
            }
            if (type.equalsIgnoreCase("rgb")) {
                x = t.math(arg1, entitydefServer.getRed(), Float.valueOf(args[3]));
                y = t.math(arg1, entitydefServer.getGreen(), Float.valueOf(args[4]));
                z = t.math(arg1, entitydefServer.getBlue(), Float.valueOf(args[5]));
                entitydefServer.setRGB(x, y, z);
                entitydefClient.setRGB(x, y, z);
            }
        }
    }
}
