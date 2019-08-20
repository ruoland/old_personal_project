package ruo.yout.y;

import cmplus.util.CommandPlusBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class CommandY extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;
        World world = sender.getEntityWorld();
        if (args[0].equalsIgnoreCase("mo")) {
            ItemStack itemStack = new ItemStack(Items.NAME_TAG);
            itemStack.setStackDisplayName(args.length < 1 ? "모리" : "모리 라이딩");
           player.inventory.addItemStackToInventory(itemStack);

        }
        if (args[0].equalsIgnoreCase("item")) {
            player.inventory.addItemStackToInventory(new ItemStack(Items.BOW));
            player.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, 64));
            player.inventory.addItemStackToInventory(new ItemStack(Items.FISHING_ROD));
            player.inventory.addItemStackToInventory(new ItemStack(Items.ENDER_PEARL, 64));

        }
        if (args[0].equalsIgnoreCase("summon")) {
            String name = args[1];
            double d0 = ((EntityPlayer) sender).posX;
            double d1 = ((EntityPlayer) sender).posY;
            double d2 = ((EntityPlayer) sender).posZ;
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if ("LightningBolt".equals(name)) {
                world.addWeatherEffect(new EntityLightningBolt(world, d0, d1, d2, false));
                notifyCommandListener(sender, this, "commands.summon.success", new Object[0]);
            }
            nbttagcompound.setString("id", name);
            Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, true);
            entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
            if (args.length > 1 && args[2].equalsIgnoreCase("riding")) {
                player.startRiding(entity);
            }
        }


    }
}
