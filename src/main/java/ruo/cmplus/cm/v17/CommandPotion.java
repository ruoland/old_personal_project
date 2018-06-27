package ruo.cmplus.cm.v17;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionType;
import net.minecraft.server.MinecraftServer;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.effect.ENEffect;

public class CommandPotion extends CommandPlusBase{

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.execute(server, sender, args);
		double[] xyz = getPos(sender,0, args);
		try {
			ENEffect.spawnPotion((EntityLivingBase) sender, xyz[0], xyz[1], xyz[2], (PotionType) PotionTypes.class.getField(args[3]).get(null));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
