package ruo.map.lot;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.cmplus.util.CommandPlusBase;
import ruo.map.lot.dungeon.EntityWind;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;

public class CommandLot extends CommandPlusBase {
    private int[] pos1, pos2;

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        super.execute(server, sender, args);
        LOTEvent.isLOT = true;
        if (args[0].equalsIgnoreCase("pos1")) {
            if (args.length > 1) {
                pos1 = new int[]{Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2])};
                return;
            }
            pos1 = WorldAPI.changePosArrayInt((EntityLivingBase) sender);
        }
        if (args[0].equalsIgnoreCase("pos2")) {
            if (args.length > 1) {
                pos2 = new int[]{Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2])};
                return;
            }
            pos2 = WorldAPI.changePosArrayInt((EntityLivingBase) sender);
        }
        if (args[0].equalsIgnoreCase("block")) {
            EntityFallBlock lavaBlock = new EntityFallBlock(sender.getEntityWorld());
            lavaBlock.setPosition(sender.getCommandSenderEntity().posX, sender.getCommandSenderEntity().posY, sender.getCommandSenderEntity().posZ);
            lavaBlock.setBlock(Blocks.BRICK_BLOCK);
            sender.getEntityWorld().spawnEntityInWorld(lavaBlock);
            lavaBlock.isFly = true;
        }
        if (args[0].equalsIgnoreCase("wind")) {
            EntityWind lavaBlock = new EntityWind(sender.getEntityWorld());
            lavaBlock.setPosition(sender.getCommandSenderEntity().posX, sender.getCommandSenderEntity().posY, sender.getCommandSenderEntity().posZ);
            lavaBlock.setBlock(Blocks.BRICK_BLOCK);
            sender.getEntityWorld().spawnEntityInWorld(lavaBlock);
            lavaBlock.isFly =true;
        }
        if (args[0].equalsIgnoreCase("set")) {
            WorldAPI.blockTick(sender.getEntityWorld(), pos1[0], pos2[0], pos1[1], pos2[1], pos1[2], pos2[2], new AbstractTick.BlockXYZ() {
                @Override
                public void run(TickEvent.Type type) {
                    EntityFallBlock lavaBlock = new EntityFallBlock(sender.getEntityWorld());
                    lavaBlock.setPosition(x, y, z);
                    lavaBlock.setBlock(Blocks.BRICK_BLOCK);
                    sender.getEntityWorld().spawnEntityInWorld(lavaBlock);
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("air"))
                            sender.getEntityWorld().setBlockState(getPos(), Blocks.AIR.getDefaultState());
                    }

                }
            });
        }
    }
}
