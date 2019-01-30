package rmap.platformer;

import cmplus.deb.DebAPI;
import cmplus.util.CommandPlusBase;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandGiveCoin extends CommandPlusBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        if(args[0].equalsIgnoreCase("reload")) {
            DebAPI.reloadConfig();
        }
            if(args[0].equalsIgnoreCase("rope")){
            Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
            if(entity != null){
                if(entity instanceof EntityHookShot){
                    EntityHookShot hookShot = (EntityHookShot) entity;
                    hookShot.player = (EntityPlayer) sender.getCommandSenderEntity();
                }
            }
        }
        if(args[0].equalsIgnoreCase("nether")){//네더의 별 소환
            if(args.length > 1){
                EntityCoin coin = new EntityCoin(sender.getEntityWorld(),  Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]), new ItemStack(Items.NETHER_STAR));
                sender.getEntityWorld().spawnEntityInWorld(coin);
            }else {
                EntityCoin coin = new EntityCoin(sender.getEntityWorld(), sender.getPosition().getX(), sender.getPosition().getY() + 2, sender.getPosition().getZ(), new ItemStack(Items.NETHER_STAR));
                sender.getEntityWorld().spawnEntityInWorld(coin);
            }
        }
        else if(args[0].equalsIgnoreCase("coin")) {
            EntityCoin coin = new EntityCoin(sender.getEntityWorld(), sender.getPosition().getX(),  sender.getPosition().getY() + 2,  sender.getPosition().getZ(), new ItemStack(Plat.platCoin));
            sender.getEntityWorld().spawnEntityInWorld(coin);
        }
    }
}
