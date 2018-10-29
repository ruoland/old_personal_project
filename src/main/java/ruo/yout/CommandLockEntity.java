package ruo.yout;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;

public class CommandLockEntity extends CommandPlusBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for (Entity entity : sender.getEntityWorld().loadedEntityList) {
            if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
                System.out.println(EntityList.getEntityString(entity) + entity);
                if (EntityList.getEntityString(entity).equalsIgnoreCase(args[0])) {
                    if (entity.getCustomNameTag().equalsIgnoreCase("잠금")) {
                        entity.setCustomNameTag("");
                        System.out.println("잠금 해제 됐습니다");
                    }
                    else
                        entity.setCustomNameTag("잠금");
                }
            }
        }
    }
}
