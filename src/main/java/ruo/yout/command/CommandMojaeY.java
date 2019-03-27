package ruo.yout.command;

import cmplus.util.CommandPlusBase;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import oneline.api.WorldAPI;
import ruo.yout.ItemUp;

import java.util.List;

public class CommandMojaeY extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args[0].equalsIgnoreCase("b")) {
            sender.getEntityWorld().setBlockState(sender.getPosition(), Block.getBlockFromName(args[1]).getDefaultState());
        }
        if (args[0].equalsIgnoreCase("e")) {
            EntityLiving living = (EntityLiving) EntityList.createEntityByName(args[1], sender.getEntityWorld());
            living.setPosition(sender.getPositionVector().xCoord, sender.getPositionVector().yCoord, sender.getPositionVector().zCoord);
            sender.getEntityWorld().spawnEntityInWorld(living);
            PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, 20000, 0);
            living.addPotionEffect(potioneffect);
        }
        if (args[0].equalsIgnoreCase("r")) {
            EntityLiving living = (EntityLiving) EntityList.createEntityByName(args[1], sender.getEntityWorld());
            living.setPosition(sender.getPositionVector().xCoord, sender.getPositionVector().yCoord, sender.getPositionVector().zCoord);
            sender.getEntityWorld().spawnEntityInWorld(living);
            sender.getCommandSenderEntity().startRiding(living);
            PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, 20000, 0);
            living.addPotionEffect(potioneffect);
        }

        if(args[0].equalsIgnoreCase("tp")){
            WorldAPI.teleport(sender.getPosition().getX(), Double.valueOf(args[1]), sender. getPosition().getZ());
        }
        if(args[0].equalsIgnoreCase("yy")){
            ItemUp.mo  = parseDouble(args[1]);
        }

    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args[0].equalsIgnoreCase("b"))
            return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
        else if (args[0].equalsIgnoreCase("e")) {
            return getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
